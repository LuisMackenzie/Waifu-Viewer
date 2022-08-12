package com.mackenzie.waifuviewer.ui.favs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.data.toError
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.ui.common.Scope
import com.mackenzie.waifuviewer.usecases.GetFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    getFavoritesUseCase: GetFavoritesUseCase
    ): ViewModel(), Scope by Scope.Impl() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getFavoritesUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) }}
                .collect{ favoriteWaifus -> _state.update { UiState(isLoading = false, waifus = favoriteWaifus) } }
        }
    }

    fun onDeleteFavorite(favoriteItem: FavoriteItem) {
        viewModelScope.launch {

        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val waifus: List<FavoriteItem>? = null,
        val error: Error? = null
    )

}