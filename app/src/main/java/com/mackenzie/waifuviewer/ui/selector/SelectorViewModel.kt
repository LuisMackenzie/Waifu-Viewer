package com.mackenzie.waifuviewer.ui.selector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.WaifuBestItem
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import com.mackenzie.waifuviewer.domain.im.WaifuImTagList
import com.mackenzie.waifuviewer.usecases.best.ClearWaifuBestUseCase
import com.mackenzie.waifuviewer.usecases.best.RequestOnlyWaifuBestUseCase
import com.mackenzie.waifuviewer.usecases.im.ClearWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.im.GetWaifuImTagsUseCase
import com.mackenzie.waifuviewer.usecases.im.RequestOnlyWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.im.RequestWaifuImTagsUseCase
import com.mackenzie.waifuviewer.usecases.pics.ClearWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.pics.RequestOnlyWaifuPicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectorViewModel @Inject constructor(
    private val getWaifuImTagsUseCase: GetWaifuImTagsUseCase,
    private val requestOnlyImWaifu: RequestOnlyWaifuImUseCase,
    private val requestOnlyPicWaifu: RequestOnlyWaifuPicUseCase,
    private val requestOnlyWaifuBestUseCase: RequestOnlyWaifuBestUseCase,
    private val requestWaifuImTagsUseCase: RequestWaifuImTagsUseCase,
    private val clearWaifuImUseCase: ClearWaifuImUseCase,
    private val clearWaifuPicUseCase: ClearWaifuPicUseCase,
    private val clearWaifuBestUseCase: ClearWaifuBestUseCase
): ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private fun loadImWaifu(orientation: Boolean) {
        viewModelScope.launch {
            val waifu = requestOnlyImWaifu(orientation)
            if (waifu != null) {
                _state.update { it.copy(waifuIm = waifu) }
            } else {
                _state.update { UiState(error = Error.Connectivity) }
            }
        }
    }

    private fun loadPicWaifu() {
        viewModelScope.launch {
            val waifu = requestOnlyPicWaifu()
            if (waifu != null) {
                _state.update { it.copy(waifuPic = waifu) }
            } else {
                _state.update { UiState(error = Error.Connectivity) }
            }
        }
    }

    private fun loadNekoWaifu() {
        viewModelScope.launch {
            val waifu = requestOnlyWaifuBestUseCase()
            if (waifu != null) {
                _state.update { it.copy(waifuNeko = waifu) }
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

    fun loadErrorOrWaifu(orientation: Boolean = false, serverType: ServerType) {
        when (serverType) {
            ServerType.NORMAL -> loadImWaifu(orientation)
            ServerType.ENHANCED -> loadPicWaifu()
            ServerType.NEKOS -> loadNekoWaifu()
            else -> {}
        }
    }

    fun onClearImDatabase() {
        viewModelScope.launch {
            val error = clearWaifuImUseCase()
            _state.update { it.copy(error = error) }
        }
    }

    fun onClearPicsDatabase() {
        viewModelScope.launch {
            val error = clearWaifuPicUseCase()
            _state.update { _state.value.copy(error = error) }
        }
    }

    fun onClearBestDatabase() {
        viewModelScope.launch {
            val error = clearWaifuBestUseCase()
            _state.update { _state.value.copy(error = error) }
        }
    }

    data class UiState(
        val waifuIm: WaifuImItem? = null,
        val waifuPic: WaifuPicItem? = null,
        val waifuNeko: WaifuBestItem? = null,
        val tags: WaifuImTagList? = null,
        val error: Error? = null
    )

}