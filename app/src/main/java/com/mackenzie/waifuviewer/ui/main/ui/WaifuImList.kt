package com.mackenzie.waifuviewer.ui.main.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mackenzie.waifuviewer.domain.WaifuImItem
import kotlinx.coroutines.launch

@Composable
fun WaifuImList(
    items: List<WaifuImItem>,
    modifier: Modifier = Modifier,
    onItemClick: (WaifuImItem) -> Unit,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.isNotEmpty() && visibleItems.last().index == items.size - 1) {
                    coroutineScope.launch { onLoadMore() }
                }
            }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        // state = listState,
        // columns = GridCells.Adaptive(150.dp),
        modifier = modifier
    ) {
        items(items) { item ->
            WaifuImItemView(
                waifu = item,
                modifier = Modifier.padding(6.dp),
                onWaifuClick = { onItemClick(it) }
            )
        }
    }
}