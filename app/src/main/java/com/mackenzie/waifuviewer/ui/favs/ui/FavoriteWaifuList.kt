package com.mackenzie.waifuviewer.ui.favs.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mackenzie.waifuviewer.domain.FavoriteItem

@Composable
fun FavoriteWaifuList(
    items: List<FavoriteItem>,
    modifier: Modifier = Modifier,
    onItemClick: (FavoriteItem) -> Unit
) {
    LazyVerticalGrid(
        // Ayuda a ver los margenes
        // modifier = Modifier.background(Color.Cyan),
        contentPadding = PaddingValues(4.dp),
        columns = GridCells.Fixed(2),
        // columns = GridCells.Adaptive(150.dp),
        modifier = modifier
    ) {
        items(items) { item ->
            WaifuItem(
                waifu = item,
                modifier = Modifier.padding(4.dp),
                onWaifuClick = { onItemClick(it) }
            )
        }
    }
}