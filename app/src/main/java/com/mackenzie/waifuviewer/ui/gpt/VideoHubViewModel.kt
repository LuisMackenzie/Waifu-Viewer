package com.mackenzie.waifuviewer.ui.gpt

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.domain.getEmbedUrl
import com.mackenzie.waifuviewer.domain.getNameById
import com.mackenzie.waifuviewer.domain.getServerUrlById
import com.mackenzie.waifuviewer.domain.video.TagDomainInfo
import com.mackenzie.waifuviewer.domain.video.ThumbItem
import com.mackenzie.waifuviewer.domain.video.VideoDomainItem
import com.mackenzie.waifuviewer.domain.video.VideoItemDetails
import com.mackenzie.waifuviewer.domain.video.embed.ServerSpec
import com.mackenzie.waifuviewer.usecases.video.GetVideoDefaultListUseCase
import com.mackenzie.waifuviewer.usecases.video.GetVideoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import javax.inject.Inject

@HiltViewModel
class VideoHubViewModel @Inject constructor(
    private val getVideoListUseCase: GetVideoListUseCase,
    private val getDefaultListUseCase: GetVideoDefaultListUseCase
): ViewModel() {

    private val _state = MutableStateFlow(VideoHubUiState())
    val state: StateFlow<VideoHubUiState> = _state.asStateFlow()

    fun getVideoList(
        page: Int?,
        thumbsize: String?,
        search: String?,
        tags: List<String>?,
        stars: List<String>?,
        category: String?,
        ordering: String?,
        period: String?
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getVideoListUseCase(
                page, thumbsize, search, tags, stars, category, ordering, period
            ).fold(
                ifLeft = { error ->
                    _state.update { it.copy(isLoading = false, error = error.toString()) }
                },
                ifRight = { videoListItem ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            videos = videoListItem.videos,
                            error = null
                        )
                    }
                })
        }
    }

    fun getDefaultVideoList() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getDefaultListUseCase().fold(
                ifLeft = { error ->
                    _state.update { it.copy(isLoading = false, error = error.toString()) }
                },
                ifRight = { videoListItem ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            videos = videoListItem.videos,
                            error = null
                        )
                    }
                }
            )
        }
    }

    /**
     * Scrapea videos de cualquier servidor soportado en ServerSpec.
     */
    fun getJavaScriptUrlFetcher(serverId: Int, serverUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update { it.copy(isLoading = true, error = null) }
                
                val serverName = getNameById(serverId)
                val baseUrl = getServerUrlById(serverId)

                Log.d("VideoHubViewModel", "Scrapeando $serverName ($serverId): $serverUrl")

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
                            } catch (e: Exception) { Log.e("VideoHubViewModel", "Error Beeg scripts: ${e.message}") }
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

                    Log.d("VideoHubViewModel", "Elementos HTML encontrados: ${videoElements.size}")

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

                _state.update {
                    it.copy(
                        isLoading = false,
                        videos = scrapedVideos.distinctBy { v -> v.video.videoId },
                        error = if (scrapedVideos.isEmpty()) "No se encontraron videos en $serverName." else null
                    )
                }

            } catch (e: Exception) {
                Log.e("VideoHubViewModel", "Error al scrapear $serverId: ${e.message}")
                _state.update { it.copy(isLoading = false, error = "Error al cargar videos: ${e.message}") }
            }
        }
    }

    // Mantener getPHUrlFetcher por compatibilidad, delegando al genérico
    /*fun getPHUrlFetcher(serverUrl: String) {
        getJavaScriptUrlFetcher(1, serverUrl)
    }*/

    fun getPHUrlFetcher(serverUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

                Log.d("VideoHubViewModel", "cargando la siguiente URL..: $serverUrl")

                // Hacer la petición HTTP y obtener el documento HTML
                val doc = Jsoup.connect(serverUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .referrer("https://www.google.com")
                    .timeout(15000)
                    .followRedirects(true)
                    .get()

                Log.d("VideoHubViewModel", "Título de la página: ${doc.title()}")

                // Scraping de videos - Pornhub usa diferentes selectores dependiendo del layout
                var videoElements = doc.select("li[data-video-vkey]")

                if (videoElements.isEmpty()) {
                    Log.w("VideoHubViewModel", "Intentando selectores alternativos...")
                    videoElements = doc.select("div.videoBox, div.pcVideoListItem")
                }

                if (videoElements.isEmpty()) {
                    videoElements = doc.select("div[class*=videoblock], li[class*=pcVideoListItem]")
                }

                Log.d("VideoHubViewModel", "Elementos de video encontrados: ${videoElements.size}")

                val scrapedVideos = videoElements.mapNotNull { element ->
                    try {
                        // Extraer videoId con múltiples estrategias
                        val videoId = element.attr("data-video-vkey").ifEmpty {
                            val href = element.select("a").attr("href")
                            when {
                                href.contains("viewkey=") -> href.substringAfter("viewkey=").substringBefore("&")
                                href.contains("/view_video.php") -> href.substringAfter("viewkey=").substringBefore("&")
                                href.startsWith("/video/") -> href.substringAfter("/video/")
                                else -> ""
                            }
                        }

                        // Extraer título con múltiples estrategias
                        val titleElement = element.select("a[title]").firstOrNull()
                            ?: element.select(".title a").firstOrNull()
                            ?: element.select("span.title").firstOrNull()
                            ?: element.select("a").firstOrNull()

                        val title = titleElement?.attr("title")?.ifEmpty { titleElement.text() }
                            ?: titleElement?.text()
                            ?: ""

                        // Extraer URL
                        val url = element.select("a").attr("href").let { href ->
                            when {
                                href.startsWith("http") -> href
                                href.startsWith("/") -> "https://es.pornhub.com$href"
                                else -> "https://es.pornhub.com/$href"
                            }
                        }

                        // Extraer thumbnail con múltiples estrategias
                        val imgElement = element.select("img").firstOrNull()
                        val thumb = imgElement?.attr("data-src")?.ifEmpty {
                            imgElement.attr("src")
                        }?.ifEmpty {
                            imgElement.attr("data-thumb_url")
                        }?.ifEmpty {
                            imgElement.attr("data-mediabook")
                        } ?: ""

                        // Extraer duración
                        val duration = element.select(".duration").text().ifEmpty {
                            element.select(".marker-overlays var").text().ifEmpty {
                                element.select("var.duration").text()
                            }
                        }

                        // Extraer vistas
                        val views = element.select(".views").text().ifEmpty {
                            element.select(".videoDetailsBlock var").text().ifEmpty {
                                element.select("span.views").text()
                            }
                        }

                        // Extraer rating
                        val rating = element.select(".value").text().ifEmpty {
                            element.select(".percent").text().ifEmpty {
                                element.select(".rating-container .value").text()
                            }
                        }

                        // Extraer tags si están disponibles
                        val tags = element.select(".videoTagsBlock a, .pstar-list-btn").mapNotNull { tag ->
                            val tagText = tag.text().trim()
                            if (tagText.isNotEmpty()) TagDomainInfo(tagName = tagText) else null
                        }

                        // Validar que tengamos datos mínimos necesarios
                        if (title.isNotEmpty()) {
                            VideoDomainItem(
                                video = VideoItemDetails(
                                    videoId = videoId.ifEmpty { System.currentTimeMillis().toString() },
                                    title = title,
                                    thumb = thumb,
                                    url = url,
                                    embedUrl = if (videoId.isNotEmpty()) "https://es.pornhub.com/embed/$videoId" else "",
                                    publishDate = "",
                                    rating = rating.replace("%", "").trim(),
                                    ratings = "0",
                                    views = views.trim(),
                                    duration = duration.trim(),
                                    defaultThumb = thumb,
                                    type = "video",
                                    thumbs = if (thumb.isNotEmpty()) listOf(ThumbItem(
                                        size = "medium",
                                        width = "640",
                                        height = "480",
                                        src = thumb
                                    )) else emptyList(),
                                    tags = tags.takeIf { it.isNotEmpty() },
                                    stars = null
                                )
                            )
                        } else {
                            Log.w("VideoHubViewModel", "Video ignorado - título vacío")
                            null
                        }
                    } catch (e: Exception) {
                        Log.e("VideoHubViewModel", "Error al parsear video individual: ${e.message}", e)
                        null
                    }
                }

                Log.d("VideoHubViewModel", "Videos scrapeados exitosamente: ${scrapedVideos.size}")

                if (scrapedVideos.isEmpty()) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            videos = emptyList(),
                            error = "No se encontraron videos. Es posible que la estructura de la página haya cambiado."
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            videos = scrapedVideos,
                            error = null
                        )
                    }
                }

            } catch (e: Exception) {
                Log.e("VideoHubViewModel", "Error al scrapear: ${e.message}", e)
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar videos: ${e.message}"
                    )
                }
            }
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
        val xvideosId = url.substringAfter("/video.", "").substringBefore( "/").substringBefore("?")
        return VideoDomainItem(
            video = VideoItemDetails(
                videoId = videoId,
                title = title,
                thumb = thumb,
                url = url,
                embedUrl = if(serverId == 9) getEmbedUrl(serverId, xvideosId) else getEmbedUrl(serverId, videoId),
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

    data class VideoHubUiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val videos: List<VideoDomainItem> = emptyList()
    )


}
