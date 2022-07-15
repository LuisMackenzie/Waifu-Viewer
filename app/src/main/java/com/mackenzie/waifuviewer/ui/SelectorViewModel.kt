package com.mackenzie.waifuviewer.ui

import androidx.lifecycle.*
import com.mackenzie.waifuviewer.data.WaifusRepository
import com.mackenzie.waifuviewer.models.WaifuPic
import com.mackenzie.waifuviewer.models.WaifuResult
import com.mackenzie.waifuviewer.ui.common.Scope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private fun loadWaifu() {
        viewModelScope.launch {
            _state.value = UiState(isLoading = true)
            // val waifu = waifusRepository.getWaifuInfo(waifuIdDefault)
            val waifu = waifusRepository.requestOnlyWaifuPic()

            _state.value = UiState(waifu = waifu, isLoading = false)
        }
    }

    private fun loadRandomWaifu() {
        viewModelScope.launch {
            _state.value = UiState(isLoading = true)
            // val waifuResult = waifusRepository.getWaifuOnly()
            // _state.value = UiState(waifu = waifuResult.waifus[0], isLoading = false)
        }
    }

    private fun loadAdultWaifu() {
        viewModelScope.launch {
            _state.value = UiState(isLoading = true)
            // val waifuResult = waifusRepository.getWaifuOnlyNsfw()
            // _state.value = UiState(waifu = waifuResult.waifus[0], isLoading = false)
        }
    }

    fun onUpdateWaifu() {
        loadWaifu()
    }

    fun onUiReady() {

    }

    /*fun onButtonClicked(waifuResult: WaifuResult) {
        viewModelScope.launch {
            _state.value = UiState(waifus = waifusRepository.getRandomWaifus(), isLoading = false)
        }
    }

    fun onLocationPermissionChecked() {
        _state.value = _state.value.copy(requestLocationPermission = false)
    }

    fun onNavigationDone() {
        _state.value = _state.value.copy(navigateTo = null)
    }*/


    /*override fun onCleared() {
        destroyScope()
        super.onCleared()
    }*/

    data class UiState(
        val isLoading: Boolean = false,
        val waifus: WaifuResult? = null,
        val waifu: WaifuPic? = null,
        // val requestLocationPermission: Boolean = true,
        // val navigateTo: WaifuResult? = null
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