package com.mackenzie.waifuviewer.ui.gpt.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.OnlinePrediction
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.android.material.snackbar.Snackbar
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.VideoItem
import com.mackenzie.waifuviewer.domain.getMedia
import com.mackenzie.waifuviewer.domain.getMedia2


@Preview(showBackground = true, heightDp = 150, widthDp = 120)
@Composable
fun RenderItem(
    modifier: Modifier = Modifier,
    item: VideoItem = getMedia2().first(),
    onFavoriteClick: () -> Unit = {},
    onItemClick: (VideoItem) -> Unit = {}
) {
    Card(
        shape =  MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .clickable { onItemClick(item) }
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .height(120.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            AsyncImage(
                model= ImageRequest.Builder(LocalContext.current)
                    .data(item.thumb)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.baseline_downloading),
                error = painterResource(R.drawable.baseline_report_error),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxSize(),
            )

            Text(
                textAlign = TextAlign.Center,
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 1.dp)
                    .background(
                        shape = MaterialTheme.shapes.small,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background
                            )
                        ))
            )

            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.TopEnd)
                    .clickable { onFavoriteClick() }

            )

            Icon(
                imageVector = Icons.Default.OnlinePrediction,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(2.dp)
                    .size(20.dp)
                    .background(if (item.isOnline) Color.Cyan else Color.Red, shape = MaterialTheme.shapes.medium)
                    .align(Alignment.TopStart)
                    .clickable { onFavoriteClick() }

            )
        }
    }
}

@Composable
fun RenderItem2(item: VideoItem, modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium.copy(
            topStart = CornerSize(100.dp),
            topEnd = CornerSize(100.dp),
            bottomEnd = CornerSize(100.dp),
            bottomStart = CornerSize(100.dp)
        ),
        // shape = Shapes().extraLarge,
        elevation = CardDefaults.cardElevation(18.dp),
        border = BorderStroke(3.dp, Color.Gray),

        ) {
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(200.dp),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.url)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.baseline_downloading),
                error = painterResource(R.drawable.baseline_report_error),
                // error = ErrorImage(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )
            if (item.type == VideoItem.Type.VIDEO) {
                Icon(
                    imageVector = Icons.Default.PlayCircleOutline,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(98.dp)
                        .align(Alignment.Center)
                )
            }
        }
        Box(
            modifier = Modifier
                .width(200.dp)
                .background(MaterialTheme.colorScheme.secondary)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    textAlign = TextAlign.Start,
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(
                    imageVector = loadIcon(item.type),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(26.dp)
                    // .padding(25.dp)

                )
            }
        }
    }
}

fun loadIcon(type: VideoItem.Type): ImageVector = when (type) {
    VideoItem.Type.PHOTO -> Icons.Default.Image
    VideoItem.Type.VIDEO -> Icons.Default.SmartDisplay
    VideoItem.Type.AUDIO -> Icons.Default.Mic
    VideoItem.Type.SERVER -> Icons.Default.PlayCircle
}