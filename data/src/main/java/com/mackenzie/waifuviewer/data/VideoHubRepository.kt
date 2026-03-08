package com.mackenzie.waifuviewer.data

import arrow.core.Either
import com.mackenzie.waifuviewer.data.datasource.VideoHubLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.VideoHubRemoteDataSource
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.getEmbedUrl
import com.mackenzie.waifuviewer.domain.getNameById
import com.mackenzie.waifuviewer.domain.getServerUrlById
import com.mackenzie.waifuviewer.domain.video.TagDomainInfo
import com.mackenzie.waifuviewer.domain.video.ThumbItem
import com.mackenzie.waifuviewer.domain.video.VideoDomainItem
import com.mackenzie.waifuviewer.domain.video.VideoItemDetails
import com.mackenzie.waifuviewer.domain.video.VideoListItem
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.net.URI
import javax.inject.Inject

class VideoHubRepository @Inject constructor(
    // val localDataSource: VideoHubLocalDataSource,
    val remoteDataSource: VideoHubRemoteDataSource,
    val okHttpClient: OkHttpClient,
    val moshi: Moshi
) {

    suspend fun requestVideoSources(): Error? {
        val videoList = remoteDataSource.getVideoServer()
            .fold(ifLeft = { return it }) {
                // save to database
                // Guardar en DBBD

            }


        return null
    }

    suspend fun requestVideoList(
        page: Int?,
        thumbsize: String?,
        search: String?,
        tags: List<String>?,
        stars: List<String>?,
        category: String?,
        ordering: String?,
        period: String?
    ):  Either<Error, VideoListItem> {
        return remoteDataSource.searchVideos(page, thumbsize, search, tags, stars, category, ordering, period)
    }

    suspend fun requestDefaultVideoList():  Either<Error, VideoListItem> {

        return remoteDataSource.searchVideos()
    }

    suspend fun primaryVideoScrapper(serverId: Int, serverUrl: String): Either<Error, VideoListItem> = withContext(Dispatchers.IO) {
        val serverName = getNameById(serverId)
        val baseUri = try { URI(serverUrl) } catch (_: Exception) { URI(getServerUrlById(serverId)) }
        val baseUrl = "${baseUri.scheme ?: "https"}://${baseUri.host ?: "www.beeg.com"}"

        println("Scrapeando $serverName ($serverId): $serverUrl")

        // Estrategia 1: Intentar obtener datos de API endpoints conocidos
        // val apiVideos = tryBeegApi(serverId, baseUrl)
        /*if (apiVideos.isNotEmpty()) {
            println("Videos extraídos de API: ${apiVideos.size}")
            return@withContext Either.Right(VideoListItem(videos = apiVideos.distinctBy { it.video.videoId }))
        }*/

        // Estrategia 2: Fetch HTML vía OkHttp y extraer datos de scripts JavaScript
        val (html, httpErr) = fetchHtml(serverUrl)
        if (httpErr != null && html.isBlank()) {
            return@withContext Either.Left(Error.Unknown(httpErr))
        }

        // Parse HTML con JSoup
        val doc = Jsoup.parse(html, baseUrl)
        val scrapedVideos = mutableListOf<VideoDomainItem>()

        // Estrategia 3: Extraer datos de scripts embebidos (JSON en JavaScript)
        val scripts = doc.select("script:not([src])")
        scripts.forEach { script ->
            val content = script.html()

            // Buscar patrones comunes de datos de videos en JavaScript
            // Patrón 1: Array de videos en formato JSON
            if (content.contains("videos") || content.contains("\"id\"") || content.contains("videoList")) {
                try {
                    // Extraer IDs de video
                    val idPattern = """"(?:id|video_id|videoId)"\s*:\s*"?(\d+)"?""".toRegex()
                    val titlePattern = """"(?:title|name|videoTitle)"\s*:\s*"([^"]+)"""".toRegex()
                    val thumbPattern = """"(?:thumb|thumbnail|preview|img)"\s*:\s*"([^"]+)"""".toRegex()
                    val durationPattern = """"(?:duration|length|time)"\s*:\s*"?(\d+)"?""".toRegex()
                    val viewsPattern = """"(?:views|view_count)"\s*:\s*"?(\d+)"?""".toRegex()

                    val ids = idPattern.findAll(content).map { it.groupValues[1] }.toList()
                    val titles = titlePattern.findAll(content).map { it.groupValues[1] }.toList()
                    val thumbs = thumbPattern.findAll(content).map { it.groupValues[1] }.toList()
                    val durations = durationPattern.findAll(content).map { it.groupValues[1] }.toList()
                    val views = viewsPattern.findAll(content).map { it.groupValues[1] }.toList()

                    ids.forEachIndexed { index, vidId ->
                        if (vidId.isNotEmpty()) {
                            val title = titles.getOrNull(index) ?: "Video $vidId"
                            val thumb = thumbs.getOrNull(index) ?: ""
                            val duration = durations.getOrNull(index) ?: ""
                            val viewCount = views.getOrNull(index) ?: ""

                            val videoUrl = "$baseUrl/$vidId"
                            val cleanThumb = when {
                                thumb.startsWith("//") -> "https:$thumb"
                                thumb.startsWith("/") -> baseUrl + thumb
                                thumb.startsWith("http") -> thumb
                                else -> ""
                            }

                            scrapedVideos.add(
                                createVideoItem(
                                    serverId = serverId,
                                    videoId = vidId,
                                    title = title.replace("\\u0027", "'").replace("\\\"", "\""),
                                    thumb = cleanThumb,
                                    url = videoUrl,
                                    baseUrl = baseUrl,
                                    duration = formatDuration(duration),
                                    views = viewCount,
                                    rating = ""
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    println("Error al parsear script JS: ${e.message}")
                }
            }
        }

        // Estrategia 4: Buscar elementos HTML estándar (similar a secondaryVideoScrapper)
        if (scrapedVideos.isEmpty()) {
            val videoElements = doc.select(
                "article.video, div.video-item, div.thumb-item, li.video, " +
                "div[data-id], article[data-id], div[data-video-id], " +
                "a[href*='/video/'], a[href*='/v/'], .video-block, .video-card"
            )

            videoElements.forEach { element ->
                try {
                    val videoId = element.attr("data-id")
                        .ifEmpty { element.attr("data-video-id") }
                        .ifEmpty { extractVideoIdFromHref(element.select("a").attr("href")) }

                    if (videoId.isEmpty()) return@forEach

                    val titleElement = element.select("a[title], .title, h2, h3").firstOrNull()
                    val title = titleElement?.attr("title")?.ifEmpty { titleElement.text() }
                        ?: element.select("img").attr("alt")
                        ?: "Video $videoId"

                    val href = element.select("a").attr("href")
                    val fullUrl = when {
                        href.startsWith("http") -> href
                        href.startsWith("/") -> baseUrl + href
                        else -> "$baseUrl/$videoId"
                    }

                    val img = element.select("img").firstOrNull()
                    val thumb = img?.let {
                        it.attr("data-src").ifEmpty { it.attr("src") }
                            .ifEmpty { it.attr("data-original") }
                    }?.let {
                        when {
                            it.startsWith("//") -> "https:$it"
                            it.startsWith("/") -> baseUrl + it
                            else -> it
                        }
                    } ?: ""

                    val duration = element.select(".duration, .time, var").text()
                    val views = element.select(".views, .view-count").text()

                    scrapedVideos.add(
                        createVideoItem(serverId, videoId, title, thumb, fullUrl, baseUrl, duration, views)
                    )
                } catch (e: Exception) {
                    // Ignorar elementos mal formados
                }
            }
        }

        println("Videos encontrados: ${scrapedVideos.size}")
        return@withContext if (scrapedVideos.isEmpty()) {
            Either.Left(Error.Unknown("No se encontraron videos en $serverName"))
        } else {
            Either.Right(VideoListItem(videos = scrapedVideos.distinctBy { it.video.videoId }))
        }
    }

    // Función auxiliar para formatear duración (de segundos a mm:ss)
    private fun formatDuration(seconds: String): String {
        return try {
            val totalSeconds = seconds.toLongOrNull() ?: return seconds
            val minutes = totalSeconds / 60
            val secs = totalSeconds % 60
            String.format(java.util.Locale.US, "%d:%02d", minutes, secs)
        } catch (_: Exception) {
            seconds
        }
    }

    suspend fun secondaryVideoScrapper(serverId: Int, serverUrl: String): Either<Error, VideoListItem> =
        withContext(Dispatchers.IO) {
            val serverName = getNameById(serverId)
            val baseUrl = getServerUrlById(serverId)

            // Log.d("VideoHubViewModel", "Scrapeando $serverName ($serverId): $serverUrl")
            println( "Scrapeando $serverName ($serverId): $serverUrl" )

            val doc = Jsoup.connect(serverUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .referrer("https://www.google.com")
                .timeout(20000)
                .followRedirects(true)
                .get()

            val scrapedVideos = mutableListOf<VideoDomainItem>()

            // 1. Estrategia específica para Beeg (ID 3) - Extracción de JSON en scripts
            /*if (serverId == 18) {
                val scripts = doc.select("script:not([src])")
                scripts.forEach { script ->
                    val content = script.html()
                    if (content.contains("videos") && content.contains("\"id\"")) {
                        try {
                            val jsonPattern = """[\s\S]*?\{[\s\S]*?"id"[\s\S]*?\}[\s\S]*?]""".toRegex()
                            jsonPattern.findAll(content).forEach { match ->
                                val jsonStr = match.value
                                val idPat = """"id"\s*:\s*"?(\d+)"?""".toRegex()
                                val titPat = """"title"\s*:\s*"([^"]+)"""".toRegex()
                                val thPat = """"thumb"\s*:\s*"([^"]+)"""".toRegex()

                                val ids = idPat.findAll(jsonStr).map { it.groupValues[1] }.toList()
                                val titles = titPat.findAll(jsonStr).map { it.groupValues[1] }.toList()
                                val thumbs = thPat.findAll(jsonStr).map { it.groupValues[1] }.toList()

                                ids.forEachIndexed { index, vidId ->
                                    val title = titles.getOrNull(index) ?: ""
                                    val thumb = thumbs.getOrNull(index) ?: ""
                                    if (title.isNotEmpty()) {
                                        scrapedVideos.add(createVideoItem(serverId, vidId, title, thumb, "$baseUrl/$vidId", baseUrl))
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            // Log.e("VideoHubViewModel", "Error Beeg scripts: ${e.message}")
                        }
                    }
                }
            }*/

            // 2. Estrategia HTML Genérica para todos los servidores (PornHub, RedTube, XVideos, etc.)
            if (scrapedVideos.isEmpty()) {
                // Lista amplia de selectores comunes en sitios de videos
                val videoElements = doc.select(
                    "li[data-video-vkey], div.videoBox, div.pcVideoListItem, article.thumb-item, " +
                            "div.thumb-block, div.hvideo, div.video-wrapper, div.mozaique, " +
                            "article[data-id], div[data-id], .thumb-item, .video-item, .video-block, " +
                            "div[class*=videoblock], li[class*=pcVideoListItem]"
                )

                // Log.d("VideoHubViewModel", "Elementos HTML encontrados: ${videoElements.size}")

                videoElements.forEach { element ->
                    try {
                        // Extraer ID
                        val videoId = element.attr("data-video-vkey")
                            .ifEmpty { element.attr("data-id") }
                            .ifEmpty { element.attr("data-video-id") }
                            .ifEmpty { extractVideoIdFromHref(element.select("a").attr("href")) }

                        if (videoId.isEmpty()) return@forEach

                        // Extraer Título
                        val titleElement = element.select("a[title], .title a, h2 a, h3 a, span.title").firstOrNull()
                            ?: element.select("a").firstOrNull()

                        val title = titleElement?.attr("title")?.ifEmpty { titleElement.text() }
                            ?: titleElement?.text()
                            ?: element.select("img").attr("alt")
                            ?: "Video $videoId"

                        // Extraer URL
                        val href = element.select("a").attr("href")
                        val fullUrl = when {
                            href.startsWith("http") -> href
                            href.startsWith("/") -> baseUrl + href
                            else -> "$baseUrl/$href"
                        }

                        // Extraer Thumbnail
                        val img = element.select("img").firstOrNull()
                        val thumb = img?.let {
                            it.attr("data-src").ifEmpty { it.attr("src") }
                                .ifEmpty { it.attr("data-original") }
                                .ifEmpty { it.attr("data-thumb_url") }
                                .ifEmpty { it.attr("data-mediabook") }
                        }?.let {
                            when {
                                it.startsWith("//") -> "https:$it"
                                it.startsWith("/") -> baseUrl + it
                                else -> it
                            }
                        } ?: ""

                        // Metadatos adicionales
                        val duration = element.select(".duration, .time, .video-duration, var").text()
                        val views = element.select(".views, .video-views, span.views").text()
                        val rating = element.select(".rating, .percent, .value, .rate, .rating-container .value").text()
                        val tags = element.select("a.tag, .tags a, .videoTagsBlock a").map { TagDomainInfo(it.text().trim()) }.takeIf { it.isNotEmpty() }

                        scrapedVideos.add(
                            createVideoItem(serverId, videoId, title, thumb, fullUrl, baseUrl, duration, views, rating, tags)
                        )
                    } catch (e: Exception) { /* Omitir elementos mal formados */ }
                }
            }

            println("videos encontrados: ${scrapedVideos.size}")
            return@withContext if (scrapedVideos.isEmpty()) {
                Either.Left(Error.Unknown("No se encontraron videos en $serverName"))
            } else {
                Either.Right(VideoListItem(videos = scrapedVideos.distinctBy { v -> v.video.videoId }))
            }
        }

    suspend fun tertiaryVideoScrapper() {

    }

    private fun extractVideoIdFromHref(href: String): String {
        return when {
            href.contains("viewkey=") -> href.substringAfter("viewkey=").substringBefore("&").substringBefore("?")
            href.contains("v=") -> href.substringAfter("v=").substringBefore("&").substringBefore("?")
            href.contains("/video/") -> href.substringAfter("/video/").substringBefore("/").substringBefore("?")
            href.contains("/video-") -> href.substringAfter("/video-").substringBefore("/").substringBefore("?")
            href.contains("/v/") -> href.substringAfter("/v/").substringBefore("/").substringBefore("?")
            href.matches(Regex(".*/\\d+.*")) -> {
                val segments = href.split("/").filter { it.isNotEmpty() }
                segments.find { it.matches(Regex("\\d+")) } ?: ""
            }
            else -> ""
        }
    }

    private fun fetchHtml(url: String): Pair<String, String?> {
        val request = Request.Builder()
            .url(url)
            .headers(
                Headers.Builder()
                    .add("User-Agent", "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36")
                    .add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8")
                    .add("Accept-Language", "es-ES,es;q=0.9,en-US;q=0.8,en;q=0.7")
                    .add("Cache-Control", "no-cache")
                    .add("Pragma", "no-cache")
                    .add("Upgrade-Insecure-Requests", "1")
                    .add("Sec-Fetch-Dest", "document")
                    .add("Sec-Fetch-Mode", "navigate")
                    .add("Sec-Fetch-Site", "none")
                    .build()
            )
            .get()
            .build()

        okHttpClient.newCall(request).execute().use { resp ->
            val body = resp.body?.string().orEmpty()
            val finalUrl = resp.request.url.toString()
            val err = if (!resp.isSuccessful) "HTTP ${resp.code}" else null
            return body to (err?.let { "$it ($finalUrl)" })
        }
    }

    private fun createVideoItem(
        serverId: Int,
        videoId: String,
        title: String,
        thumb: String,
        url: String,
        baseUrl: String,
        duration: String = "",
        views: String = "",
        rating: String = "",
        tags: List<TagDomainInfo>? = null
    ): VideoDomainItem {
        val xvideosId = url.substringAfter("/video.", "").substringBefore("/").substringBefore("?")
        return VideoDomainItem(
            video = VideoItemDetails(
                videoId = videoId,
                title = title,
                thumb = thumb,
                url = url,
                embedUrl = if (serverId == 9) getEmbedUrl(serverId, xvideosId) else getEmbedUrl(serverId, videoId),
                publishDate = "",
                rating = rating.replace("%", "").trim(),
                ratings = "0",
                views = views.trim(),
                duration = duration.trim(),
                defaultThumb = thumb,
                type = "video",
                thumbs = if (thumb.isNotEmpty()) listOf(ThumbItem("medium", "640", "480", thumb)) else emptyList(),
                tags = tags,
                stars = null
            )
        )
    }

}