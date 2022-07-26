package com.mackenzie.waifuviewer.ui

import androidx.lifecycle.*
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.framework.server.WaifuPic
import com.mackenzie.waifuviewer.usecases.RequestOnlyWaifuPicUseCase
import com.mackenzie.waifuviewer.ui.common.Scope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectorViewModel (private val requestOnlyWaifu: RequestOnlyWaifuPicUseCase): ViewModel(), Scope by Scope.Impl() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()
    // private val _events = Channel<UiEvent> ()
    // val events = _events.receiveAsFlow()

    fun loadWaifu() {
        viewModelScope.launch {
            val waifu = requestOnlyWaifu.get()
            _state.update { UiState(waifu = waifu) }
        }
    }

    fun loadErrorOrWaifu() {
        viewModelScope.launch {
            // val waifu = waifusRepository.getWaifuInfo(waifuIdDefault)
            val error = requestOnlyWaifu()
            if (error == null) {
                val waifu = requestOnlyWaifu.get()
                _state.update { UiState(waifu = waifu) }
            }
            _state.update { UiState(error = error) }
        }
    }

    data class UiState(
        val waifu: WaifuPic? = null,
        val error: Error? = null
    )
}

@Suppress("UNCHECKED_CAST")
class SelectorViewModelFactory(private val requestOnlyWaifu: RequestOnlyWaifuPicUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SelectorViewModel(requestOnlyWaifu) as T
    }
}