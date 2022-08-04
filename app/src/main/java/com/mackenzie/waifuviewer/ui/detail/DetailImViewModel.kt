package com.mackenzie.waifuviewer.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.usecases.FindWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.SwitchImFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailImViewModel (
    waifuId: Int,
    findWaifuImUseCase: FindWaifuImUseCase,
    private val switchImFavoriteUseCase : SwitchImFavoriteUseCase
    ): ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            findWaifuImUseCase(waifuId).collect {
                _state.value = UiState(waifuIm = it)
            }
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            _state.value.waifuIm?.let { switchImFavoriteUseCase(it) }
        }
    }

    data class UiState(val waifuIm: com.mackenzie.waifuviewer.domain.WaifuImItem? = null)

}

@Suppress("UNCHECKED_CAST")
class DetailImViewModelFactory(
    private val waifuId: Int,
    private val findWaifuImUseCase: FindWaifuImUseCase,
    private val switchImFavoriteUseCase : SwitchImFavoriteUseCase
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailImViewModel(waifuId, findWaifuImUseCase, switchImFavoriteUseCase) as T
    }
}