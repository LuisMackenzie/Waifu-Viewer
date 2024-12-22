package com.mackenzie.waifuviewer.ui.detail.ui

import android.Manifest
import android.app.Activity
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mackenzie.waifuviewer.domain.DownloadModel
import com.mackenzie.waifuviewer.ui.NavHostActivity
import com.mackenzie.waifuviewer.ui.common.app
import com.mackenzie.waifuviewer.ui.common.onDownloadClick
import com.mackenzie.waifuviewer.ui.common.showToast
import com.mackenzie.waifuviewer.ui.common.ui.isNavigationBarVisible
import com.mackenzie.waifuviewer.ui.common.ui.previewDetailState
import com.mackenzie.waifuviewer.ui.detail.DetailBestViewModel
import com.mackenzie.waifuviewer.ui.detail.DetailFavsViewModel
import com.mackenzie.waifuviewer.ui.detail.DetailImViewModel
import com.mackenzie.waifuviewer.ui.detail.DetailPicsViewModel
import com.mackenzie.waifuviewer.ui.favs.ui.WaifuSearchDialog

@Composable
internal fun DetailImScreenContentRoute(
    vm: DetailImViewModel = viewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()

    DetailImScreenContent(
        state = state,
        onFavoriteClicked = { vm.onFavoriteClicked() },
        // onDownloadClick = { vm.onRequestPermissions(Activity()) },
        onSearchClick = { vm.onSearchClicked(it) }
    )
}

@Preview(showBackground = true)
@Composable
fun DetailImScreenContent(
    state: DetailImViewModel.UiState = previewDetailState(),
    // prepareDownload: (String, String, String) -> Unit = { _, _, _ -> },
    onFavoriteClicked: () -> Unit = {},
    // onDownloadClick: () -> Unit = {},
    onSearchClick: (String) -> Unit = {}
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    var openAlertDialog by remember { mutableStateOf(false) }
    var waifuUrl by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var download: DownloadModel by remember { mutableStateOf(DownloadModel("","","")) }


    if (openAlertDialog) {
        WaifuSearchDialog(
            onDismissRequest = { openAlertDialog = it },
            onConfirmation = { onSearchClick(waifuUrl) ; openAlertDialog = false }
        )
    }

    Box(
        modifier = if (isNavigationBarVisible()) {
            Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .background(MaterialTheme.colorScheme.background)
        } else {
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        }
    ) {
        state.waifuIm?.let { waifu ->
            waifuUrl = waifu.url
            // prepareDownload(waifu.imageId.toString(), waifu.url, waifu.url.substringAfterLast('.'))
            download = DownloadModel(waifu.imageId.toString(), waifu.url, waifu.url.substringAfterLast('.'))
            ZoomableImage(waifu.url)
            DetailFabFavorites(isFavorite = waifu.isFavorite, onFavoriteClicked = onFavoriteClicked)
            DetailTitle(title = waifu.imageId.toString())
            DetailFabDownload(onDownloadClick = {
                onDownloadClick(
                    download = download,
                    scope = coroutineScope,
                    context = context
                )
            }, onSearchClick = { openAlertDialog = true })
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

    Box(
        modifier = if (isNavigationBarVisible()) {
            Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .background(MaterialTheme.colorScheme.background)
        } else {
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        }
    ) {
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

    Box(
        modifier = if (isNavigationBarVisible()) {
            Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .background(MaterialTheme.colorScheme.background)
        } else {
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        }
    ) {
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

    Box(
        modifier = if (isNavigationBarVisible()) {
            Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .background(MaterialTheme.colorScheme.background)
        } else {
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        }
    ) {
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