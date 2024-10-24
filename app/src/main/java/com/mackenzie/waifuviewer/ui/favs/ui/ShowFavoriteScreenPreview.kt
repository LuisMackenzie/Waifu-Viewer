package com.mackenzie.waifuviewer.ui.favs.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.ui.favs.FavoriteViewModel

@Preview(showBackground = true)
@Composable
fun ShowFavoriteScreenPreview() {
    FavoriteScreenContent(
        state = previewState(),
        onItemClick = { },
        onItemLongClick = { },
        onFabClick = { },
        hideInfoCount = { }
    )
}

@Composable
private fun previewState() = FavoriteViewModel.UiState(
    waifus = getMedia(),
    error = null
)

fun getMedia() = (1..20).map {
    FavoriteItem(
        id = it,
        url = "https://nekos.best/api/v2/neko/f09f1d72-4d7d-43ac-9aec-79f0544b95c3.png",
        title = "title $it",
        isFavorite = true
    )
}