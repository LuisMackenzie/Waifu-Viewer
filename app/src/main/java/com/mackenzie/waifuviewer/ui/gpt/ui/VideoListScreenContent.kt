package com.mackenzie.waifuviewer.ui.gpt.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.mackenzie.waifuviewer.domain.VideoItem
import com.mackenzie.waifuviewer.domain.getNameById
import com.mackenzie.waifuviewer.ui.gpt.VideoHubViewModel


@Composable
fun VideoListScreenContent(
    serverId: Int,
    vm: VideoHubViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit = {}
) {

    val state by vm.state.collectAsStateWithLifecycle()

    // Llamar a getServers() cuando se monta el composable o cambia el serverId
    LaunchedEffect(serverId) {
        val serverName = getNameById(serverId)
        vm.getServers()
    }

    Scaffold(
        topBar = { MainAppBar() }
    ) { padding ->

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${state.error}")
                }
            }
            else -> {
                // TODO ELiminar este mapear y subirlo de capa al repositorio.
                // Mapear VideoDomainItem a VideoItem
                /*val videoItems = state.videos.map { videoDomainItem ->
                    VideoItem(
                        id = videoDomainItem.video.videoId.toIntOrNull() ?: 0,
                        title = videoDomainItem.video.title,
                        thumb = videoDomainItem.video.thumb,
                        url = videoDomainItem.video.url,
                        type = VideoItem.Type.VIDEO,
                        description = "${videoDomainItem.video.views} views • ${videoDomainItem.video.duration}"
                    )
                }*/

                VideoHubList(
                    itemSection01 = state.videos,
                    titleServer = getNameById(serverId),
                    padding = padding,
                    onItemClick = { item ->
                        Log.e("VideoHubScreenContent", "ID=${item.video.videoId}, Clicked item: ${item.video.title}")
                        Log.e("VideoHubScreenContent", "Loading URL...=${item.video.url}")
                        Log.e("VideoHubScreenContent", "Loading Embedded URL...=${item.video.embedUrl}")
                        Log.e("VideoHubScreenContent", "Loading Item...=${item}")
                        // Me falta el EmbededUrl en el modelo de dominio
                        // alli esta el video que se podria reproducir directamente
                        onNavigate(item.video.embedUrl)
                    }
                )
            }
        }
    }
}