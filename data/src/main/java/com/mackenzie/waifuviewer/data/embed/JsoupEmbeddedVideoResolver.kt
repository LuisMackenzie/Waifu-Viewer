package com.mackenzie.waifuviewer.data.embed

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.mackenzie.waifuviewer.data.datasource.EmbeddedVideoResolver
import com.mackenzie.waifuviewer.domain.video.embed.EmbeddedVideoResolveResult
import com.mackenzie.waifuviewer.domain.video.embed.ServerSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URI
import javax.inject.Inject

class JsoupEmbeddedVideoResolver @Inject constructor(
    private val okHttpClient: OkHttpClient,
) : EmbeddedVideoResolver {

    // Lista de servidores soportados (IDs 1-22) definidos en VideoItem.kt
    private val supportedServers = ServerSpec.getSupportedServers()

    override suspend fun resolve(embedUrl: String): Either<EmbeddedVideoResolveError, EmbeddedVideoResolveResult> =
        withContext(Dispatchers.IO) {
            try {
                val server = identifyServer(embedUrl)
                val initial = fetchDocument(embedUrl, referer = null)
                val result = resolveFromDocument(initial, baseUrl = embedUrl, server = server)
                result ?: EmbeddedVideoResolveError.NotFound().left()
            } catch (e: HttpException) {
                EmbeddedVideoResolveError.Http(code = e.code, message = e.message).left()
            } catch (t: Throwable) {
                EmbeddedVideoResolveError.Network(message = t.message ?: "Error de red", cause = t).left()
            }
        }

    private fun resolveFromDocument(
        doc: Document,
        baseUrl: String,
        server: ServerSpec?
    ): Either<EmbeddedVideoResolveError, EmbeddedVideoResolveResult>? {
        // 0. Detección de JS obligatorio
        if (requiresJavaScript(doc)) {
            return EmbeddedVideoResolveError.RequiresJavaScript().left()
        }

        // 1. Estrategia Específica por Servidor (Si conocemos trucos para ese server)
        resolveServerSpecific(doc, baseUrl, server)?.let { return it.right() }

        // 2. Parser Genérico (Video tags, Meta, Scripts comunes)
        EmbeddedVideoHtmlParser.parse(doc, baseUrl)?.let { parsed ->
            return createResult(parsed.mediaUrl, parsed.mediaType, baseUrl).right()
        }

        // 3. Estrategia JSON-LD (Estructura estándar de metadatos de video)
        // findFromJsonLd(doc, baseUrl)?.let { return it.right() }

        // 4. Estrategia de Configuración en Atributos (data-config, data-sources)
        findFromDataAttributes(doc, baseUrl)?.let { return it.right() }

        // 5. Estrategia de Iframe (Recursión una sola vez para evitar bucles)
        findIframeUrl(doc, baseUrl)?.let { iframeUrl ->
            val iframeDocEither = fetchDocumentEither(iframeUrl, referer = baseUrl)
            return iframeDocEither.fold(
                ifLeft = { it.left() },
                ifRight = { iframeDoc ->
                    // Recursión: Intentamos resolver desde el iframe, manteniendo el servidor si es el mismo
                    resolveFromDocument(iframeDoc, baseUrl = iframeUrl, server = server ?: identifyServer(iframeUrl))
                        ?: EmbeddedVideoResolveError.NotFound().left()
                }
            )
        }

        return null
    }

    private fun resolveServerSpecific(doc: Document, baseUrl: String, server: ServerSpec?): EmbeddedVideoResolveResult? {
        if (server == null) return null
        
        return when (server.id) {
            9, 10 -> { // XVideos / XNXX
                // Buscan variables como html5player.setVideoUrlHigh('...')
                val script = doc.select("script").find { it.data().contains("setVideoUrl") }
                val url = script?.data()?.let { data ->
                    Regex("setVideoUrl(?:High|Low)\\s*\\(\\s*['\"]([^'\"]+)['\"]").find(data)?.groupValues?.get(1)
                }
                url?.let { createResult(it, inferType(it), baseUrl) }
            }
            1 -> { // PornHub
                // A veces está en un objeto JSON mediaDefinitions
                val script = doc.select("script").find { it.data().contains("mediaDefinitions") }
                val url = script?.data()?.let { data ->
                    Regex("\"videoUrl\"\\s*:\\s*\"([^\"]+)\"").find(data)?.groupValues?.get(1)?.replace("\\/", "/")
                }
                url?.let { createResult(it, inferType(it), baseUrl) }
            }
            else -> null
        }
    }

    /*private fun findFromJsonLd(doc: Document, baseUrl: String): EmbeddedVideoResolveResult? {
        val scripts = doc.select("script[type=application/ld+json]")
        for (script in scripts) {
            try {
                val json = JSONObject(script.data())
                val contentUrl = json.optString("contentUrl").takeIf { it.isNotBlank() }
                    ?: json.optJSONObject("video")?.optString("contentUrl")
                
                contentUrl?.let {
                    return createResult(it, inferType(it), baseUrl)
                }
            } catch (_: Exception) {}
        }
        return null
    }*/

    private fun findFromDataAttributes(doc: Document, baseUrl: String): EmbeddedVideoResolveResult? {
        // Busca en cualquier elemento que tenga data-config o data-sources que parezca JSON
        val elements = doc.select("[*[data-config]], [*[data-sources]]")
        for (el in elements) {
            val config = el.attr("data-config").ifBlank { el.attr("data-sources") }
            if (config.contains(".mp4") || config.contains(".m3u8")) {
                // Intento simple de extraer la URL si el JSON es complejo
                Regex("https?://[^\"'\\s]+\\.(?:mp4|m3u8)").find(config)?.value?.let {
                    return createResult(it, inferType(it), baseUrl)
                }
            }
        }
        return null
    }

    private fun createResult(url: String, type: EmbeddedVideoResolveResult.MediaType, referer: String) = 
        EmbeddedVideoResolveResult(
            mediaUrl = url,
            mediaType = type,
            playbackHeaders = mapOf("Referer" to referer, "User-Agent" to "Mozilla/5.0 (Linux; Android 13)"),
            resolvedFromUrl = referer
        )

    private fun inferType(url: String): EmbeddedVideoResolveResult.MediaType {
        return when {
            url.contains(".m3u8") -> EmbeddedVideoResolveResult.MediaType.HLS
            url.contains(".mpd") -> EmbeddedVideoResolveResult.MediaType.DASH
            else -> EmbeddedVideoResolveResult.MediaType.MP4
        }
    }

    private fun requiresJavaScript(doc: Document): Boolean {
        val bodyText = doc.body().text().lowercase()
        val scriptCount = doc.select("script").size
        return (doc.html().length < 1000 && scriptCount > 0 && 
               (bodyText.contains("enable javascript") || bodyText.isBlank()))
    }

    private fun identifyServer(url: String): ServerSpec? {
        val host = try { URI(url).host } catch (e: Exception) { return null } ?: return null
        val lowerHost = host.lowercase()
        return supportedServers.find { spec -> 
            spec.domains.any { domain -> lowerHost.contains(domain) } 
        }
    }

    private fun findIframeUrl(doc: Document, baseUrl: String): String? {
        val iframe = doc.selectFirst("iframe[src]") ?: return null
        val src = iframe.attr("src")
        return makeAbsoluteUrl(baseUrl, src)
    }

    private fun fetchDocumentEither(url: String, referer: String?): Either<EmbeddedVideoResolveError, Document> {
        return try {
            val doc = fetchDocument(url, referer)
            doc.right()
        } catch (e: HttpException) {
            EmbeddedVideoResolveError.Http(code = e.code, message = e.message).left()
        } catch (t: Throwable) {
            EmbeddedVideoResolveError.Network(t.message ?: "Error de red", t).left()
        }
    }

    private fun fetchDocument(url: String, referer: String?): Document {
        val requestBuilder = Request.Builder()
            .url(url)
            .get()
            .header("User-Agent", "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0 Mobile Safari/537.36")
            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")

        if (!referer.isNullOrBlank()) requestBuilder.header("Referer", referer)

        val response = okHttpClient.newCall(requestBuilder.build()).execute()
        response.use {
            if (!it.isSuccessful) throw HttpException(it.code, it.message)
            return Jsoup.parse(it.body?.string().orEmpty(), url)
        }
    }

    private fun makeAbsoluteUrl(baseUrl: String, maybeRelative: String?): String? {
        val raw = maybeRelative?.trim().orEmpty()
        if (raw.isBlank()) return null
        if (raw.startsWith("http://") || raw.startsWith("https://")) return raw
        if (raw.startsWith("//")) return "https:$raw"
        return try { URI(baseUrl).resolve(raw).toString() } catch (_: Throwable) { null }
    }
    private class HttpException(val code: Int, override val message: String) : RuntimeException(message)
}
