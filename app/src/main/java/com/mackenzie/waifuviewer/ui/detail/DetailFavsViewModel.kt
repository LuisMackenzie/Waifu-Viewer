package com.mackenzie.waifuviewer.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.di.WaifuId
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.usecases.FindFavoriteUseCase
import com.mackenzie.waifuviewer.usecases.SwitchFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailFavsViewModel @Inject constructor(
    @WaifuId private val waifuId: Int,
    findFavoriteUseCase: FindFavoriteUseCase,
    private val switchFavoriteUseCase : SwitchFavoriteUseCase
    ): ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            findFavoriteUseCase(waifuId).collect {
                _state.value = UiState(waifu = it)
            }
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            _state.value.waifu?.let { waifu ->
                val error = switchFavoriteUseCase(waifu)
                _state.update { it.copy(error = error) }
            }
        }
    }

    data class UiState(
        val waifu: FavoriteItem? = null,
        val error: Error? = null
    )
}