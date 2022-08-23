package com.mackenzie.waifuviewer.ui.favs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.data.toError
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.ui.common.Scope
import com.mackenzie.waifuviewer.usecases.DeleteFavoriteUseCase
import com.mackenzie.waifuviewer.usecases.GetFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    getFavoritesUseCase: GetFavoritesUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase
    ): ViewModel(), Scope by Scope.Impl() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            getFavoritesUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) }}
                .collect{ favoriteWaifus -> _state.update { UiState(waifus = favoriteWaifus) } }
        }
    }

    fun onDeleteFavorite(favoriteItem: FavoriteItem) {
        viewModelScope.launch {
            val error = deleteFavoriteUseCase(favoriteItem)
            _state.update { _state.value.copy(error = error) }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val waifus: List<FavoriteItem>? = null,
        val error: Error? = null
    )

}