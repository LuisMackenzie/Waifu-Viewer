package com.mackenzie.waifuviewer.ui.gpt.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.mackenzie.waifuviewer.ui.gpt.PlayerViewModel

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VideoPlayerScreenContent(
    videoId: String = "",
    videoUrl: String = "",
    vm: PlayerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(videoUrl) {
        vm.getVideoFromEmbeddedUrl(videoUrl)
    }



    // 1. Recordar la instancia de ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // 2. Crear el MediaItem a partir de la URL
            val mediaItem = MediaItem.fromUri(videoUrl.toUri())
            val mediaItem2 = MediaItem.fromUri(state.embeddedVideoFile?.toUri() ?: videoUrl.toUri())

            Log.e("VideoHubScreenContent", "Loading EXOPLayer URL...=${videoUrl}")
            Log.e("VideoHubScreenContent", "embeddedVideoFile=${state.embeddedVideoFile}")
            Log.e("VideoHubScreenContent", "Loading EXOPLayer Video ID...=${videoId}")
            setMediaItem(mediaItem)
            // 3. Preparar el reproductor
            prepare()
            // Iniciar la reproducción automáticamente
            playWhenReady = true
        }
    }

    // 4. Liberar el reproductor cuando el Composable se destruya
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // 5. Integrar el PlayerView de ExoPlayer usando AndroidView
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
            }
        }
    )
}