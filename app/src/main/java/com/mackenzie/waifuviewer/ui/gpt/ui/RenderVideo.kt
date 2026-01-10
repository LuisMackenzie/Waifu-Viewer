package com.mackenzie.waifuviewer.ui.gpt.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleOutline
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.VideoItem
import com.mackenzie.waifuviewer.domain.getMedia
import com.mackenzie.waifuviewer.domain.video.VideoDomainItem

@Preview( showBackground = true, heightDp = 200, widthDp = 150)
@Composable
fun RenderVideo(
    // modifier: Modifier = Modifier,
    item: VideoDomainItem = getMedia().first(),
    onItemClick: (VideoDomainItem) -> Unit = {}
) {

    Card(
        shape =  MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .padding(4.dp)
            .height(200.dp)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Cyan)
                .clickable { onItemClick(item) }
        ) {
            AsyncImage(
                model= ImageRequest.Builder(LocalContext.current)
                    .data(item.video.thumb)
                    // .data("https://loremflickr.com/400/400/girl?lock=24")
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.baseline_downloading),
                error = painterResource(R.drawable.baseline_report_error),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )

            Text(
                textAlign = TextAlign.Center,
                text = item.video.duration,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
                    .background(MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.small)
            )

            Row(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background
                        )
                    ))
                    .fillMaxWidth()
                    .padding(start = 2.dp, end = 2.dp, bottom = 2.dp)
                    .align(Alignment.BottomCenter)

            ) {

                Text(
                    textAlign = TextAlign.Center,
                    text = item.video.title,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        // .background(MaterialTheme.colorScheme.background)
                        .background(Color.Transparent)
                        .weight(5f)
                )
                Icon(
                    imageVector = loadIcon(VideoItem.Type.VIDEO),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(40.dp)
                        .weight(1f)

                )
            }
        }
    }


}


// @Preview( showBackground = true, heightDp = 200, widthDp = 150)
@Composable
fun RenderVideo2(
    modifier: Modifier = Modifier,
    item: VideoDomainItem = getMedia().first(),
    onItemClick: (VideoDomainItem) -> Unit = {}
) {

    Log.e("RenderVideo", "Rendering video item: ${item.video.title}")
    // Card(
    Column(
        modifier = modifier.clickable { onItemClick(item) }
    ) {
        Box(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
                .background(Color.Blue)
        ) {
            AsyncImage(
                model= ImageRequest.Builder(LocalContext.current)
                    .data(item.video.thumb)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.baseline_downloading),
                error = painterResource(R.drawable.baseline_report_error),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )
        }
        Box(
            // contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .background(Color.Red)
                .padding(16.dp)
        ) {
            Text(
                textAlign = TextAlign.Start,
                text = item.video.title,
                style = MaterialTheme.typography.bodyLarge
            )
            Icon(
                imageVector = loadIcon(VideoItem.Type.VIDEO),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(26.dp)
                    .align(Alignment.CenterEnd)
            )
        }
    }
}