package com.mackenzie.waifuviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.models.datasource.WaifusRepository
import com.mackenzie.waifuviewer.models.db.WaifuPicItem
import com.mackenzie.waifuviewer.ui.common.Scope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WaifuPicsViewModel(private val waifusRepository: WaifusRepository): ViewModel(), Scope by Scope.Impl() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()
    // private val _events = Channel<UiEvent> ()
    // val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            waifusRepository.savedWaifusPic.collect{ WaifuPics ->
                _state.value = UiState(waifusSavedPics = WaifuPics)
            }
        }
    }

    fun onPicsReady(isNsfw: Boolean, tag: String) {
            _state.value = UiState(isLoading = true)
            if (isNsfw) {
                waifusGetter("nsfw", tag)
            } else {
                waifusGetter("sfw", tag)
            }
    }

    fun onRequestMore(isNsfw: Boolean, tag: String) {
        viewModelScope.launch {
            if (isNsfw) {
                if (tag == "all") {
                    waifusRepository.requestNewWaifusPics("nsfw", "waifu")
                } else {
                    waifusRepository.requestNewWaifusPics("nsfw", tag)
                }
            } else {
                if (tag == "all") {
                    waifusRepository.requestNewWaifusPics("sfw", "waifu")
                } else {
                    waifusRepository.requestNewWaifusPics("sfw", tag)
                }
            }
        }
    }

    private fun waifusGetter(isNsfw: String, tag: String) {
        viewModelScope.launch {
            if (tag == "all") {
                waifusRepository.requestWaifusPics(isNsfw, "waifu")
            } else {
                waifusRepository.requestWaifusPics(isNsfw, tag)
            }
        }
    }


    data class UiState(
        val isLoading: Boolean = false,
        // val waifusIm: WaifuResult? = null,
        // val waifusPics: List<String>? = null,
        val waifusSavedPics: List<WaifuPicItem>? = null,
        // val waifusSavedIm: List<WaifuImItem>? = null,
        val isError: Boolean = false
    )


}

@Suppress("UNCHECKED_CAST")
class WaifuPicsViewModelFactory(private val waifusRepository: WaifusRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WaifuPicsViewModel(waifusRepository) as T
    }
}