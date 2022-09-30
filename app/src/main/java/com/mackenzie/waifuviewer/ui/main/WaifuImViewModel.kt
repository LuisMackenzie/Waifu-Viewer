package com.mackenzie.waifuviewer.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.data.toError
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaifuImViewModel @Inject constructor(
    getWaifuImUseCase: GetWaifuImUseCase,
    private val requestWaifuImUseCase: RequestWaifuImUseCase,
    private val requestMoreImUseCase: RequestMoreWaifuImUseCase,
    private val clearWaifuImUseCase: ClearWaifuImUseCase
    ): ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getWaifuImUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) }}
                .collect{ waifusIm -> _state.update { UiState( waifus = waifusIm) } }
        }
    }

    fun onImReady(isNsfw: Boolean, isGif: Boolean, tag: String, orientation: Boolean) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val error : Error?
            if (tag == "all") {
                if (isNsfw) {
                    error = requestWaifuImUseCase(isNsfw,"ecchi",isGif,orientation)
                } else {
                    error = requestWaifuImUseCase(isNsfw,"waifu",isGif,orientation)
                }
            } else {
                error = requestWaifuImUseCase(isNsfw, tag, isGif,  orientation)
            }
            _state.value = _state.value.copy(isLoading = false, error = error)
        }
    }

    fun onRequestMore(isNsfw: Boolean, isGif: Boolean, tag: String, orientation: Boolean) {
        viewModelScope.launch {
            if (tag == "all") {
                if (isNsfw) {
                    val error = requestMoreImUseCase(isNsfw,"ecchi",isGif,orientation)
                    _state.value = _state.value.copy(error = error)

                } else {
                    val error = requestMoreImUseCase(isNsfw,"waifu",isGif,orientation)
                    _state.value = _state.value.copy(error = error)

                }
            } else {
                val error = requestMoreImUseCase(isNsfw, tag, isGif,  orientation)
                _state.value = _state.value.copy(error = error)
            }
        }
    }

    fun onClearImDatabase() {
        viewModelScope.launch {
            val error = clearWaifuImUseCase()
            _state.value = _state.value.copy(error = error)
        }
    }

    data class UiState(
        val isLoading: Boolean? = null,
        val waifus: List<WaifuImItem>? = null,
        val error: Error? = null
    )
}