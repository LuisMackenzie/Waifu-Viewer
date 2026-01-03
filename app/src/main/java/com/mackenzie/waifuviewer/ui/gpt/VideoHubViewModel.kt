package com.mackenzie.waifuviewer.ui.gpt

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.domain.video.VideoDomainItem
import com.mackenzie.waifuviewer.usecases.video.GetVideoServersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoHubViewModel @Inject constructor(
    private val getVideoServersUseCase: GetVideoServersUseCase
): ViewModel() {

    private val _state = MutableStateFlow(VideoHubUiState())
    val state: StateFlow<VideoHubUiState> = _state.asStateFlow()

    fun getServers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getVideoServersUseCase().fold(
                ifLeft = { error ->
                    _state.update { it.copy(isLoading = false, error = error.toString()) }
                },
                ifRight = { videoListItem ->
                    Log.e("VideoHubViewModel", "Video servers fetched: ${videoListItem.videos.size} videos")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            videos = videoListItem.videos,
                            error = null
                        )
                    }
                }
            )
        }
    }


    data class VideoHubUiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val videos: List<VideoDomainItem> = emptyList()
    )


}