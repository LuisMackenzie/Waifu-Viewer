package com.mackenzie.waifuviewer.ui.selector.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mackenzie.waifuviewer.R

@Composable
fun BackgroundImage(
    imageUrl: String = "",
) {
    AsyncImage(
        model= ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        error = painterResource(R.drawable.ic_error_grey),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
fun BackgroundImageError(
    image: Int = R.drawable.ic_baseline_add_24
) {
    AsyncImage(
        model= ImageRequest.Builder(LocalContext.current)
            .data(image)
            .crossfade(true)
            .build(),
        error = painterResource(R.drawable.ic_error_grey),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
    )
}