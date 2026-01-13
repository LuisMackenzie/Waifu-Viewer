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

    fun getUrlFetcher() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

                // Hacer la petición HTTP y obtener el documento HTML
                val doc = Jsoup.connect("https://es.pornhub.com/video/")
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



    /*fun getHTMLRequestPage() {
        viewModelScope.launch {
            // Ejemplo 1: Obtener HTML completo
            val html: String = skrape(HttpFetcher) {
                request {
                    url = "https://luisbaez.dev/"
                    // Opcional: añadir headers
                    headers = mapOf(
                        "User-Agent" to "Mozilla/5.0"
                    )
                }

                response {
                    htmlDocument {
                        html // retorna el HTML completo
                    }
                }
            }
            Log.e("VideoHubViewModel", "HTML completo: $html")

            // Ejemplo 2: Scrapear elementos específicos
            val scrapedData = skrape(HttpFetcher) {
                request {
                    url = "https://luisbaez.dev/"
                }

                response {
                    htmlDocument {
                        // Seleccionar elementos por CSS selector
                        val title = titleText // obtiene el título de la página

                        // Seleccionar todos los enlaces (tag <a>)
                        val allLinks = findAll("a").map { element ->
                            "${element.text} -> ${element.attribute("href")}"
                        }

                        // Seleccionar por clase (elementos con class="mi-clase")
                        val divsByClass = findAll(".mi-clase").map { it.text }

                        // Seleccionar por ID (elementos con id="mi-id")
                        val elementById = findFirst("#mi-id").text

                        // Seleccionar con selector CSS personalizado
                        val customSelection = findAll("div.clase > p").map { it.text }

                        // Retornar los datos scrapeados
                        mapOf(
                            "title" to title,
                            "links" to allLinks,
                            "divsByClass" to divsByClass,
                            "elementById" to elementById,
                            "customSelection" to customSelection
                        )
                    }
                }
            }

            Log.e("VideoHubViewModel", "Datos scrapeados: $scrapedData")

            // Ejemplo 3: Uso avanzado - Extraer datos estructurados
            try {
                val resultado = skrape(HttpFetcher) {
                    request {
                        url = "https://ejemplo.com/articulos"
                        timeout = 10000 // timeout en milisegundos
                        headers = mapOf(
                            "User-Agent" to "Mozilla/5.0"
                        )
                    }

                    response {
                        htmlDocument {
                            relaxed = true // modo relajado para HTML mal formado

                            // Extraer múltiples artículos
                            findAll("article").map { article ->
                                mapOf(
                                    "titulo" to article.findFirst("h2").text,
                                    "descripcion" to article.findFirst("p").text,
                                    "enlace" to article.findFirst("a").attribute("href")
                                )
                            }
                        }
                    }
                }
                Log.e("VideoHubViewModel", "Resultado: $resultado")
            } catch (e: Exception) {
                Log.e("VideoHubViewModel", "Error al scrapear: ${e.message}")
            }
        }
    }*/


    data class VideoHubUiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val videos: List<VideoDomainItem> = emptyList()
    )


}