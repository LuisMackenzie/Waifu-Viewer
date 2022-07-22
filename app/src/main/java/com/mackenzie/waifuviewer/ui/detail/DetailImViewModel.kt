package com.mackenzie.waifuviewer.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.models.datasource.WaifusRepository
import com.mackenzie.waifuviewer.models.db.WaifuImItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailImViewModel (private val waifuId: Int, private val repository: WaifusRepository): ViewModel() {

    private val _state = MutableStateFlow(UiState(waifuId))
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.findImById(waifuId).collect {
                _state.value = UiState(waifuIm = it)
                // _state.value = UiState(idIm = it.id, waifuIm = it)
            }
        }
    }

    data class UiState(
        val idIm: Int? = null,
        val waifuIm: WaifuImItem? = null,
        val requestStoragePermission: Boolean = true)

}

@Suppress("UNCHECKED_CAST")
class DetailImViewModelFactory(private val waifuId: Int, private val repository: WaifusRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailImViewModel(waifuId, repository) as T
    }
}