package com.mackenzie.waifuviewer.ui.gpt.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mackenzie.waifuviewer.domain.VideoItem
import com.mackenzie.waifuviewer.domain.getMedia
import com.mackenzie.waifuviewer.domain.getMedia2
import com.mackenzie.waifuviewer.domain.getVideoServers
import com.mackenzie.waifuviewer.ui.common.ui.isNavigationBarVisible

@Composable
fun VideoHubList(
    itemSection01: List<VideoItem> = getMedia(),
    titleServer: String = "Video Server",
    padding: PaddingValues,
    onItemClick: (VideoItem) -> Unit = {}
) {
    LazyVerticalGrid(
        contentPadding = PaddingValues(4.dp),
        columns = GridCells.Fixed(3),
        // columns = GridCells.Adaptive(150.dp),
        modifier = if (isNavigationBarVisible()) {
            Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        } else {
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        }
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            TitleText(titleServer)
        }
        items(itemSection01) { item ->
            Log.e("VideoHubList", "VideoHubList - item.thumb: ${item.thumb}")
            Log.e("VideoHubList", "VideoHubList - item.thumb: ${item.url}")
            RenderVideo(
                item = item,
                modifier = Modifier.padding(4.dp),
                onItemClick = { onItemClick(item) }
            )
        }
    }
}