package com.mackenzie.waifuviewer.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.data.WaifusRepository
import com.mackenzie.waifuviewer.models.Waifu
import com.mackenzie.waifuviewer.ui.common.Scope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel (waifuUrl: String): ViewModel() {

    private val _state = MutableStateFlow(UiState(waifuUrl))
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        // waifu = state.value.waifu
        // loadWaifu(waifu)
    }


    private fun loadWaifu(waifuUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {

            _state.value = _state.value.copy(waifuUrl)
            /*try {
                val url = URL(waifu.url)
                val image: Bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                // SaveImage().saveImageToStorage(, image, waifu.file, type)
            } catch (e: IOException) {
                Log.e("DetailViewModel", "Error downloading image", e)
                null
            }*/
            // _state.value = UiState(isLoading = false)
            // _state.value = UiState(waifu = waifu, isLoading = false)
        }

        // _state.value = _state.value?.copy(waifu = waifu, isLoading = false)
        // downloadWaifu(waifu)
        // val waifu = waifuRepository.getWaifu()
        // _state.value = _state.value?.copy(waifus = waifu, isLoading = false)

    }

    fun onUiReady() {
        // TODO
    }

    fun onStoragePermissionChecked() {
        _state.value = _state.value.copy(requestStoragePermission = false)

    }

    private fun downloadWaifu(waifu: Waifu) {
        val type: String = selectMimeType(waifu.extension)
        /*val type: String = selectMimeType(waifu.extension)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val url = URL(link)
                val image: Bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                SaveImage().saveImageToStorage(
                    applicationContext,
                    image,
                    title,
                    type
                )
            } catch (e: IOException) {
                Log.d(ContentValues.TAG, "error: ${e.localizedMessage}")
            }
        }*/
    }

    private fun selectMimeType(fileType: String): String {
        when (fileType) {
            ".jpg" -> return "image/jpeg"
            ".jpeg" -> return "image/jpeg"
            ".png" -> return "image/png"
            ".gif" -> return "image/gif"
            else -> return "image/jpeg"
        }
    }

    data class UiState(
        val url: String? = null,
        val requestStoragePermission: Boolean = true)

}

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val waifuUrl: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(waifuUrl) as T
    }
}