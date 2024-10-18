package com.mackenzie.waifuviewer.ui.detail.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.ui.detail.DetailImViewModel

@Composable
fun DetailScreenContent(
    state: DetailImViewModel.UiState,
    prepareDownload: (String, String, String) -> Unit,
    onFavoriteClicked: () -> Unit,
    onDownloadClick: () -> Unit
) {

    // val state = imViewModel.state.collectAsState().value

    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        state.waifuIm?.let { waifu ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(waifu.url)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_baseline_download),
                // placeholder = lottiePlaceholder(),
                // onLoading = { LoadingAnimation(modifier = Modifier.fillMaxSize()) },
                error = painterResource(R.drawable.ic_failed),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                onClick = {onFavoriteClicked()}
            ) {
                Icon(
                    painter = painterResource(
                        id = if (waifu.isFavorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
                    ),
                    contentDescription = null
                )
            }
            Text(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp),
                text = waifu.imageId.toString(),
                fontSize = 25.sp,
                style = MaterialTheme.typography.bodyMedium
            )

            FloatingActionButton(
                onClick = { onDownloadClick() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_download),
                    contentDescription = null
                )
            }
            // TODO: Implement download functionality
            prepareDownload(waifu.imageId.toString(), waifu.url, waifu.url.substringAfterLast('.'))
        }
        state.error?.let {

            LoadingAnimationError(modifier = Modifier.fillMaxSize())
            Text(
                text = it.toString(),
                fontSize = 25.sp,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
            )
        }
    }
}