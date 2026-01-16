package com.mackenzie.waifuviewer.ui.gpt

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.domain.video.TagDomainInfo
import com.mackenzie.waifuviewer.domain.video.ThumbItem
import com.mackenzie.waifuviewer.domain.video.VideoDomainItem
import com.mackenzie.waifuviewer.domain.video.VideoItemDetails
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

    fun getBeegUrlFetcher(serverUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

                Log.d("VideoHubViewModel", "Cargando URL de Beeg: $serverUrl")

                // Hacer la petición HTTP y obtener el documento HTML
                val doc = Jsoup.connect(serverUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .referrer("https://www.google.com")
                    .timeout(15000)
                    .followRedirects(true)
                    .ignoreContentType(false)
                    .get()

                Log.d("VideoHubViewModel", "Título de la página: ${doc.title()}")

                val scrapedVideos = mutableListOf<VideoDomainItem>()

                // Beeg carga contenido dinámicamente con JavaScript
                // Intentar extraer datos del JSON embebido en scripts
                Log.d("VideoHubViewModel", "Buscando datos en scripts de Beeg...")
                val scripts = doc.select("script:not([src])")

                var foundVideos = false
                scripts.forEach { script ->
                    val content = script.html()

                    // Beeg suele tener datos en formato: window.INITIALSTATE o similar
                    if (content.contains("videos") && content.contains("\"id\"")) {
                        Log.d("VideoHubViewModel", "Script con datos encontrado")

                        try {
                            // Intentar extraer arrays JSON del script
                            val jsonPattern = """[\s\S]*?\{[\s\S]*?"id"[\s\S]*?\}[\s\S]*?]""".toRegex()
                            val matches = jsonPattern.findAll(content)

                            matches.forEach { match ->
                                val jsonStr = match.value
                                Log.d("VideoHubViewModel", "JSON encontrado: ${jsonStr.take(200)}")

                                // Aquí podrías usar una librería JSON como Gson o Kotlinx Serialization
                                // Por ahora, extraemos manualmente los campos básicos
                                val videoIdPattern = """"id"\s*:\s*"?(\d+)"?""".toRegex()
                                val titlePattern = """"title"\s*:\s*"([^"]+)"""".toRegex()
                                val thumbPattern = """"thumb"\s*:\s*"([^"]+)"""".toRegex()

                                val videoIds = videoIdPattern.findAll(jsonStr).map { it.groupValues[1] }.toList()
                                val titles = titlePattern.findAll(jsonStr).map { it.groupValues[1] }.toList()
                                val thumbs = thumbPattern.findAll(jsonStr).map { it.groupValues[1] }.toList()

                                videoIds.forEachIndexed { index, videoId ->
                                    val title = titles.getOrNull(index) ?: ""
                                    val thumb = thumbs.getOrNull(index) ?: ""

                                    if (title.isNotEmpty() && videoId.isNotEmpty()) {
                                        scrapedVideos.add(
                                            VideoDomainItem(
                                                video = VideoItemDetails(
                                                    videoId = videoId,
                                                    title = title,
                                                    thumb = if (thumb.startsWith("//")) "https:$thumb" else thumb,
                                                    url = "https://beeg.com/$videoId",
                                                    embedUrl = "https://beeg.com/embed/$videoId",
                                                    publishDate = "",
                                                    rating = "",
                                                    ratings = "0",
                                                    views = "",
                                                    duration = "",
                                                    defaultThumb = if (thumb.startsWith("//")) "https:$thumb" else thumb,
                                                    type = "video",
                                                    thumbs = if (thumb.isNotEmpty()) listOf(
                                                        ThumbItem(
                                                            size = "medium",
                                                            width = "640",
                                                            height = "480",
                                                            src = if (thumb.startsWith("//")) "https:$thumb" else thumb
                                                        )
                                                    ) else emptyList(),
                                                    tags = null,
                                                    stars = null
                                                )
                                            )
                                        )
                                        foundVideos = true
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("VideoHubViewModel", "Error al parsear JSON de script: ${e.message}", e)
                        }
                    }
                }

                // Si no se encontraron videos en scripts, intentar scraping HTML tradicional
                if (!foundVideos) {
                    Log.w("VideoHubViewModel", "No se encontraron videos en scripts, intentando HTML...")
                    var videoElements = doc.select("article.thumb-item, div.thumb-item, div.video-item, article.video")

                    if (videoElements.isEmpty()) {
                        videoElements = doc.select("article[data-id], div[data-id]")
                    }

                    Log.d("VideoHubViewModel", "Elementos de video encontrados: ${videoElements.size}")

                    Log.d("VideoHubViewModel", "Elementos de video encontrados: ${videoElements.size}")

                    val htmlVideos = videoElements.mapNotNull { element ->
                        try {
                            // Extraer videoId - Beeg usa data-id o está en la URL
                            val videoId = element.attr("data-id").ifEmpty {
                                element.attr("data-video-id").ifEmpty {
                                    val href = element.select("a").attr("href")
                                    when {
                                        href.contains("/video/") -> href.substringAfter("/video/").substringBefore("/").substringBefore("?")
                                        href.matches(Regex(".*/\\d+.*")) -> href.split("/").find { it.matches(Regex("\\d+")) } ?: ""
                                        else -> ""
                                    }
                                }
                            }

                            // Extraer título
                            val titleElement = element.select("a[title]").firstOrNull()
                                ?: element.select("h2 a, h3 a").firstOrNull()
                                ?: element.select(".title a, .video-title a").firstOrNull()
                                ?: element.select("a").firstOrNull()

                            val title = titleElement?.attr("title")?.ifEmpty { titleElement.text() }
                                ?: titleElement?.text()
                                ?: element.select("img").attr("alt")
                                ?: ""

                            // Extraer URL
                            val url = element.select("a").attr("href").let { href ->
                                when {
                                    href.startsWith("http") -> href
                                    href.startsWith("/") -> "https://beeg.com$href"
                                    else -> "https://beeg.com/$href"
                                }
                            }

                            // Extraer thumbnail
                            val imgElement = element.select("img").firstOrNull()
                            val thumb = imgElement?.attr("data-src")?.ifEmpty {
                                imgElement.attr("src")
                            }?.ifEmpty {
                                imgElement.attr("data-lazy")
                            }?.ifEmpty {
                                imgElement.attr("data-original")
                            }?.let { src ->
                                when {
                                    src.startsWith("http") -> src
                                    src.startsWith("//") -> "https:$src"
                                    src.startsWith("/") -> "https://beeg.com$src"
                                    else -> src
                                }
                            } ?: ""

                            // Extraer duración
                            val duration = element.select(".duration, .time, .video-duration").text().ifEmpty {
                                element.select("span[class*=duration], span[class*=time]").text()
                            }

                            // Extraer vistas
                            val views = element.select(".views, .video-views").text().ifEmpty {
                                element.select("span[class*=view]").text()
                            }

                            // Extraer rating
                            val rating = element.select(".rating, .rate").text().ifEmpty {
                                element.select("span[class*=rating]").text()
                            }

                            // Extraer tags si están disponibles
                            val tags = element.select("a.tag, .tags a").mapNotNull { tag ->
                                val tagText = tag.text().trim()
                                if (tagText.isNotEmpty()) TagDomainInfo(tagName = tagText) else null
                            }

                            // Validar que tengamos datos mínimos necesarios
                            if (title.isNotEmpty() && url.isNotEmpty()) {
                                VideoDomainItem(
                                    video = VideoItemDetails(
                                        videoId = videoId.ifEmpty { System.currentTimeMillis().toString() },
                                        title = title,
                                        thumb = thumb,
                                        url = url,
                                        embedUrl = if (videoId.isNotEmpty()) "https://beeg.com/embed/$videoId" else "",
                                        publishDate = "",
                                        rating = rating.replace("%", "").trim(),
                                        ratings = "0",
                                        views = views.trim(),
                                        duration = duration.trim(),
                                        defaultThumb = thumb,
                                        type = "video",
                                        thumbs = if (thumb.isNotEmpty()) listOf(
                                            ThumbItem(
                                                size = "medium",
                                                width = "640",
                                                height = "480",
                                                src = thumb
                                            )
                                        ) else emptyList(),
                                        tags = tags.takeIf { it.isNotEmpty() },
                                        stars = null
                                    )
                                )
                            } else {
                                Log.w("VideoHubViewModel", "Video de Beeg ignorado - datos insuficientes (título: $title, url: $url)")
                                null
                            }
                        } catch (e: Exception) {
                            Log.e("VideoHubViewModel", "Error al parsear video individual de Beeg: ${e.message}", e)
                            null
                        }
                    }

                    scrapedVideos.addAll(htmlVideos)
                }

                Log.d("VideoHubViewModel", "Videos de Beeg scrapeados exitosamente: ${scrapedVideos.size}")

                if (scrapedVideos.isEmpty()) {
                    // Intentar imprimir información de debug
                    Log.d("VideoHubViewModel", "HTML snippet: ${doc.body().html().take(500)}")

                    _state.update {
                        it.copy(
                            isLoading = false,
                            videos = emptyList(),
                            error = "No se encontraron videos en Beeg. La página puede usar JavaScript para cargar contenido dinámicamente o la estructura ha cambiado."
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
                Log.e("VideoHubViewModel", "Error al scrapear Beeg: ${e.message}", e)
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar videos de Beeg: ${e.message}"
                    )
                }
            }
        }
    }

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


    data class VideoHubUiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val videos: List<VideoDomainItem> = emptyList()
    )


}