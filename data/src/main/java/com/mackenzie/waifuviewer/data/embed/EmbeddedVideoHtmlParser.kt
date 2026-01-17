package com.mackenzie.waifuviewer.data.embed

import com.mackenzie.waifuviewer.domain.video.embed.EmbeddedVideoResolveResult
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URI

/**
 * Parser puro (sin red) que intenta extraer una URL reproducible desde HTML estático.
 */
internal object EmbeddedVideoHtmlParser {

    fun parse(html: String, baseUrl: String): ParsedCandidate? {
        val doc = Jsoup.parse(html, baseUrl)
        return parse(doc, baseUrl)
    }

    fun parse(doc: Document, baseUrl: String): ParsedCandidate? {
        findFromVideoTags(doc, baseUrl)?.let { return it }
        findFromMeta(doc, baseUrl)?.let { return it }
        findFromScripts(doc, baseUrl)?.let { return it }
        return null
    }

    data class ParsedCandidate(
        val mediaUrl: String,
        val mediaType: EmbeddedVideoResolveResult.MediaType
    )

    private fun findFromVideoTags(doc: Document, baseUrl: String): ParsedCandidate? {
        doc.select("video").firstOrNull()?.let { video ->
            video.attr("abs:src").takeIf { it.isNotBlank() }?.let { return ParsedCandidate(it, inferMediaType(it)) }

            video.select("source[src]")
                .asSequence()
                .mapNotNull { it.absUrl("src").takeIf(String::isNotBlank) }
                .firstOrNull(::isPlayable)
                ?.let { return ParsedCandidate(it, inferMediaType(it)) }
        }

        doc.select("source[src]")
            .asSequence()
            .mapNotNull { it.absUrl("src").takeIf(String::isNotBlank) }
            .firstOrNull(::isPlayable)
            ?.let { return ParsedCandidate(it, inferMediaType(it)) }

        doc.select("video[data-src], source[data-src]")
            .asSequence()
            .mapNotNull { el -> makeAbsoluteUrl(baseUrl, el.attr("data-src")) }
            .firstOrNull(::isPlayable)
            ?.let { return ParsedCandidate(it, inferMediaType(it)) }

        return null
    }

    private fun findFromMeta(doc: Document, baseUrl: String): ParsedCandidate? {
        val selectors = listOf(
            "meta[property=og:video]",
            "meta[property=og:video:url]",
            "meta[name=twitter:player:stream]",
            "meta[name=twitter:player:stream:url]",
            "link[rel=preload][as=video]"
        )

        for (selector in selectors) {
            val el = doc.selectFirst(selector) ?: continue
            val raw = when (el.tagName()) {
                "meta" -> el.attr("content")
                "link" -> el.attr("href")
                else -> ""
            }
            val abs = makeAbsoluteUrl(baseUrl, raw) ?: continue
            if (isPlayable(abs)) return ParsedCandidate(abs, inferMediaType(abs))
        }
        return null
    }

    private fun findFromScripts(doc: Document, baseUrl: String): ParsedCandidate? {
        val absRegex = Regex(
            "https?://[^\"'\\s>]+\\.(mp4|m3u8|mpd)(\\?[^\"'\\s>]*)?",
            RegexOption.IGNORE_CASE
        )

        // Acepta cosas tipo:
        //  file: "...mp4" | file:'...mp4' | "file":"...mp4" | 'file':'...mp4'
        val relRegex = Regex(
            "(?i)(?:src|file|hls|dash)\\s*[:=]\\s*['\"]([^'\"]+\\.(?:mp4|m3u8|mpd)[^'\"]*)['\"]"
        )

        doc.select("script").forEach { script ->
            val data = script.data().ifBlank { script.html() }
            absRegex.find(data)?.value?.let { url ->
                if (isPlayable(url)) return ParsedCandidate(url, inferMediaType(url))
            }
        }

        doc.select("script").forEach { script ->
            val data = script.data().ifBlank { script.html() }
            val match = relRegex.find(data) ?: return@forEach
            val raw = match.groupValues.getOrNull(1).orEmpty()
            val abs = makeAbsoluteUrl(baseUrl, raw) ?: return@forEach
            if (isPlayable(abs)) return ParsedCandidate(abs, inferMediaType(abs))
        }

        return null
    }

    private fun isPlayable(url: String): Boolean {
        val lower = url.lowercase()
        return lower.contains(".mp4") || lower.contains(".m3u8") || lower.contains(".mpd")
    }

    private fun inferMediaType(url: String): EmbeddedVideoResolveResult.MediaType {
        val lower = url.lowercase()
        return when {
            lower.contains(".m3u8") -> EmbeddedVideoResolveResult.MediaType.HLS
            lower.contains(".mpd") -> EmbeddedVideoResolveResult.MediaType.DASH
            lower.contains(".mp4") -> EmbeddedVideoResolveResult.MediaType.MP4
            else -> EmbeddedVideoResolveResult.MediaType.UNKNOWN
        }
    }

    private fun makeAbsoluteUrl(baseUrl: String, maybeRelative: String?): String? {
        val raw = maybeRelative?.trim().orEmpty()
        if (raw.isBlank()) return null
        if (raw.startsWith("http://") || raw.startsWith("https://")) return raw
        return try {
            URI(baseUrl).resolve(raw).toString()
        } catch (_: Throwable) {
            null
        }
    }
}
