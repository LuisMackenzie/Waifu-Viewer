package com.mackenzie.waifuviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.data.toError
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.usecases.GetWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.RequestWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.ClearWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.RequestMoreWaifuPicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaifuPicsViewModel @Inject constructor(
    getWaifuPicUseCase: GetWaifuPicUseCase,
    private val requestWaifuPicUseCase: RequestWaifuPicUseCase,
    private val requestMorePicUseCase: RequestMoreWaifuPicUseCase,
    private val clearWaifuPicUseCase: ClearWaifuPicUseCase
    ): ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getWaifuPicUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) }}
                .collect{ waifusPics -> _state.update { UiState( waifus = waifusPics) } }
        }
    }

    fun onPicsReady(isNsfw: Boolean, tag: String) {
            if (isNsfw) {
                waifusGetter("nsfw", tag)
            } else {
                waifusGetter("sfw", tag)
            }
    }

    fun onRequestMore(isNsfw: Boolean, tag: String) {
        if (isNsfw) {
            moreWaifusGetter("nsfw", tag)
        } else {
            moreWaifusGetter("sfw", tag)
        }
    }

    fun onClearPicsDatabase() {
        viewModelScope.launch {
            val error = clearWaifuPicUseCase()
            _state.update { _state.value.copy(error = error) }
        }
    }

    private fun waifusGetter(isNsfw: String, tag: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val error: Error?
            if (tag == "all") {
                error = requestWaifuPicUseCase(isNsfw, "waifu")
            } else {
                error = requestWaifuPicUseCase(isNsfw, tag)
            }
            _state.update { it.copy(isLoading = false, error = error) }
        }
    }

    private fun moreWaifusGetter(isNsfw: String, tag: String) {
        viewModelScope.launch {
            if (tag == "all") {
                val error = requestMorePicUseCase(isNsfw, "waifu")
                _state.update { it.copy(error = error) }
            } else {
                val error = requestMorePicUseCase(isNsfw, tag)
                _state.update { it.copy(error = error) }
            }
        }
    }

    data class UiState(
        val isLoading: Boolean? = null,
        val waifus: List<WaifuPicItem>? = null,
        val error: Error? = null
    )
}

