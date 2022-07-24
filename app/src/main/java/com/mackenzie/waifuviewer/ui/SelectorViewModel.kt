package com.mackenzie.waifuviewer.ui

import androidx.lifecycle.*
import com.mackenzie.waifuviewer.models.Error
import com.mackenzie.waifuviewer.models.datasource.WaifusRepository
import com.mackenzie.waifuviewer.models.WaifuPic
import com.mackenzie.waifuviewer.models.WaifuResult
import com.mackenzie.waifuviewer.ui.common.Scope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectorViewModel (private val waifusRepository: WaifusRepository): ViewModel(), Scope by Scope.Impl() {

    private val waifuIdDefault: String = "fde0c21c420d9e0c"
    private val waifuIdNight: String = "5872ca0583da7dea"
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()
    // private val _events = Channel<UiEvent> ()
    // val events = _events.receiveAsFlow()

    init {
        loadWaifu()
    }

    fun loadWaifu() {
        viewModelScope.launch {
            val waifu = waifusRepository.requestOnlyWaifuPic()
            _state.update { UiState(waifu = waifu) }
        }
    }

    fun loadErrorOrWaifu() {
        viewModelScope.launch {
            // val waifu = waifusRepository.getWaifuInfo(waifuIdDefault)
            val error = waifusRepository.requestOnlyWaifuPicFix()
            _state.update { UiState(error = error) }
        }
    }

    private fun loadRandomWaifu() {
        viewModelScope.launch {
            // _state.value = UiState(isLoading = true)
            // val waifuResult = waifusRepository.getWaifuOnly()
            // _state.value = UiState(waifu = waifuResult.waifus[0], isLoading = false)
        }
    }

    private fun loadAdultWaifu() {
        viewModelScope.launch {
            // _state.value = UiState(isLoading = true)
            // val waifuResult = waifusRepository.getWaifuOnlyNsfw()
            // _state.value = UiState(waifu = waifuResult.waifus[0], isLoading = false)
        }
    }

    /*fun onUpdateWaifu() {
        loadWaifu()
    }*/

    data class UiState(
        val waifu: WaifuPic? = null,
        val error: Error? = null
    )

    /*sealed interface UiEvent {
        data class NavigateTo(val waifuResult: WaifuResult) : UiEvent
    }*/

}

@Suppress("UNCHECKED_CAST")
class SelectorViewModelFactory(private val waifusRepository: WaifusRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SelectorViewModel(waifusRepository) as T
    }
}