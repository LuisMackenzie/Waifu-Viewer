package com.mackenzie.waifuviewer.ui.gpt.ui

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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.google.android.material.snackbar.Snackbar
import com.mackenzie.waifuviewer.domain.VideoItem
import com.mackenzie.waifuviewer.domain.getHentaiServers
import com.mackenzie.waifuviewer.domain.getLiveCamsServers
import com.mackenzie.waifuviewer.domain.getVideoServers
import com.mackenzie.waifuviewer.ui.common.ui.isNavigationBarVisible


@Composable
fun ServerList(
    itemSection01: List<VideoItem> = getVideoServers(),
    itemSection02: List<VideoItem> = getLiveCamsServers(),
    itemSection03: List<VideoItem> = getHentaiServers(),
    padding: PaddingValues,
    onFavoriteClick: () -> Unit = {},
    onItemClick: (VideoItem) -> Unit = {}
) {

    val localView = LocalView.current

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
            TitleText("Seccion numero 01")
        }
        items(itemSection01) { item ->
            RenderItem(
                item = item,
                modifier = Modifier.padding(4.dp),
                onFavoriteClick = onFavoriteClick,
            ) { serverItem ->
                when (serverItem.id) {
                    2 -> onItemClick(item)
                    else -> {
                        Snackbar.make(localView, "Server Under Development!", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            TitleText("Seccion numero 02")
        }
        items(itemSection02) { item ->
            RenderItem(
                item = item,
                modifier = Modifier.padding(4.dp),
                onFavoriteClick = onFavoriteClick,
                onItemClick = {
                    Snackbar.make(localView, "Section Under Development!", Snackbar.LENGTH_SHORT).show()
                    // onItemClick(item)
                }
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            TitleText("Seccion numero 03")
        }
        items(itemSection03) { item ->
            RenderItem(
                item = item,
                modifier = Modifier.padding(4.dp),
                onFavoriteClick = onFavoriteClick,
                onItemClick = {
                    Snackbar.make(localView, "Section Under Development!", Snackbar.LENGTH_SHORT).show()
                    // onItemClick(item)
                }
            )
        }
    }
}