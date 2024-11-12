package com.mackenzie.waifuviewer.ui.common.ui

import androidx.compose.runtime.Composable
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import com.mackenzie.waifuviewer.ui.detail.DetailImViewModel
import com.mackenzie.waifuviewer.ui.favs.FavoriteViewModel
import com.mackenzie.waifuviewer.ui.main.WaifuImViewModel

// Vista Detalle
@Composable
fun previewDetailState() = DetailImViewModel.UiState(waifuIm = getImMediaItem(), error = null)

// Vista Favoritos
@Composable
fun previewFavoriteState() = FavoriteViewModel.UiState(waifus = getFavoriteMedia(), error = null)

@Composable
fun getFavoriteMedia() = (1..20).map { getFavoriteMediaItem(it) }

@Composable
fun getFavoriteMediaItem(id: Int = 1) = FavoriteItem(
    id = id,
    url = "https://nekos.best/api/v2/neko/f09f1d72-4d7d-43ac-9aec-79f0544b95c3.png",
    title = "title",
    isFavorite = true
)

// Vista Principal
@Composable
fun previewMainState() = WaifuImViewModel.UiState(waifus = getImMedia(), error = null)

@Composable
fun getImMedia() = (1..20).map { getImMediaItem(it) }

@Composable
fun getImMediaItem(id: Int = 1) = WaifuImItem(
    id = id,
    signature = "signature",
    extension = "jpg",
    dominantColor = "dominantColor",
    source = "source",
    uploadedAt = "uploadedAt",
    isNsfw = false,
    width = "width",
    height = "height",
    imageId = id,
    url = "https://nekos.best/api/v2/neko/f09f1d72-4d7d-43ac-9aec-79f0544b95c3.png",
    previewUrl = "previewUrl",
    isFavorite = if(id % 3 == 0 || id == 1) true else false
)