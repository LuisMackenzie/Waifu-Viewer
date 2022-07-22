package com.mackenzie.waifuviewer.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.models.datasource.WaifusRepository
import com.mackenzie.waifuviewer.models.db.WaifuImItem
import com.mackenzie.waifuviewer.ui.common.Scope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WaifuImViewModel(private val waifusRepository: WaifusRepository): ViewModel(), Scope by Scope.Impl() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            waifusRepository.savedWaifusIm.collect{ WaifuIm ->
                _state.value = UiState(waifusSavedIm = WaifuIm)
            }
        }
    }

    fun onImReady(isNsfw: Boolean, isGif: Boolean, tag: String, orientation: Boolean) {

        viewModelScope.launch {
            // _state.value = UiState(isLoading = true)
            // val waifus: WaifuResult
            if (tag == "all") {
                if (isNsfw) {
                    waifusRepository.requestWaifusIm(isNsfw,"ecchi",isGif,orientation)
                } else {
                    waifusRepository.requestWaifusIm(isNsfw,"waifu",isGif,orientation)
                }
                // waifus = waifusRepository.requestWaifusIm(isNsfw = isNsfw, tag = "waifu", isGif =  isGif, orientation = orientation)
                // _state.value = UiState(isLoading = false)
            } else {
                waifusRepository.requestWaifusIm(isNsfw, tag, isGif,  orientation)
                // waifus = waifusRepository.requestWaifusIm(isNsfw, tag, isGif,  orientation)
                // _state.value = UiState(isLoading = false)
            }
            /*if (waifus.waifus.isEmpty()) {
                _state.value = UiState(isLoading = false, isError = true)
            }*/
        }
    }

    fun onRequestMore(isNsfw: Boolean, isGif: Boolean, tag: String, orientation: Boolean) {
        viewModelScope.launch {
            if (tag == "all") {
                if (isNsfw) {
                    waifusRepository.requestNewWaifusIm(isNsfw,"ecchi",isGif,orientation)
                } else {
                    waifusRepository.requestNewWaifusIm(isNsfw,"waifu",isGif,orientation)
                }
            } else {
                waifusRepository.requestNewWaifusIm(isNsfw, tag, isGif,  orientation)
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val waifusSavedIm: List<WaifuImItem>? = null,
        val isError: Boolean = false
    )

}

@Suppress("UNCHECKED_CAST")
class WaifuImViewModelFactory(private val waifusRepository: WaifusRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WaifuImViewModel(waifusRepository) as T
    }
}