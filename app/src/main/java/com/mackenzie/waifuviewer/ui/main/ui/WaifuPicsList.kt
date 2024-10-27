package com.mackenzie.waifuviewer.ui.main.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.ui.common.isLandscape
import com.mackenzie.waifuviewer.ui.theme.Dimens

@Composable
fun WaifuPicsList(
    items: List<WaifuPicItem>,
    modifier: Modifier = Modifier,
    onItemClick: (WaifuPicItem) -> Unit,
    onLoadMore: () -> Unit
) {


    LazyVerticalGrid(
        columns = if (LocalContext.current.isLandscape()) GridCells.Adaptive(Dimens.waifuItemHeight) else GridCells.Fixed(2),
        // state = listState,
        modifier = modifier
    ) {
        items(items) { item ->
            WaifuPicItemView(
                waifu = item,
                modifier = Modifier.padding(if (LocalContext.current.isLandscape()) Dimens.waifuItemPaddingLandscape else Dimens.waifuItemPadding),
                onWaifuClick = { onItemClick(it) }
            )
        }
    }
}