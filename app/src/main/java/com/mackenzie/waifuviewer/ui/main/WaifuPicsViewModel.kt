package com.mackenzie.waifuviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.data.toError
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.usecases.GetWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.RequestWaifuPicUseCase
import com.mackenzie.waifuviewer.ui.common.Scope
import com.mackenzie.waifuviewer.ui.main.WaifuImViewModel
import com.mackenzie.waifuviewer.usecases.RequestMoreWaifuPicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaifuPicsViewModel @Inject constructor(
    getWaifuPicUseCase: GetWaifuPicUseCase,
    private val requestWaifuPicUseCase: RequestWaifuPicUseCase,
    private val requestMorePicUseCase: RequestMoreWaifuPicUseCase
    ): ViewModel(), Scope by Scope.Impl() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()
    // private val _events = Channel<UiEvent> ()
    // val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            getWaifuPicUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) }}
                .collect{ waifusPics -> _state.update { UiState(waifus = waifusPics) } }
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

    /*fun onRequestMore(isNsfw: Boolean, tag: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            if (isNsfw) {
                if (tag == "all") {
                    val error = requestMorePicUseCase("nsfw", "waifu")
                    error.fold(
                        ifLeft = { cause -> _state.update{ it.copy(error = cause) } },
                        ifRight = { waifusPics -> _state.update{ it.copy(isLoading = false, waifus = waifusPics) } })
                } else {
                    val error = requestMorePicUseCase("nsfw", tag)
                    error.fold(
                        ifLeft = { cause -> _state.update{ it.copy(error = cause) } },
                        ifRight = { waifusPics -> _state.update{ it.copy(isLoading = false, waifus = waifusPics) } })
                }
            } else {
                if (tag == "all") {
                    val error = requestMorePicUseCase("sfw", "waifu")
                    error.fold(
                        ifLeft = { cause -> _state.update{ it.copy(error = cause) } },
                        ifRight = { waifusPics -> _state.update{ it.copy(isLoading = false, waifus = waifusPics) } })
                } else {
                    val error = requestMorePicUseCase("sfw", tag)
                    error.fold(
                        ifLeft = { cause -> _state.update{ it.copy(error = cause) } },
                        ifRight = { waifusPics -> _state.update{ it.copy(isLoading = false, waifus = waifusPics) } })
                }
            }
        }
    }*/

    private fun moreWaifusGetter(isNsfw: String, tag: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            if (tag == "all") {
                val error = requestMorePicUseCase(isNsfw, "waifu")
                error.fold(
                    ifLeft = { cause -> _state.update{ it.copy(error = cause) } },
                    ifRight = { waifusPics -> _state.update{ it.copy(isLoading = false, waifus = waifusPics) } })
            } else {
                val error = requestMorePicUseCase(isNsfw, tag)
                error.fold(
                    ifLeft = { cause -> _state.update{ it.copy(error = cause) } },
                    ifRight = { waifusPics -> _state.update{ it.copy(isLoading = false, waifus = waifusPics) } })
            }
        }
    }

    private fun waifusGetter(isNsfw: String, tag: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            if (tag == "all") {
                val error = requestWaifuPicUseCase(isNsfw, "waifu")
                _state.update { _state.value.copy(isLoading = false, error = error) }
            } else {
                val error = requestWaifuPicUseCase(isNsfw, tag)
                _state.update { _state.value.copy(isLoading = false, error = error) }
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val waifus: List<WaifuPicItem>? = null,
        val error: Error? = null
    )
}

/*
@Suppress("UNCHECKED_CAST")
class WaifuPicsViewModelFactory(
    private val getWaifuPicUseCase: GetWaifuPicUseCase,
    private val requestWaifuPicUseCase: RequestWaifuPicUseCase,
    private val requestMorePicUseCase: RequestMoreWaifuPicUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WaifuPicsViewModel(getWaifuPicUseCase, requestWaifuPicUseCase, requestMorePicUseCase) as T
    }
}*/
