package com.mackenzie.waifuviewer.ui.main

import androidx.lifecycle.*
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.usecases.RequestOnlyWaifuImUseCase
import com.mackenzie.waifuviewer.ui.common.Scope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectorImViewModel @Inject constructor(
    private val requestOnlyImWaifu: RequestOnlyWaifuImUseCase
    ): ViewModel(), Scope by Scope.Impl() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private fun loadWaifu() {
        viewModelScope.launch {
            /*getWaifuImUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) }}
                .collect{ waifuIm -> _state.update { UiState(waifu = waifuIm.first()) } }*/
            val waifu = requestOnlyImWaifu()
            if (waifu != null) {
                _state.update { it.copy(waifu = waifu) }
            } else {
                _state.update { UiState(error = Error.Connectivity) }
            }

        }
    }

    fun onChangeType(type: ServerType) {
            _state.update { it.copy(type = type) }
    }

    fun loadErrorOrWaifu() {
            loadWaifu()
    }

    data class UiState(
        val waifu: WaifuImItem? = null,
        val type: ServerType = ServerType.NORMAL,
        val error: Error? = null
    )

}
