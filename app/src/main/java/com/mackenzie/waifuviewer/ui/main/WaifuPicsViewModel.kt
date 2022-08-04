package com.mackenzie.waifuviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.data.toError
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.usecases.GetWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.RequestWaifuPicUseCase
import com.mackenzie.waifuviewer.ui.common.Scope
import com.mackenzie.waifuviewer.usecases.RequestMoreWaifuPicUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WaifuPicsViewModel(
    getWaifuPicUseCase: GetWaifuPicUseCase,
    private val requestWaifuPicUseCase: RequestWaifuPicUseCase,
    private val requestMorePicUseCase: RequestMoreWaifuPicUseCase
    ): ViewModel(), Scope by Scope.Impl() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()
    // private val _events = Channel<UiEvent> ()
    // val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            getWaifuPicUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) }}
                .collect{ WaifuPics -> _state.update { UiState(waifusSavedPics = WaifuPics) } }
        }
    }

    fun onPicsReady(isNsfw: Boolean, tag: String) {
            if (isNsfw) {
                waifusGetter("nsfw", tag)
            } else {
                waifusGetter("sfw", tag)
            }
    }

    fun onRequestMore(isNsfw: Boolean, tag: String) {
        viewModelScope.launch {
            val error: Error?
            if (isNsfw) {
                if (tag == "all") {
                    error = requestMorePicUseCase("nsfw", "waifu")
                    _state.update { it.copy(error = error) }
                } else {
                    error = requestMorePicUseCase("nsfw", tag)
                    _state.update { it.copy(error = error) }
                }
            } else {
                if (tag == "all") {
                    error = requestMorePicUseCase("sfw", "waifu")
                    _state.update { it.copy(error = error) }
                } else {
                    error = requestMorePicUseCase("sfw", tag)
                    _state.update { it.copy(error = error) }
                }
            }
        }
    }

    private fun waifusGetter(isNsfw: String, tag: String) {
        viewModelScope.launch {
            val error: Error?
            if (tag == "all") {
                error = requestWaifuPicUseCase(isNsfw, "waifu")
                _state.update { it.copy(error = error) }
            } else {
                error = requestWaifuPicUseCase(isNsfw, tag)
                _state.update { it.copy(error = error) }
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val waifusSavedPics: List<com.mackenzie.waifuviewer.domain.WaifuPicItem>? = null,
        val error: Error? = null
    )
}

@Suppress("UNCHECKED_CAST")
class WaifuPicsViewModelFactory(
    private val getWaifuPicUseCase: GetWaifuPicUseCase,
    private val requestWaifuPicUseCase: RequestWaifuPicUseCase,
    private val requestMorePicUseCase: RequestMoreWaifuPicUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WaifuPicsViewModel(getWaifuPicUseCase, requestWaifuPicUseCase, requestMorePicUseCase) as T
    }
}