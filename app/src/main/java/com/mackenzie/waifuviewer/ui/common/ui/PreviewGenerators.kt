package com.mackenzie.waifuviewer.ui.common.ui

import androidx.compose.runtime.Composable
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.ui.detail.DetailImViewModel
import com.mackenzie.waifuviewer.ui.favs.FavoriteViewModel
import com.mackenzie.waifuviewer.ui.main.WaifuImViewModel

// Vista Detalle
@Composable
fun previewDetailState() = DetailImViewModel.UiState(
    waifuIm = WaifuImItem(
        id = 1,
        signature = "signature",
        extension = "jpg",
        dominantColor = "dominantColor",
        source = "source",
        uploadedAt = "uploadedAt",
        isNsfw = false,
        width = "width",
        height = "height",
        imageId = 11,
        url = "https://nekos.best/api/v2/neko/f09f1d72-4d7d-43ac-9aec-79f0544b95c3.png",
        previewUrl = "previewUrl",
        isFavorite = false
    ),
    error = null
)

// Vista Favoritos
@Composable
fun previewFavoriteState() = FavoriteViewModel.UiState(
    waifus = getFavoriteMedia(),
    error = null
)

private fun getFavoriteMedia() = (1..20).map {
    FavoriteItem(
        id = it,
        url = "https://nekos.best/api/v2/neko/f09f1d72-4d7d-43ac-9aec-79f0544b95c3.png",
        title = "title $it",
        isFavorite = true
    )
}

// Vista Principal
@Composable
fun previewMainState() = WaifuImViewModel.UiState(
    waifus = getImMedia(),
    error = null
)

private fun getImMedia() = (1..20).map {
    WaifuImItem(
        id = it,
        signature = "signature",
        extension = "jpg",
        dominantColor = "dominantColor",
        source = "source",
        uploadedAt = "uploadedAt",
        isNsfw = false,
        width = "width",
        height = "height",
        imageId = it,
        url = "https://nekos.best/api/v2/neko/f09f1d72-4d7d-43ac-9aec-79f0544b95c3.png",
        previewUrl = "previewUrl",
        isFavorite = if(it % 3 == 0) true else false
    )
}