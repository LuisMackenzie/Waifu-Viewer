package com.mackenzie.waifuviewer.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.data.toError
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import com.mackenzie.waifuviewer.usecases.im.ClearWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.im.GetWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.im.RequestMoreWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.im.RequestWaifuImUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaifuImViewModel @Inject constructor(
    getWaifuImUseCase: GetWaifuImUseCase,
    private val requestWaifuImUseCase: RequestWaifuImUseCase,
    private val requestMoreImUseCase: RequestMoreWaifuImUseCase,
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

            _state.update { it.copy(isLoading = true) }
            val error = requestWaifuImUseCase(isNsfw, tag, isGif,  orientation)
            _state.update { it.copy(isLoading = false, error = error) }
        }
    }

    fun onRequestMore(isNsfw: Boolean, isGif: Boolean, tag: String, orientation: Boolean) {
        viewModelScope.launch {
            val error = requestMoreImUseCase(isNsfw, tag, isGif,  orientation)
            _state.update { it.copy(error = error) }
        }
    }

    data class UiState(
        val isLoading: Boolean? = null,
        val waifus: List<WaifuImItem>? = null,
        val error: Error? = null
    )
}