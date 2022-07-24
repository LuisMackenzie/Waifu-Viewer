package com.mackenzie.waifuviewer.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.models.datasource.WaifusImRepository
import com.mackenzie.waifuviewer.models.datasource.WaifusPicRepository
import com.mackenzie.waifuviewer.models.db.WaifuPicItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailPicsViewModel (private val waifuId: Int, private val waifusPicRepository: WaifusPicRepository): ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            waifusPicRepository.findPicsById(waifuId).collect {
                _state.value = UiState(waifuPic = it)
            }
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            _state.value.waifuPic?.let { waifusPicRepository.switchPicsFavorite(it) }
        }
    }

    data class UiState(val waifuPic: WaifuPicItem? = null)

}

@Suppress("UNCHECKED_CAST")
class DetailPicsViewModelFactory(private val waifuId: Int, private val waifusPicRepository: WaifusPicRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailPicsViewModel(waifuId, waifusPicRepository) as T
    }
}