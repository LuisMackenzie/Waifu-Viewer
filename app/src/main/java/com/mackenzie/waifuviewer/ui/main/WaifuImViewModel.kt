package com.mackenzie.waifuviewer.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.data.toError
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.usecases.GetWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.RequestWaifuImUseCase
import com.mackenzie.waifuviewer.ui.common.Scope
import com.mackenzie.waifuviewer.usecases.RequestMoreWaifuImUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WaifuImViewModel(
    getWaifuImUseCase: GetWaifuImUseCase,
    private val requestWaifuImUseCase: RequestWaifuImUseCase,
    private val requestMoreImUseCase: RequestMoreWaifuImUseCase
    ): ViewModel(), Scope by Scope.Impl() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getWaifuImUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) }}
                .collect{ WaifusIm -> _state.update { UiState(waifusSavedIm = WaifusIm) } }
        }
    }

    fun onImReady(isNsfw: Boolean, isGif: Boolean, tag: String, orientation: Boolean) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val error: Error?
            if (tag == "all") {
                if (isNsfw) {
                    error = requestWaifuImUseCase(isNsfw,"ecchi",isGif,orientation)
                    // _state.update { it.copy(error = error) }
                    _state.update { _state.value.copy(isLoading = false, error = error) }
                } else {
                    error = requestWaifuImUseCase(isNsfw,"waifu",isGif,orientation)
                    // _state.update { it.copy(error = error) }
                    _state.update { _state.value.copy(isLoading = false, error = error) }
                }
            } else {
                error = requestWaifuImUseCase(isNsfw, tag, isGif,  orientation)
                _state.update { _state.value.copy(isLoading = false, error = error) }
            }
        }
    }

    fun onRequestMore(isNsfw: Boolean, isGif: Boolean, tag: String, orientation: Boolean) {
        viewModelScope.launch {
            val error: Error?
            if (tag == "all") {
                if (isNsfw) {
                    error = requestMoreImUseCase(isNsfw,"ecchi",isGif,orientation)
                    _state.update { _state.value.copy(isLoading = false, error = error) }
                } else {
                    error = requestMoreImUseCase(isNsfw,"waifu",isGif,orientation)
                    _state.update { _state.value.copy(isLoading = false, error = error) }
                }
            } else {
                error = requestMoreImUseCase(isNsfw, tag, isGif,  orientation)
                _state.update { _state.value.copy(isLoading = false, error = error) }
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val waifusSavedIm: List<WaifuImItem>? = null,
        val error: Error? = null
    )
}

@Suppress("UNCHECKED_CAST")
class WaifuImViewModelFactory(
    private val getWaifuImUseCase: GetWaifuImUseCase,
    private val requestWaifuImUseCase: RequestWaifuImUseCase,
    private val requestMoreImUseCase: RequestMoreWaifuImUseCase
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WaifuImViewModel(getWaifuImUseCase, requestWaifuImUseCase, requestMoreImUseCase) as T
    }
}