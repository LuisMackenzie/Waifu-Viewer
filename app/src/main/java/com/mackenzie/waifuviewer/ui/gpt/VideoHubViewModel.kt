package com.mackenzie.waifuviewer.ui.gpt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.domain.video.VideoDomainItem
import com.mackenzie.waifuviewer.usecases.video.GetVideoDefaultListUseCase
import com.mackenzie.waifuviewer.usecases.video.GetVideoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoHubViewModel @Inject constructor(
    private val getVideoListUseCase: GetVideoListUseCase,
    private val getDefaultListUseCase: GetVideoDefaultListUseCase
): ViewModel() {

    private val _state = MutableStateFlow(VideoHubUiState())
    val state: StateFlow<VideoHubUiState> = _state.asStateFlow()

    fun getVideoList(
        page: Int?,
        thumbsize: String?,
        search: String?,
        tags: List<String>?,
        stars: List<String>?,
        category: String?,
        ordering: String?,
        period: String?
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getVideoListUseCase(
                page,
                thumbsize,
                search,
                tags,
                stars,
                category,
                ordering,
                period
            ).fold(
                ifLeft = { error ->
                    _state.update { it.copy(isLoading = false, error = error.toString()) }
                },
                ifRight = { videoListItem ->
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

    fun getDefaultVideoList() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getDefaultListUseCase().fold(
                ifLeft = { error ->
                    _state.update { it.copy(isLoading = false, error = error.toString()) }
                },
                ifRight = { videoListItem ->
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