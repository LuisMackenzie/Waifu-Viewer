package com.mackenzie.waifuviewer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.ui.common.Scope
import com.mackenzie.waifuviewer.usecases.RequestOnlyWaifuPicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectorPicViewModel @Inject constructor(
    private val requestOnlyPicWaifu: RequestOnlyWaifuPicUseCase
    ): ViewModel(), Scope by Scope.Impl() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private fun loadWaifu() {
        viewModelScope.launch {
            val waifu = requestOnlyPicWaifu()
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
        val waifu: WaifuPicItem? = null,
        val type: ServerType = ServerType.NORMAL,
        val error: Error? = null
    )
}
