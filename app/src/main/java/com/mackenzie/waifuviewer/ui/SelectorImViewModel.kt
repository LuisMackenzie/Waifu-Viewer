package com.mackenzie.waifuviewer.ui.main

import androidx.lifecycle.*
import com.mackenzie.waifuviewer.data.server.Waifu
import com.mackenzie.waifuviewer.data.toError
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.usecases.RequestOnlyWaifuImUseCase
import com.mackenzie.waifuviewer.ui.common.Scope
import com.mackenzie.waifuviewer.usecases.GetWaifuImUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SelectorImViewModel(
    private val getWaifuImUseCase: GetWaifuImUseCase,
    private val requestOnlyImWaifu: RequestOnlyWaifuImUseCase): ViewModel(), Scope by Scope.Impl() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private fun loadWaifu() {
        viewModelScope.launch {
            /*getWaifuImUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) }}
                .collect{ waifuIm -> _state.update { UiState(waifu = waifuIm.first()) } }*/
            val waifu = requestOnlyImWaifu()
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
        }
    }

    data class UiState(
        val waifu: WaifuImItem? = null,
        val error: Error? = null
    )

}

@Suppress("UNCHECKED_CAST")
class SelectorImViewModelFactory(
    private val getWaifuImUseCase: GetWaifuImUseCase,
    private val requestOnlyImWaifu: RequestOnlyWaifuImUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SelectorImViewModel(getWaifuImUseCase, requestOnlyImWaifu) as T
    }
}