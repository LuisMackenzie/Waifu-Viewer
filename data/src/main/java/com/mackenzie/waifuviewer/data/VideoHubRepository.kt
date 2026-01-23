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
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
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

    suspend fun primaryVideoScrapper(serverUrl: String): Either<Error, VideoListItem> = withContext(Dispatchers.IO) {
        // TODO implement primary scrapper
        return@withContext Either.Right(VideoListItem(emptyList()))
    }

    suspend fun secondaryVideoScrapper(serverId: Int, serverUrl: String): Either<Error, VideoListItem> =
        withContext(Dispatchers.IO) {
            val serverName = getNameById(serverId)
            val baseUrl = getServerUrlById(serverId)

            // Log.d("VideoHubViewModel", "Scrapeando $serverName ($serverId): $serverUrl")

            val doc = Jsoup.connect(serverUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .referrer("https://www.google.com")
                .timeout(20000)
                .followRedirects(true)
                .get()

            val scrapedVideos = mutableListOf<VideoDomainItem>()

            // 1. Estrategia específica para Beeg (ID 3) - Extracción de JSON en scripts
            if (serverId == 3 || serverId == 6) {
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
            }

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

            return@withContext Either.Right(VideoListItem(videos = scrapedVideos.distinctBy { v -> v.video.videoId }))
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