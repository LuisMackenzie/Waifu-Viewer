package com.mackenzie.waifuviewer

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.data.WaifuManager
import com.mackenzie.waifuviewer.data.WaifusRepository
import com.mackenzie.waifuviewer.models.Waifu
import com.mackenzie.waifuviewer.models.WaifuPic
import com.mackenzie.waifuviewer.models.WaifuPicsResult
import com.mackenzie.waifuviewer.models.WaifuResult
import com.mackenzie.waifuviewer.ui.common.Scope
import com.mackenzie.waifuviewer.ui.main.WaifuFragment
import com.mackenzie.waifuviewer.ui.main.WaifuFragmentArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.HttpException
import kotlin.jvm.internal.Ref

class WaifuViewModel(private val waifusRepository: WaifusRepository): ViewModel(), Scope by Scope.Impl() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()
    // private val _events = Channel<UiEvent> ()
    // val events = _events.receiveAsFlow()

    private fun onWaifusReady() {
        viewModelScope.launch {
            _state.value = UiState(isLoading = true)
            _state.value = UiState(waifusIm = waifusRepository.getRandomWaifus(), isLoading = false)
        }
    }

    fun onCustomWaifusReady(isNsfw: Boolean, isGif: Boolean, tag: String, orientation: String) {

        viewModelScope.launch {
            _state.value = UiState(isLoading = true)
            val waifus: WaifuResult
            if (tag == "all") {
                waifus = waifusRepository.getCustomRandomWaifus(isNsfw, isGif, orientation)
                _state.value = UiState(waifusIm = waifus, isLoading = false)
            } else {
                waifus = waifusRepository.getCustomTagWaifus(isNsfw, isGif, tag, orientation)
                // waifus = waifusRepository.getEspecialWaifu(isNsfw, isGif, tag)
                _state.value = UiState(waifusIm = waifus, isLoading = false)
            }
            if (waifus.waifus.isEmpty()) {
                _state.value = UiState(isLoading = false, isError = true)
            }
        }
    }

    fun onCustomWaifusPicsReady(isNsfw: Boolean, tag: String) {
            _state.value = UiState(isLoading = true)
            if (isNsfw) {
                waifusGetter("nsfw", tag)
            } else {
                waifusGetter("sfw", tag)
            }
    }

    private fun waifusGetter(isNsfw: String, tag: String) {
        viewModelScope.launch {
            WaifuManager().getWaifuPics(isNsfw = isNsfw, tag = tag) { waifuResult ->
                if (waifuResult != null){
                    if (!waifuResult.isEmpty()) {
                        _state.value = UiState(waifusPics = waifuResult, isLoading = false)
                    } else {
                        _state.value = UiState(isLoading = false, isError = true)
                    }
                }
            }
        }
    }

    /*override fun onCleared() {
        destroyScope()
        super.onCleared()
    }*/


    data class UiState(
        val isLoading: Boolean = false,
        val waifusIm: WaifuResult? = null,
        val waifusPics: List<String>? = null,
        // val waifu: String? = null,
        val isError: Boolean = false
    )


}

@Suppress("UNCHECKED_CAST")
class WaifuViewModelFactory(private val waifusRepository: WaifusRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WaifuViewModel(waifusRepository) as T
    }
}