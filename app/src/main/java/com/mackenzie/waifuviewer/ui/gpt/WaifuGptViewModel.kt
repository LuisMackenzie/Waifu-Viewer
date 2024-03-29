package com.mackenzie.waifuviewer.ui.gpt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.ui.favs.FavoriteViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaifuGptViewModel @Inject constructor(): ViewModel() {

    private val _state = MutableStateFlow(FavoriteViewModel.UiState())
    val state: StateFlow<FavoriteViewModel.UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {

        }
    }



    data class UiState(
        val isLoading: Boolean = false,
        val waifus: List<FavoriteItem>? = null,
        val error: Error? = null
    )

}