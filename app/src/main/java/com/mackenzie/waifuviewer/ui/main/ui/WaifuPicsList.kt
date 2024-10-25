package com.mackenzie.waifuviewer.ui.main.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mackenzie.waifuviewer.domain.WaifuPicItem

@Composable
fun WaifuPicsList(
    items: List<WaifuPicItem>,
    modifier: Modifier = Modifier,
    onItemClick: (WaifuPicItem) -> Unit,
    onLoadMore: () -> Unit
) {


    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        // state = listState,
        // columns = GridCells.Adaptive(150.dp),
        modifier = modifier
    ) {
        items(items) { item ->
            WaifuPicItemView(
                waifu = item,
                modifier = Modifier.padding(6.dp),
                onWaifuClick = { onItemClick(it) }
            )
        }
    }
}