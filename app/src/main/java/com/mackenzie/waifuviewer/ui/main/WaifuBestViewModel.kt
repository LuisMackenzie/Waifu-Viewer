package com.mackenzie.waifuviewer.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.data.toError
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuBestItemGif
import com.mackenzie.waifuviewer.domain.WaifuBestItemPng
import com.mackenzie.waifuviewer.usecases.best.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaifuBestViewModel @Inject constructor(
    getWaifuPngUseCase: GetWaifuPngUseCase,
    getWaifuGifUseCase: GetWaifuGifUseCase,
    private val requestWaifuBestUseCase: RequestWaifuBestUseCase,
    private val requestMoreBestUseCase: RequestMoreWaifuBestUseCase,
    private val clearWaifuBestUseCase: ClearWaifuBestUseCase
): ViewModel()  {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getWaifuPngUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) }}
                .collect{ waifusPng -> _state.update { UiState(waifusPng = waifusPng) } }
        }
    }

    init {
        viewModelScope.launch {
            getWaifuGifUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) }}
                .collect{ waifusGif -> _state.update { UiState(waifusGif = waifusGif) } }
        }
    }

    fun onBestReady(isGif: Boolean,tag: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val error = requestWaifuBestUseCase(isGif, tag)
            _state.update { it.copy(isLoading = false, error = error) }
        }
    }

    fun onRequestMore(isGif: Boolean, tag: String) {
        viewModelScope.launch {
            val error = requestMoreBestUseCase(isGif, tag)
            _state.update { it.copy(error = error) }
        }
    }

    fun onClearDatabase(isGif: Boolean) {
        viewModelScope.launch {
            val error = clearWaifuBestUseCase(isGif)
            _state.update { _state.value.copy(error = error) }
        }
    }

    data class UiState(
        val isLoading: Boolean? = null,
        val waifusPng: List<WaifuBestItemPng>? = null,
        val waifusGif: List<WaifuBestItemGif>? = null,
        val error: Error? = null
    )

}