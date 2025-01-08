package com.mackenzie.waifuviewer.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.domain.AnimeSearchItem
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.usecases.FindFavoriteUseCase
import com.mackenzie.waifuviewer.usecases.SwitchFavoriteUseCase
import com.mackenzie.waifuviewer.usecases.moe.GetSearchMoeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailFavsViewModel @Inject constructor(
    // @WaifuId private val waifuId: Int,
    private val findFavoriteUseCase: FindFavoriteUseCase,
    private val switchFavoriteUseCase : SwitchFavoriteUseCase,
    private val getSearchMoeUseCase : GetSearchMoeUseCase
    ): ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    /*init {
        viewModelScope.launch {
            findFavoriteUseCase(waifuId).collect {
                _state.value = UiState(waifu = it)
            }
        }
    }*/

    fun getWaifuResultNavigate(waifuId: Int) {
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

    fun onSearchClicked(url: String) {
        viewModelScope.launch {
            val search = getSearchMoeUseCase(url)
            search.fold(
                ifLeft = { error -> _state.update { it.copy(error = error) } },
                ifRight = { search -> _state.update { it.copy(search = search) } }
            )
        }
    }

    data class UiState(
        val waifu: FavoriteItem? = null,
        val search: List<AnimeSearchItem>? = null,
        val error: Error? = null
    )
}