package com.mackenzie.waifuviewer.ui.detail.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mackenzie.waifuviewer.ui.common.ui.previewDetailState
import com.mackenzie.waifuviewer.ui.detail.DetailBestViewModel
import com.mackenzie.waifuviewer.ui.detail.DetailFavsViewModel
import com.mackenzie.waifuviewer.ui.detail.DetailImViewModel
import com.mackenzie.waifuviewer.ui.detail.DetailPicsViewModel
import com.mackenzie.waifuviewer.ui.favs.ui.WaifuSearchDialog

@Preview(showBackground = true)
@Composable
fun DetailImScreenContent(
    state: DetailImViewModel.UiState = previewDetailState(),
    prepareDownload: (String, String, String) -> Unit = { _, _, _ -> },
    onFavoriteClicked: () -> Unit = {},
    onDownloadClick: () -> Unit = {},
    onSearchClick: (String) -> Unit = {}
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    var openAlertDialog by remember { mutableStateOf(false) }
    var waifuUrl by remember { mutableStateOf("") }


    if (openAlertDialog) {
        WaifuSearchDialog(
            onDismissRequest = { openAlertDialog = it },
            onConfirmation = { onSearchClick(waifuUrl) ; openAlertDialog = false }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        state.waifuIm?.let { waifu ->
            waifuUrl = waifu.url
            ZoomableImage(waifu.url)
            DetailFabFavorites(isFavorite = waifu.isFavorite, onFavoriteClicked = onFavoriteClicked)
            DetailTitle(title = waifu.imageId.toString())
            DetailFabDownload(onDownloadClick = onDownloadClick, onSearchClick = { openAlertDialog = true })
            prepareDownload(waifu.imageId.toString(), waifu.url, waifu.url.substringAfterLast('.'))
        }
        state.search?.let { search ->
            showBottomSheet = true
            ModalBottomSheetLayout(
                showBottomSheet = showBottomSheet,
                searchResults = search
            )
        }
        state.error?.let {
            LoadingAnimationError()
            DetailErrorTitle(error = it.toString())
        }
    }
}

@Composable
fun DetailPicsScreenContent(
    state: DetailPicsViewModel.UiState,
    prepareDownload: (String, String, String) -> Unit,
    onFavoriteClicked: () -> Unit,
    onDownloadClick: () -> Unit,
    onSearchClick: (String) -> Unit = {}
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    var openAlertDialog by remember { mutableStateOf(false) }
    var waifuUrl by remember { mutableStateOf("") }

    if (openAlertDialog) {
        WaifuSearchDialog(
            onDismissRequest = { openAlertDialog = it },
            onConfirmation = { onSearchClick(waifuUrl) ; openAlertDialog = false }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        state.waifuPic?.let { waifu ->
            waifuUrl = waifu.url
            val title = waifu.url.substringAfterLast('/').substringBeforeLast('.')
            ZoomableImage(waifu.url)
            DetailFabFavorites(isFavorite = waifu.isFavorite, onFavoriteClicked = onFavoriteClicked)
            DetailTitle(title = title)
            DetailFabDownload(onDownloadClick = onDownloadClick, onSearchClick = { openAlertDialog = true })
            prepareDownload(title,  waifu.url, waifu.url.substringAfterLast('.'))
        }
        state.search?.let { search ->
            showBottomSheet = true
            ModalBottomSheetLayout(
                showBottomSheet = showBottomSheet,
                searchResults = search
            )
        }
        state.error?.let {
            LoadingAnimationError(modifier = Modifier.fillMaxSize())
            DetailErrorTitle(error = it.toString())
        }
    }
}

@Composable
fun DetailBestScreenContent(
    state: DetailBestViewModel.UiState,
    prepareDownload: (String, String, String) -> Unit,
    onFavoriteClicked: () -> Unit,
    onDownloadClick: () -> Unit,
    onSearchClick: (String) -> Unit = {}
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    var openAlertDialog by remember { mutableStateOf(false) }
    var waifuUrl by remember { mutableStateOf("") }

    if (openAlertDialog) {
        WaifuSearchDialog(
            onDismissRequest = { openAlertDialog = it },
            onConfirmation = { onSearchClick(waifuUrl) ; openAlertDialog = false }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        state.waifu?.let { waifu ->
            waifuUrl = waifu.url
            val title = waifu.url.substringAfterLast('/').substringBeforeLast('.')
            ZoomableImage(waifu.url)
            DetailFabFavorites(isFavorite = waifu.isFavorite, onFavoriteClicked = onFavoriteClicked)
            DetailTitle(title = if (waifu.artistName.isNotEmpty()) waifu.artistName else waifu.animeName)
            DetailFabDownload(onDownloadClick = onDownloadClick, onSearchClick = { openAlertDialog = true })
            prepareDownload(title, waifu.url, waifu.url.substringAfterLast('.'))
        }
        state.search?.let { search ->
            showBottomSheet = true
            ModalBottomSheetLayout(
                showBottomSheet = showBottomSheet,
                searchResults = search
            )
        }
        state.error?.let {
            LoadingAnimationError(modifier = Modifier.fillMaxSize())
            DetailErrorTitle(error = it.toString())
        }
    }
}

@Composable
fun DetailFavsScreenContent(
    state: DetailFavsViewModel.UiState,
    prepareDownload: (String, String, String) -> Unit,
    onFavoriteClicked: () -> Unit,
    onDownloadClick: () -> Unit,
    onSearchClick: (String) -> Unit = {}
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    var openAlertDialog by remember { mutableStateOf(false) }
    var waifuUrl by remember { mutableStateOf("") }

    if (openAlertDialog) {
        WaifuSearchDialog(
            onDismissRequest = { openAlertDialog = it },
            onConfirmation = { onSearchClick(waifuUrl) ; openAlertDialog = false }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        state.waifu?.let { waifu ->
            waifuUrl = waifu.url
            ZoomableImage(waifu.url)
            DetailFabFavorites(isFavorite = waifu.isFavorite, onFavoriteClicked = onFavoriteClicked)
            DetailTitle(title = waifu.title)
            DetailFabDownload(onDownloadClick = onDownloadClick, onSearchClick = { openAlertDialog = true })
            prepareDownload(waifu.title, waifu.url, waifu.url.substringAfterLast('.'))
        }
        state.search?.let { search ->
            showBottomSheet = true
            ModalBottomSheetLayout(
                showBottomSheet = showBottomSheet,
                searchResults = search
            )
        }
        state.error?.let {
            LoadingAnimationError(modifier = Modifier.fillMaxSize())
            DetailErrorTitle(error = it.toString())
        }
    }
}