package com.mackenzie.waifuviewer.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.usecases.FindWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.SwitchPicFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailPicsViewModel (
    waifuId: Int,
    findWaifuPicUseCase: FindWaifuPicUseCase,
    private val switchPicFavoriteUseCase: SwitchPicFavoriteUseCase
    ): ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()


    init {
        viewModelScope.launch {
            findWaifuPicUseCase(waifuId).collect {
                _state.value = UiState(waifuPic = it)
            }
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            _state.value.waifuPic?.let { switchPicFavoriteUseCase(it) }
        }
    }

    data class UiState(val waifuPic: com.mackenzie.waifuviewer.domain.WaifuPicItem? = null)

}

@Suppress("UNCHECKED_CAST")
class DetailPicsViewModelFactory(
    private val waifuId: Int,
    private val findWaifuPicUseCase: FindWaifuPicUseCase,
    private val switchPicFavoriteUseCase: SwitchPicFavoriteUseCase
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailPicsViewModel(waifuId, findWaifuPicUseCase, switchPicFavoriteUseCase) as T
    }
}