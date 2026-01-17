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

@HiltViewModel
class PlayerViewModel @Inject constructor(): ViewModel() {

    private val _state = MutableStateFlow(PlayerUiState())
    val state: StateFlow<PlayerUiState> = _state.asStateFlow()

    fun getVideoFromEmbeddedUrl(url: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            // Aquí podrías implementar la lógica para extraer el video desde la URL embebida
            // Por ahora, solo simulamos una carga exitosa sin datos reales

            https://es.pornhub.com/embed/694a9bbbf23fd

            _state.update {
                it.copy(
                    isLoading = false,
                    embeddedVideoFile = "", // TODO
                    error = "Funcionalidad no implementada"
                )
            }
        }
    }

    data class PlayerUiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val embeddedVideoFile: String? = null
    )

}