package com.mackenzie.waifuviewer.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.di.WaifuId
import com.mackenzie.waifuviewer.domain.AnimeSearchItem
import com.mackenzie.waifuviewer.usecases.im.FindWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.im.SwitchImFavoriteUseCase
import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.usecases.moe.GetSearchMoeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailImViewModel @Inject constructor (
    @WaifuId private val waifuId: Int,
    findWaifuImUseCase: FindWaifuImUseCase,
    private val switchImFavoriteUseCase : SwitchImFavoriteUseCase,
    private val getSearchMoeUseCase : GetSearchMoeUseCase,
    // private val getSearchMoeUseCase2 : GetSearchMoeUseCase2
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
            _state.value.waifuIm?.let { waifu ->
                val error = switchImFavoriteUseCase(waifu)
                _state.update { it.copy(error = error) }
            }
        }
    }

    fun onSearchClicked(url: String) {
        viewModelScope.launch {
            val search = getSearchMoeUseCase(url)
            search.fold(
                ifLeft = { error -> _state.update { it.copy(error = error) } },
                ifRight = { search -> _state.update { it.copy(search = search) } }
            )
        }
    }

    /*fun onSearchClicked2(url: String) {
        viewModelScope.launch {
            val search = getSearchMoeUseCase2(url)
            _state.update { it.copy(search = search) }
        }
    }*/

    data class UiState(
        val waifuIm: WaifuImItem? = null,
        val search: List<AnimeSearchItem>? = null,
        val error: Error? = null
    )
}

