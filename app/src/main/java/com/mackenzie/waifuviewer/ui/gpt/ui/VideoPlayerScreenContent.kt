package com.mackenzie.waifuviewer.ui.gpt.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.mackenzie.waifuviewer.ui.gpt.PlayerViewModel

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VideoPlayerScreenContent(
    videoId: String = "",
    videoUrl: String = "",
    embedUrl: String = "",
    vm: PlayerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by vm.state.collectAsStateWithLifecycle()
    val testUrl = "https://embed.redtube.com/?id=206274421"

    LaunchedEffect(videoUrl) {
        vm.getVideoFromEmbeddedUrl(videoUrl)
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    Log.e("VideoHubScreenContent", "Loading EXOPLayer URL...=${videoUrl}")
    Log.e("VideoHubScreenContent", "Loading EXOPLayer EmbedURL...=${embedUrl}")
    Log.e("VideoHubScreenContent", "Loading EXOPLayer Video ID...=${videoId}")


    when {
        state.isLoading -> {
            Log.e("VideoPlayerScreenContent", "Loading...")
            /*Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }*/
            Snackbar.make(LocalView.current, "Loading...", Snackbar.LENGTH_SHORT).show()
        }
        state.error != null -> {
            Log.e("VideoPlayerScreenContent", "Error: ${state.error}")
            /*Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: ${state.error}")
            }*/
            Snackbar.make(LocalView.current, "Error=${state.error}", Snackbar.LENGTH_SHORT).show()
        }
        else -> {

            exoPlayer.apply {
                val mediaItem = MediaItem.fromUri(state.embeddedVideoFile?.toUri() ?: videoUrl.toUri())
                setMediaItem(mediaItem)
                // 3. Preparar el reproductor
                prepare()
                // Iniciar la reproducción automáticamente
                playWhenReady = true
            }

            Log.e("VideoHubScreenContent", "embeddedVideoFile=${state.embeddedVideoFile}")
            Log.e("VideoHubScreenContent", "embeddedVideoFile.toUTI()=${state.embeddedVideoFile?.toUri()}")
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
    }

    // 4. Liberar el reproductor cuando el Composable se destruya
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}