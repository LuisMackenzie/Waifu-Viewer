package com.mackenzie.waifuviewer.data.embed

sealed class EmbeddedVideoResolveError {
    data class Network(val message: String, val cause: Throwable? = null) : EmbeddedVideoResolveError()
    data class Http(val code: Int, val message: String) : EmbeddedVideoResolveError()
    data class NotFound(val message: String = "No se encontró una URL de vídeo reproducible en el HTML") : EmbeddedVideoResolveError()
    data class RequiresJavaScript(val message: String = "La página parece requerir JavaScript para obtener la URL del vídeo") : EmbeddedVideoResolveError()
}
