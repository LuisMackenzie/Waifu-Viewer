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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.ui.common.isLandscape
import com.mackenzie.waifuviewer.ui.common.ui.getImMedia
import com.mackenzie.waifuviewer.ui.theme.Dimens
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun WaifuImList(
    items: List<WaifuImItem> = getImMedia(),
    modifier: Modifier = Modifier,
    onItemClick: (WaifuImItem) -> Unit = {},
    onLoadMore: () -> Unit = {}
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
        columns = if (LocalContext.current.isLandscape()) GridCells.Adaptive(Dimens.waifuItemHeight) else GridCells.Fixed(2),
        modifier = modifier,
        state = listState
    ) {
        items(items) { item ->
            WaifuImItemView(
                waifu = item,
                modifier = Modifier.padding(if (LocalContext.current.isLandscape()) Dimens.waifuItemPaddingLandscape else Dimens.waifuItemPadding),
                onWaifuClick = { onItemClick(it) }
            )
        }
    }
}