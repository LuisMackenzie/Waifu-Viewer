package com.mackenzie.waifuviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.data.toError
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.usecases.pics.GetWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.pics.RequestWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.pics.ClearWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.pics.RequestMoreWaifuPicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaifuPicsViewModel @Inject constructor(
    getWaifuPicUseCase: GetWaifuPicUseCase,
    private val requestWaifuPicUseCase: RequestWaifuPicUseCase,
    private val requestMorePicUseCase: RequestMoreWaifuPicUseCase,
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

    private fun waifusGetter(isNsfw: String, tag: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val error = requestWaifuPicUseCase(isNsfw, tag)
            _state.update { it.copy(isLoading = false, error = error) }
        }
    }

    private fun moreWaifusGetter(isNsfw: String, tag: String) {
        viewModelScope.launch {
            val error = requestMorePicUseCase(isNsfw, tag)
            _state.update { it.copy(error = error) }
        }
    }

    data class UiState(
        val isLoading: Boolean? = null,
        val waifus: List<WaifuPicItem>? = null,
        val error: Error? = null
    )
}

