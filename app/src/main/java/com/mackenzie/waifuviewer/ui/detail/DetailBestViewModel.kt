package com.mackenzie.waifuviewer.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.di.WaifuId
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuBestItem
import com.mackenzie.waifuviewer.usecases.best.FindWaifuBestUseCase
import com.mackenzie.waifuviewer.usecases.best.SwitchBestFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailBestViewModel @Inject constructor (
    @WaifuId private val waifuId: Int,
    findWaifuBestUseCase: FindWaifuBestUseCase,
    private val switchBestFavoriteUseCase : SwitchBestFavoriteUseCase,
): ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            findWaifuBestUseCase(waifuId).collect {
                _state.value = UiState(waifu = it)
            }
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            _state.value.waifu?.let { waifu ->
                val error = switchBestFavoriteUseCase(waifu)
                _state.update { it.copy(error = error) }
            }
        }

    }

    data class UiState(
        val waifu: WaifuBestItem? = null,
        val error: Error? = null
    )
}