package com.mackenzie.waifuviewer.ui.main

import androidx.lifecycle.*
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import com.mackenzie.waifuviewer.domain.im.WaifuImTagList
import com.mackenzie.waifuviewer.usecases.im.GetWaifuImTagsUseCase
import com.mackenzie.waifuviewer.usecases.im.RequestOnlyWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.im.RequestWaifuImTagsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectorImViewModel @Inject constructor(
    private val getWaifuImTagsUseCase: GetWaifuImTagsUseCase,
    private val requestOnlyImWaifu: RequestOnlyWaifuImUseCase,
    private val requestWaifuImTagsUseCase: RequestWaifuImTagsUseCase
    ): ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private fun loadWaifu(orientation: Boolean) {
        viewModelScope.launch {
            val waifu = requestOnlyImWaifu(orientation)
            if (waifu != null) {
                _state.update { it.copy(waifu = waifu) }
            } else {
                _state.update { UiState(error = Error.Connectivity) }
            }

        }
    }

    fun requestTags() {
        viewModelScope.launch {
            val error = requestWaifuImTagsUseCase()
            loadTags()
            if (error != null) _state.update { it.copy(error = error) }
        }
    }

    private fun loadTags() {
        viewModelScope.launch {
            getWaifuImTagsUseCase()
                .catch { cause -> }
                .collect{ tags -> _state.update { it.copy(tags = tags) } }

        }
    }

    fun onChangeType(type: ServerType) {
            _state.update { it.copy(type = type) }
    }

    fun loadErrorOrWaifu(orientation: Boolean = false) {
            loadWaifu(orientation)
    }

    data class UiState(
        val waifu: WaifuImItem? = null,
        val type: ServerType = ServerType.NORMAL,
        val tags: WaifuImTagList? = null,
        val error: Error? = null
    )

}
