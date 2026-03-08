package com.mackenzie.waifuviewer.domain.video.embed

/**
 * Resultado de la resolución de un embed a un recurso reproducible por ExoPlayer.
 *
 * @param mediaUrl URL directa a un recurso MP4/HLS/DASH.
 * @param mediaType Tipo inferido por extensión.
 * @param playbackHeaders Headers típicos necesarios para reproducir (p.ej. Referer).
 */
data class EmbeddedVideoResolveResult(
    val mediaUrl: String,
    val mediaType: MediaType,
    val playbackHeaders: Map<String, String> = emptyMap(),
    val resolvedFromUrl: String
) {
    enum class MediaType { MP4, HLS, DASH, UNKNOWN }
}