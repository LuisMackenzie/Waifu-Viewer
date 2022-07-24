package com.mackenzie.waifuviewer.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.models.Error
import com.mackenzie.waifuviewer.models.datasource.WaifusImRepository
import com.mackenzie.waifuviewer.models.db.WaifuImItem
import com.mackenzie.waifuviewer.models.toError
import com.mackenzie.waifuviewer.ui.common.Scope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WaifuImViewModel(private val waifusImRepository: WaifusImRepository): ViewModel(), Scope by Scope.Impl() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            waifusImRepository.savedWaifusIm
                .catch { cause -> _state.update { it.copy(error = cause.toError()) }}
                .collect{ WaifuIm -> _state.update { UiState(waifusSavedIm = WaifuIm) }
            }
        }
    }

    fun onImReady(isNsfw: Boolean, isGif: Boolean, tag: String, orientation: Boolean) {

        viewModelScope.launch {
            val error: Error?
            if (tag == "all") {
                if (isNsfw) {
                    error = waifusImRepository.requestWaifusIm(isNsfw,"ecchi",isGif,orientation)
                    _state.update { it.copy(error = error) }
                } else {
                    error = waifusImRepository.requestWaifusIm(isNsfw,"waifu",isGif,orientation)
                    _state.update { it.copy(error = error) }
                }
            } else {
                error = waifusImRepository.requestWaifusIm(isNsfw, tag, isGif,  orientation)
                _state.update { it.copy(error = error) }
            }
        }
    }

    fun onRequestMore(isNsfw: Boolean, isGif: Boolean, tag: String, orientation: Boolean) {
        viewModelScope.launch {
            val error: Error?
            if (tag == "all") {
                if (isNsfw) {
                    error =waifusImRepository.requestNewWaifusIm(isNsfw,"ecchi",isGif,orientation)
                    _state.update { it.copy(error = error) }
                } else {
                    error = waifusImRepository.requestNewWaifusIm(isNsfw,"waifu",isGif,orientation)
                    _state.update { it.copy(error = error) }
                }
            } else {
                error = waifusImRepository.requestNewWaifusIm(isNsfw, tag, isGif,  orientation)
                _state.update { it.copy(error = error) }
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
class WaifuImViewModelFactory(private val waifusImRepository: WaifusImRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WaifuImViewModel(waifusImRepository) as T
    }
}