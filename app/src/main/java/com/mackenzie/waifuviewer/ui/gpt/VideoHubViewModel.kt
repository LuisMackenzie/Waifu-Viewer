package com.mackenzie.waifuviewer.ui.gpt

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.domain.video.VideoDomainItem
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
                // Hacer la petición HTTP y obtener el documento HTML
                val doc = Jsoup.connect("https://document.agi-app.es/")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(10000) // timeout de 10 segundos
                    .get()

                // Obtener el título de la página
                val title = doc.title()
                Log.e("VideoHubViewModel", "Título de la página: $title")

                // Obtener el HTML completo
                val htmlCompleto = doc.html()
                // Log.e("VideoHubViewModel", "HTML completo: $htmlCompleto")

                // Seleccionar elementos específicos

                // 1. Todos los enlaces (tags <a>)
                val enlaces = doc.select("a").map { element ->
                    val texto = element.text()
                    val href = element.attr("href")
                    "Texto: $texto -> URL: $href"
                }
                Log.e("VideoHubViewModel", "Enlaces encontrados: $enlaces")

                // 2. Elementos por clase CSS
                val elementosPorClase = doc.select(".mi-clase").map { it.text() }
                Log.e("VideoHubViewModel", "Elementos por clase: $elementosPorClase")

                // 3. Elemento por ID
                val elementoPorId = doc.select("#mi-id").text()
                Log.e("VideoHubViewModel", "Elemento por ID: $elementoPorId")

                // 4. Selector CSS personalizado (ejemplo: div con clase específica > p)
                val seleccionPersonalizada = doc.select("div.clase > p").map { it.text() }
                Log.e("VideoHubViewModel", "Selección personalizada: $seleccionPersonalizada")

                // 5. Obtener todos los headers (h1, h2, h3, etc)
                val headers = doc.select("h1, h2, h3, h4, h5, h6").map {
                    "${it.tagName()}: ${it.text()}"
                }
                Log.e("VideoHubViewModel", "Headers: $headers")

                // 6. Obtener todas las imágenes
                val imagenes = doc.select("img").map { img ->
                    val src = img.attr("src")
                    val alt = img.attr("alt")
                    "Alt: $alt -> Src: $src"
                }
                Log.e("VideoHubViewModel", "Imágenes: $imagenes")

            } catch (e: Exception) {
                Log.e("VideoHubViewModel", "Error al scrapear: ${e.message}", e)
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