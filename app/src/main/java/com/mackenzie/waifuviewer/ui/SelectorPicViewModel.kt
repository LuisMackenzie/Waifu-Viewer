package com.mackenzie.waifuviewer.ui

import androidx.lifecycle.*
import com.mackenzie.waifuviewer.WaifuPicsViewModel
import com.mackenzie.waifuviewer.data.server.WaifuPic
import com.mackenzie.waifuviewer.data.toError
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.usecases.RequestOnlyWaifuPicUseCase
import com.mackenzie.waifuviewer.ui.common.Scope
import com.mackenzie.waifuviewer.usecases.GetOnlyWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.GetWaifuPicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectorPicViewModel @Inject constructor(
    private val getOnlyWaifuPicUseCase: GetOnlyWaifuPicUseCase,
    private val requestOnlyPicWaifu: RequestOnlyWaifuPicUseCase): ViewModel(), Scope by Scope.Impl() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()
    // private val _events = Channel<UiEvent> ()
    // val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            /*getOnlyWaifuPicUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) }}
                .collect{ waifuPics -> _state.update { it.copy(waifu = waifuPics.first()) } }*/
        }
    }

    private fun loadWaifu() {
        viewModelScope.launch {
            val waifu = requestOnlyPicWaifu()
            _state.update { UiState(waifu = waifu) }
            /*waifu.fold(ifLeft = {
                _state.update { it.copy(error = it.error) }
            }, ifRight = {
                _state.update { it.copy(waifu = it.waifu) }
            })*/
        }
    }

    fun loadErrorOrWaifu() {
        viewModelScope.launch {
            loadWaifu()
            /*val error = requestOnlyPicWaifu()
            if (error == null) {
                loadWaifu()
            }
            _state.update { UiState(error = error) }*/
        }
    }

    data class UiState(
        val waifu: WaifuPicItem? = null,
        val error: Error? = null
    )
}

/*
@Suppress("UNCHECKED_CAST")
class SelectorPicViewModelFactory(private val getOnlyWaifuPicUseCase: GetOnlyWaifuPicUseCase, private val requestOnlyPicWaifu: RequestOnlyWaifuPicUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SelectorPicViewModel(getOnlyWaifuPicUseCase, requestOnlyPicWaifu) as T
    }
}*/
