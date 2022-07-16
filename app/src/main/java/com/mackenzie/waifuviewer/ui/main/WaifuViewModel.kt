package com.mackenzie.waifuviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.data.WaifuManager
import com.mackenzie.waifuviewer.data.WaifusRepository
import com.mackenzie.waifuviewer.models.Waifu
import com.mackenzie.waifuviewer.models.WaifuResult
import com.mackenzie.waifuviewer.models.db.WaifuImItem
import com.mackenzie.waifuviewer.models.db.WaifuPicItem
import com.mackenzie.waifuviewer.ui.common.Scope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WaifuViewModel(private val waifusRepository: WaifusRepository): ViewModel(), Scope by Scope.Impl() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()
    // private val _events = Channel<UiEvent> ()
    // val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {

            // _state.value = UiState(isLoading = true)
            waifusRepository.savedWaifusPic.collect{ WaifuPics ->
                _state.value = UiState(waifusSavedPics = WaifuPics)
            }
            waifusRepository.savedWaifusIm.collect{ WaifuIm ->
                _state.value = UiState(waifusSavedIm = WaifuIm)
            }
        }
    }

    fun onCustomWaifusReady(isNsfw: Boolean, isGif: Boolean, tag: String, orientation: Boolean) {

        viewModelScope.launch {
            _state.value = UiState(isLoading = true)
            val waifus: WaifuResult
            if (tag == "all") {
                waifus = waifusRepository.requestWaifusIm(isNsfw = isNsfw, tag = "waifu", isGif =  isGif, orientation = orientation)
                _state.value = UiState(waifusIm = waifus, isLoading = false)
            } else {
                waifus = waifusRepository.requestWaifusIm(isNsfw, tag, isGif,  orientation)
                _state.value = UiState(waifusIm = waifus, isLoading = false)
            }
            if (waifus.waifus.isEmpty()) {
                _state.value = UiState(isLoading = false, isError = true)
            }
        }
    }

    fun onCustomWaifusPicsReady(isNsfw: Boolean, tag: String) {
            _state.value = UiState(isLoading = true)
            if (isNsfw) {
                waifusGetter("nsfw", tag)
            } else {
                waifusGetter("sfw", tag)
            }
    }

    private fun waifusGetter(isNsfw: String, tag: String) {
        viewModelScope.launch {
            WaifuManager().getWaifuPics(isNsfw = isNsfw, tag = tag) { waifuResult ->
                if (waifuResult != null){
                    if (!waifuResult.isEmpty()) {
                        _state.value = UiState(waifusPics = waifuResult, isLoading = false)
                    } else {
                        _state.value = UiState(isLoading = false, isError = true)
                    }
                }
            }
        }
    }


    data class UiState(
        val isLoading: Boolean = false,
        val waifusIm: WaifuResult? = null,
        val waifusPics: List<String>? = null,
        val waifusSavedPics: List<WaifuPicItem>? = null,
        val waifusSavedIm: List<WaifuImItem>? = null,
        val isError: Boolean = false
    )


}

@Suppress("UNCHECKED_CAST")
class WaifuViewModelFactory(private val waifusRepository: WaifusRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WaifuViewModel(waifusRepository) as T
    }
}