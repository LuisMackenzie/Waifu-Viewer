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
import com.mackenzie.waifuviewer.usecases.video.GetVideoDefaultListUseCase
import com.mackenzie.waifuviewer.usecases.video.GetVideoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class VideoHubViewModel @Inject constructor(
    private val getVideoListUseCase: GetVideoListUseCase,
    private val getDefaultListUseCase: GetVideoDefaultListUseCase,
    private val okHttpClient: OkHttpClient,
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

    fun getxHamsterUrlFetcher(serverUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

                val serverId = 6
                val baseUri = try { URI(serverUrl) } catch (_: Exception) { URI(getServerUrlById(serverId)) }
                val baseUrl = "${baseUri.scheme ?: "https"}://${baseUri.host ?: "www.xhamster.com"}"

                Log.d("VideoHubViewModel", "Scrapeando XHamster: $serverUrl")

                fun fetchHtml(url: String): Pair<String, String?> {
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

                // 1) Fetch HTML vía OkHttp (más control de headers/redirects/cookies)
                val (html, httpErr) = fetchHtml(serverUrl)
                if (httpErr != null && html.isBlank()) {
                    _state.update { it.copy(isLoading = false, videos = emptyList(), error = "Error al cargar xHamster: $httpErr") }
                    return@launch
                }

                // 2) Parse
                val doc = Jsoup.parse(html, baseUrl)

                // 3) Detección de bloqueo (más conservadora para evitar falsos positivos)
                val htmlLower = html.lowercase()
                val titleLower = doc.title().lowercase()

                val looksLikeChallenge =
                    listOf("captcha", "cloudflare", "access denied", "verify you are human", "enable javascript").any { token ->
                        titleLower.contains(token) || htmlLower.contains(token)
                    }

                // Si es un challenge, normalmente el HTML es corto y sin anchors de videos
                val videoAnchorsCount = doc.select("a[href*=/videos/]").size
                val extremelyShort = html.length < 10_000

                if (looksLikeChallenge && (videoAnchorsCount == 0 || extremelyShort)) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            videos = emptyList(),
                            error = "xHamster devolvió una página de verificación (anti-bot / requiere JavaScript)."
                        )
                    }
                    return@launch
                }

                val scrapedVideos = mutableListOf<VideoDomainItem>()

                // 1) Intento principal: cards de video por selectores comunes en xHamster
                val primarySelectors = listOf(
                    "article",
                    "div.video-thumb",
                    "div.thumb-list__item",
                    "div.video-item",
                    "div[data-video-id]",
                    "li[data-video-id]"
                )

                fun resolveUrlMaybeRelative(raw: String): String {
                    if (raw.isBlank()) return ""
                    return try {
                        when {
                            raw.startsWith("http") -> raw
                            raw.startsWith("//") -> "https:$raw"
                            else -> baseUri.resolve(raw).toString()
                        }
                    } catch (_: Exception) {
                        when {
                            raw.startsWith("//") -> "https:$raw"
                            raw.startsWith("/") -> baseUrl + raw
                            else -> "$baseUrl/$raw"
                        }
                    }
                }

                fun extractXhamsterIdFromUrl(url: String): String {
                    // Ejemplos comunes: /videos/slug-12345678 o /videos/12345678/...
                    val numeric = Regex("(?:-|/)(\\d{4,})(?:\\b|/|\\?|$)").find(url)?.groupValues?.get(1)
                    return numeric ?: extractVideoIdFromHref(url)
                }

                fun parseCard(element: Element): VideoDomainItem? {
                    // URL principal
                    val linkEl = element.selectFirst(
                        "a[href*=/videos/], a[href*=/video/], a[href*=/porn/], a[href]"
                    ) ?: return null

                    val href = linkEl.attr("href")
                    val fullUrl = resolveUrlMaybeRelative(href)
                    if (fullUrl.isBlank()) return null

                    // Evitar enlaces que no son videos (por ej. perfiles/categorías)
                    if (!fullUrl.contains("/videos/")) {
                        val looksLikeVideo = fullUrl.contains("/video") || fullUrl.contains("/porn")
                        if (!looksLikeVideo) return null
                    }

                    // ID
                    val videoId = element.attr("data-video-id")
                        .ifEmpty { element.attr("data-id") }
                        .ifEmpty { extractXhamsterIdFromUrl(fullUrl) }
                        .trim()
                    if (videoId.isBlank()) return null

                    // Metadatos (los calculamos antes para poder validar el título)
                    val duration = element.select(
                        ".duration, .time, [class*=duration], [class*=time]"
                    ).text().trim()

                    val views = element.select(
                        ".views, [class*=views]"
                    ).text().trim()

                    fun normalizeTitle(raw: String): String {
                        return raw
                            .replace("\u00A0", " ")
                            .replace(Regex("\\s+"), " ")
                            .trim()
                    }

                    fun looksLikeDuration(text: String): Boolean {
                        val t = text.trim()
                        if (t.isBlank()) return false
                        // Formatos típicos: 12:34, 1:02:03
                        if (Regex("^\\d{1,2}:\\d{2}(?::\\d{2})?$").matches(t)) return true
                        // A veces: "12 min" o "12m"
                        if (Regex("^\\d+\\s*(min|mins|m)$", RegexOption.IGNORE_CASE).matches(t)) return true
                        return false
                    }

                    fun candidateTitles(): List<String> {
                        val candidates = mutableListOf<String>()

                        // 1) Atributos comunes en el link
                        candidates += linkEl.attr("title")
                        candidates += linkEl.attr("aria-label")
                        candidates += linkEl.attr("data-title")

                        // 2) Títulos dentro de headings
                        candidates += element.selectFirst("h1")?.text().orEmpty()
                        candidates += element.selectFirst("h2")?.text().orEmpty()
                        candidates += element.selectFirst("h3")?.text().orEmpty()

                        // 3) Elementos con clases típicas de título
                        candidates += element.selectFirst(".title")?.text().orEmpty()
                        candidates += element.selectFirst("[class*=title]")?.text().orEmpty()

                        // 4) Meta semántico
                        candidates += element.selectFirst("meta[itemprop=name]")?.attr("content").orEmpty()
                        candidates += element.selectFirst("[itemprop=name]")?.attr("content").orEmpty()
                        candidates += element.selectFirst("[itemprop=name]")?.text().orEmpty()

                        // 5) Fallback: alt de imagen
                        candidates += element.selectFirst("img[alt]")?.attr("alt").orEmpty()

                        // 6) Último recurso: texto del link (pero puede estar contaminado)
                        candidates += linkEl.text()

                        return candidates
                            .map(::normalizeTitle)
                            .filter { it.isNotBlank() }
                            .distinct()
                    }

                    val title = candidateTitles()
                        .firstOrNull { cand ->
                            // No aceptamos títulos que claramente sean solo duración
                            if (looksLikeDuration(cand)) return@firstOrNull false

                            // Si coincide exactamente con duration, descartarlo
                            if (duration.isNotBlank() && cand.equals(duration, ignoreCase = true)) return@firstOrNull false

                            // Evitar títulos excesivamente cortos que suelen ser ruido (ej: "HD")
                            if (cand.length < 4) return@firstOrNull false

                            true
                        }
                        ?: run {
                            // Último fallback: si todo falla, usar el alt aunque sea corto
                            val alt = normalizeTitle(element.selectFirst("img[alt]")?.attr("alt").orEmpty())
                            if (alt.isNotBlank() && !looksLikeDuration(alt) && alt != duration) alt else ""
                        }

                    if (title.isBlank()) return null

                    // Thumb
                    val img = element.selectFirst("img")
                    val thumbRaw = img?.attr("data-src")
                        ?.ifEmpty { img.attr("data-original") }
                        ?.ifEmpty { img.attr("data-lazy") }
                        ?.ifEmpty { img.attr("src") }
                        ?.ifEmpty { element.selectFirst("video[poster]")?.attr("poster") ?: "" }
                        ?: ""

                    val thumb = resolveUrlMaybeRelative(thumbRaw)

                    Log.e("VideoHubViewModel", "xHamster Video Found - ID: $videoId, Title: $title, Duration: $duration")

                    return createVideoItem(
                        serverId = serverId,
                        videoId = videoId,
                        title = title,
                        thumb = thumb,
                        url = fullUrl,
                        baseUrl = baseUrl,
                        duration = duration,
                        views = views
                    )
                }

                // 1a) Recogida por selectores principales
                // Procesamos TODOS los selectores para maximizar la cantidad de videos encontrados
                for (selector in primarySelectors) {
                    val cards = doc.select(selector)
                    if (cards.isEmpty()) continue

                    cards.forEach { el ->
                        try {
                            parseCard(el)?.let { video ->
                                // Evitar duplicados durante la recolección
                                val isDuplicate = scrapedVideos.any {
                                    it.video.videoId == video.video.videoId ||
                                    it.video.url == video.video.url
                                }
                                if (!isDuplicate) {
                                    scrapedVideos.add(video)
                                }
                            }
                        } catch (_: Exception) {
                            // ignorar
                        }
                    }

                    // Continuamos con todos los selectores en lugar de hacer break temprano
                    // Esto permite recolectar videos de diferentes estructuras HTML en la misma página
                }
                Log.e("VideoHubViewModel", "scrapedVideos.size después de primarySelectors= ${scrapedVideos.size}")


                // 2) Fallback: anchors directos a /videos/
                if (scrapedVideos.isEmpty()) {
                    val anchors = doc.select("a[href*=/videos/]")
                    anchors.forEach { a ->
                        try {
                            val container = a.parent() ?: a
                            parseCard(container) ?: run {
                                val href = resolveUrlMaybeRelative(a.attr("href"))
                                val videoId = extractXhamsterIdFromUrl(href)
                                val title = a.attr("title").ifEmpty { a.text() }.trim()
                                if (videoId.isNotBlank() && title.isNotBlank()) {
                                    scrapedVideos.add(
                                        createVideoItem(
                                            serverId = serverId,
                                            videoId = videoId,
                                            title = title,
                                            thumb = "",
                                            url = href,
                                            baseUrl = baseUrl
                                        )
                                    )
                                }
                            }
                        } catch (_: Exception) {
                            // ignorar
                        }
                    }
                }

                val deduped = scrapedVideos
                    .distinctBy { it.video.videoId.ifBlank { it.video.url } }
                Log.e("VideoHubViewModel", "xHamster Videos scrapeados exitosamente: ${deduped.size}, scrapedVideos.size= ${scrapedVideos.size}")

                _state.update {
                    it.copy(
                        isLoading = false,
                        videos = deduped,
                        error = if (deduped.isEmpty()) {
                            "No se encontraron videos en xHamster. Título: '${doc.title()}'. Es posible que la estructura haya cambiado o que el sitio requiera JavaScript."
                        } else null
                    )
                }

            } catch (e: Exception) {
                Log.e("VideoHubViewModel", "Error al scrapear xHamster: ${e.message}", e)
                _state.update { it.copy(isLoading = false, error = "Error al cargar videos: ${e.message}") }
            }
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
