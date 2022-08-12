package com.mackenzie.waifuviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.data.toError
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.usecases.GetWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.RequestWaifuPicUseCase
import com.mackenzie.waifuviewer.ui.common.Scope
import com.mackenzie.waifuviewer.ui.main.WaifuImViewModel
import com.mackenzie.waifuviewer.usecases.RequestMoreWaifuPicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaifuPicsViewModel @Inject constructor(
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
                .collect{ waifusPics -> _state.update { UiState(isLoading = false, waifus = waifusPics) } }
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
        if (isNsfw) {
            moreWaifusGetter("nsfw", tag)
        } else {
            moreWaifusGetter("sfw", tag)
        }
    }

    fun onClearPicsDatabase(favoriteItem: FavoriteItem) {
        viewModelScope.launch {

        }
    }

    private fun waifusGetter(isNsfw: String, tag: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            if (tag == "all") {
                val error = requestWaifuPicUseCase(isNsfw, "waifu")
                _state.update { _state.value.copy(error = error) }
            } else {
                val error = requestWaifuPicUseCase(isNsfw, tag)
                _state.update { _state.value.copy(error = error) }
            }
        }
    }

    private fun moreWaifusGetter(isNsfw: String, tag: String) {
        viewModelScope.launch {
            // _state.value = _state.value.copy(isLoading = true)
            if (tag == "all") {
                val error = requestMorePicUseCase(isNsfw, "waifu")
                _state.update { _state.value.copy(error = error) }
            } else {
                val error = requestMorePicUseCase(isNsfw, tag)
                _state.update { _state.value.copy(error = error) }
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val waifus: List<WaifuPicItem>? = null,
        val error: Error? = null
    )
}

