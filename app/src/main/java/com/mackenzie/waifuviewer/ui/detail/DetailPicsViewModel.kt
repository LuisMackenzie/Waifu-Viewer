package com.mackenzie.waifuviewer.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.domain.AnimeSearchItem
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.usecases.moe.GetSearchMoeUseCase
import com.mackenzie.waifuviewer.usecases.pics.FindWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.pics.SwitchPicFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPicsViewModel @Inject constructor(
    // @WaifuId private val waifuId: Int,
    private val findWaifuPicUseCase: FindWaifuPicUseCase,
    private val switchPicFavoriteUseCase: SwitchPicFavoriteUseCase,
    private val getSearchMoeUseCase : GetSearchMoeUseCase
    ): ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    /*init {
        viewModelScope.launch {
            findWaifuPicUseCase(waifuId).collect {
                _state.value = UiState(waifuPic = it)
            }
        }
    }*/

    fun getWaifuResultNavigate(waifuId: Int) {
        viewModelScope.launch {
            findWaifuPicUseCase(waifuId).collect {
                _state.value = UiState(waifuPic = it)
            }
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            _state.value.waifuPic?.let { waifu ->
                val error = switchPicFavoriteUseCase(waifu)
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
        val waifuPic: WaifuPicItem? = null,
        val search: List<AnimeSearchItem>? = null,
        val error: Error? = null
    )
}
