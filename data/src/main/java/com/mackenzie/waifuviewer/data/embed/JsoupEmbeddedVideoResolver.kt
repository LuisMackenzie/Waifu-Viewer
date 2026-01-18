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
                // Identificamos si el servidor es uno de los soportados
                val server = identifyServer(embedUrl)

                // Intentamos obtener el documento, ya sea URL completa o embed
                val initial = fetchDocument(embedUrl, referer = null)
                
                // Resolvemos usando el documento obtenido
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
        // Señales comunes de que el contenido se genera con JS.
        val bodyText = doc.body().text().lowercase()
        val htmlLen = doc.html().length
        val scriptCount = doc.select("script").size
        if (htmlLen < 800 && scriptCount > 0 && (bodyText.contains("enable javascript") || bodyText.isBlank())) {
            return EmbeddedVideoResolveError.RequiresJavaScript().left()
        }

        // Intento 1: Parser genérico (Video tags, meta tags, scripts conocidos)
        EmbeddedVideoHtmlParser.parse(doc, baseUrl)?.let { parsed ->
            return EmbeddedVideoResolveResult(
                mediaUrl = parsed.mediaUrl,
                mediaType = parsed.mediaType,
                playbackHeaders = defaultPlaybackHeaders(referer = baseUrl),
                resolvedFromUrl = baseUrl
            ).right()
        }

        // Estrategia D: Buscar iframe y seguirlo (reintentar una vez).
        // Esto funciona tanto para webs que envuelven el video en iframe como para embeds.
        findIframeUrl(doc, baseUrl)?.let { iframeUrl ->
            val iframeDocEither = fetchDocumentEither(iframeUrl, referer = baseUrl)
            return iframeDocEither.fold(
                ifLeft = { it.left() },
                ifRight = { iframeDoc ->
                    // Recursión: Si encontramos un iframe, intentamos resolver desde ahí
                    // pasando el mismo server si aplica (o re-identificando si fuera necesario, pero mantenemos el contexto)
                    resolveFromDocument(iframeDoc, baseUrl = iframeUrl, server = server)
                        ?: EmbeddedVideoResolveError.NotFound().left()
                }
            )
        }

        return null
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

    private fun defaultPlaybackHeaders(referer: String?): Map<String, String> {
        if (referer.isNullOrBlank()) return emptyMap()
        // Muchos CDNs bloquean sin Referer.
        return mapOf("Referer" to referer)
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
            .header(
                "User-Agent",
                "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0 Mobile Safari/537.36"
            )
            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .header("Accept-Language", "es-ES,es;q=0.9,en;q=0.8")

        if (!referer.isNullOrBlank()) {
            requestBuilder.header("Referer", referer)
        }

        val response = okHttpClient.newCall(requestBuilder.build()).execute()
        response.use {
            if (!it.isSuccessful) {
                throw HttpException(it.code, it.message)
            }
            val body = it.body?.string().orEmpty()
            return Jsoup.parse(body, url)
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

    private class HttpException(val code: Int, override val message: String) : RuntimeException(message)
}
