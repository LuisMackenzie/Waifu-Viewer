package com.mackenzie.waifuviewer.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.models.Waifu
import com.mackenzie.waifuviewer.models.datasource.WaifusRepository
import com.mackenzie.waifuviewer.models.db.WaifuPicItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailPicsViewModel (private val waifuId: Int, private val repository: WaifusRepository): ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.findPicsById(waifuId).collect {
                _state.value = UiState(waifuPic = it)
            }
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value.waifuPic?.let { repository.switchPicsFavorite(it) }
        }
    }

    data class UiState(val waifuPic: WaifuPicItem? = null)

}

@Suppress("UNCHECKED_CAST")
class DetailPicsViewModelFactory(private val waifuId: Int, private val repository: WaifusRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailPicsViewModel(waifuId, repository) as T
    }
}