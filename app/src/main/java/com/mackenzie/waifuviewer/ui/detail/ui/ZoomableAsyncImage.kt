package com.mackenzie.waifuviewer.ui.detail.ui

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mackenzie.waifuviewer.BuildConfig
import com.mackenzie.waifuviewer.R

@Preview(showBackground = true)
@Composable
fun ZoomableImage(imageUrl: String = "https://nekos.best/api/v2/neko/f09f1d72-4d7d-43ac-9aec-79f0544b95c3.png") {
    // Estados para escala y desplazamiento
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    // Gestor de gestos
    val modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTransformGestures { _, pan, zoom, _ ->
                scale *= zoom
                offsetX += pan.x
                offsetY += pan.y
            }
        }

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.ic_baseline_download),
        error = if (BuildConfig.DEBUG) painterResource(R.drawable.ic_offline_background) else painterResource(R.drawable.ic_failed),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offsetX,
                translationY = offsetY
            ),
    )
}