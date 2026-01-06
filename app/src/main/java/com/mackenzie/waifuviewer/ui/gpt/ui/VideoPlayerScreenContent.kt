package com.mackenzie.waifuviewer.ui.gpt.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayerScreenContent(modifier: Modifier = Modifier.fillMaxSize(), videoUrl: String) {
    val context = LocalContext.current

    // 1. Recordar la instancia de ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // 2. Crear el MediaItem a partir de la URL
            val mediaItem = MediaItem.fromUri(videoUrl)

            Log.e("VideoHubScreenContent", "Loading EXOPLayer URL...=${videoUrl}")
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
        modifier = modifier,
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
            }
        }
    )
}