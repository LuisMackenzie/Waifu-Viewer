package com.mackenzie.waifuviewer.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.data.toError
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuBestItem
import com.mackenzie.waifuviewer.usecases.best.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaifuBestViewModel @Inject constructor(
    getWaifuBestUseCase: GetWaifuBestUseCase,
    private val requestWaifuBestUseCase: RequestWaifuBestUseCase,
    private val requestMoreBestUseCase: RequestMoreWaifuBestUseCase,
): ViewModel()  {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getWaifuBestUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) }}
                .collect{ waifusPng -> _state.update { UiState(waifus = waifusPng) } }
        }
    }

    fun onBestReady(tag: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val error = requestWaifuBestUseCase(tag)
            _state.update { it.copy(isLoading = false, error = error) }
        }
    }

    fun onRequestMore(tag: String) {
        viewModelScope.launch {
            val error = requestMoreBestUseCase(tag)
            _state.update { it.copy(error = error) }
        }
    }

    data class UiState(
        val isLoading: Boolean? = null,
        val waifus: List<WaifuBestItem>? = null,
        val error: Error? = null
    )

}