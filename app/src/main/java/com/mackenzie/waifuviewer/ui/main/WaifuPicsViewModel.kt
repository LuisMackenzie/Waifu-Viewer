package com.mackenzie.waifuviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.models.Error
import com.mackenzie.waifuviewer.models.datasource.WaifusPicRepository
import com.mackenzie.waifuviewer.models.db.WaifuPicItem
import com.mackenzie.waifuviewer.models.toError
import com.mackenzie.waifuviewer.ui.common.Scope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WaifuPicsViewModel(private val waifusPicRepository: WaifusPicRepository): ViewModel(), Scope by Scope.Impl() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()
    // private val _events = Channel<UiEvent> ()
    // val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            waifusPicRepository.savedWaifusPic
                .catch { cause -> _state.update { it.copy(error = cause.toError()) }}
                .collect{ WaifuPics -> _state.update { UiState(waifusSavedPics = WaifuPics) }
            }
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
                    error = waifusPicRepository.requestNewWaifusPics("nsfw", "waifu")
                    _state.update { it.copy(error = error) }
                } else {
                    error = waifusPicRepository.requestNewWaifusPics("nsfw", tag)
                    _state.update { it.copy(error = error) }
                }
            } else {
                if (tag == "all") {
                    error = waifusPicRepository.requestNewWaifusPics("sfw", "waifu")
                    _state.update { it.copy(error = error) }
                } else {
                    error = waifusPicRepository.requestNewWaifusPics("sfw", tag)
                    _state.update { it.copy(error = error) }
                }
            }
        }
    }

    private fun waifusGetter(isNsfw: String, tag: String) {
        viewModelScope.launch {
            val error: Error?
            if (tag == "all") {
                error = waifusPicRepository.requestWaifusPics(isNsfw, "waifu")
                _state.update { it.copy(error = error) }
            } else {
                error = waifusPicRepository.requestWaifusPics(isNsfw, tag)
                _state.update { it.copy(error = error) }
            }
        }
    }


    data class UiState(
        val isLoading: Boolean = false,
        val waifusSavedPics: List<WaifuPicItem>? = null,
        val error: Error? = null
    )


}

@Suppress("UNCHECKED_CAST")
class WaifuPicsViewModelFactory(private val waifusImRepository: WaifusPicRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WaifuPicsViewModel(waifusImRepository) as T
    }
}