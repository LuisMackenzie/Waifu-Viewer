package com.mackenzie.waifuviewer.ui.gpt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mackenzie.waifuviewer.data.embed.EmbeddedVideoResolveError
import com.mackenzie.waifuviewer.usecases.embed.ResolveEmbeddedVideoUrlUseCase
import arrow.core.Either

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val resolveEmbeddedVideoUrlUseCase: ResolveEmbeddedVideoUrlUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PlayerUiState())
    val state: StateFlow<PlayerUiState> = _state.asStateFlow()

    fun getVideoFromEmbeddedUrl(url: String, embedUrl: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, embeddedVideoFile = null) }

            val primaryUrl = url.trim()
            val fallbackUrl = embedUrl.trim()

            val result = resolveEmbeddedVideoUrlUseCase(primaryUrl)
                .fold(
                    ifLeft = { firstError ->
                        // Segundo intento solo si hay una alternativa no vacía y distinta.
                        if (fallbackUrl.isNotBlank() && fallbackUrl != primaryUrl) {
                            resolveEmbeddedVideoUrlUseCase(fallbackUrl)
                        } else {
                            Either.Left(firstError)
                        }
                    },
                    ifRight = { resolved -> Either.Right(resolved) }
                )

            result.fold(
                ifLeft = { err ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            embeddedVideoFile = null,
                            error = err.toHumanMessage()
                        )
                    }
                },
                ifRight = { resolved ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            embeddedVideoFile = resolved.mediaUrl,
                            error = null
                        )
                    }
                }
            )
        }
    }

    private fun EmbeddedVideoResolveError.toHumanMessage(): String = when (this) {
        is EmbeddedVideoResolveError.Http -> "HTTP ${code}: ${message}"
        is EmbeddedVideoResolveError.Network -> message
        is EmbeddedVideoResolveError.NotFound -> message
        is EmbeddedVideoResolveError.RequiresJavaScript -> message
    }

    data class PlayerUiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val embeddedVideoFile: String? = null
    )

}