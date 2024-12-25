package com.mackenzie.waifuviewer.ui.detail.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.DownloadModel
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
        onSearchClick = { vm.onSearchClicked(it) }
    )
}

@Preview(showBackground = true)
@Composable
fun DetailImScreenContent(
    state: DetailImViewModel.UiState = previewDetailState(),
    onFavoriteClicked: () -> Unit = {},
    onSearchClick: (String) -> Unit = {}
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    var openAlertDialog by remember { mutableStateOf(false) }
    var waifuUrl by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var download: DownloadModel by remember { mutableStateOf(DownloadModel("","","")) }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) { getString(context, R.string.waifus_permissions_content).showToast(context) }
        }


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
            download = DownloadModel(waifu.imageId.toString(), waifu.url, waifu.url.substringAfterLast('.'))
            ZoomableImage(waifu.url)
            DetailFabFavorites(isFavorite = waifu.isFavorite, onFavoriteClicked = onFavoriteClicked)
            DetailTitle(title = waifu.imageId.toString())
            DetailFabDownload(onDownloadClick = { onDownloadClick(download, coroutineScope, context, permissionLauncher) }, onSearchClick = { openAlertDialog = true })
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
    onFavoriteClicked: () -> Unit,
    onSearchClick: (String) -> Unit = {}
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    var openAlertDialog by remember { mutableStateOf(false) }
    var waifuUrl by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var download: DownloadModel by remember { mutableStateOf(DownloadModel("","","")) }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) { getString(context, R.string.waifus_permissions_content).showToast(context) }
        }

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
            download = DownloadModel(title, waifu.url, waifu.url.substringAfterLast('.'))
            ZoomableImage(waifu.url)
            DetailFabFavorites(isFavorite = waifu.isFavorite, onFavoriteClicked = onFavoriteClicked)
            DetailTitle(title = title)
            DetailFabDownload(onDownloadClick = { onDownloadClick(download, coroutineScope, context, permissionLauncher) }, onSearchClick = { openAlertDialog = true })
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
    onFavoriteClicked: () -> Unit,
    onSearchClick: (String) -> Unit = {}
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    var openAlertDialog by remember { mutableStateOf(false) }
    var waifuUrl by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var download: DownloadModel by remember { mutableStateOf(DownloadModel("","","")) }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) { getString(context, R.string.waifus_permissions_content).showToast(context) }
        }

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
            download = DownloadModel(title, waifu.url, waifu.url.substringAfterLast('.'))
            ZoomableImage(waifu.url)
            DetailFabFavorites(isFavorite = waifu.isFavorite, onFavoriteClicked = onFavoriteClicked)
            DetailTitle(title = if (waifu.artistName.isNotEmpty()) waifu.artistName else waifu.animeName)
            DetailFabDownload(onDownloadClick = { onDownloadClick(download, coroutineScope, context, permissionLauncher) }, onSearchClick = { openAlertDialog = true })
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
    onFavoriteClicked: () -> Unit,
    onSearchClick: (String) -> Unit = {}
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    var openAlertDialog by remember { mutableStateOf(false) }
    var waifuUrl by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var download: DownloadModel by remember { mutableStateOf(DownloadModel("","","")) }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) { getString(context, R.string.waifus_permissions_content).showToast(context) }
        }

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
            download = DownloadModel(waifu.title, waifu.url, waifu.url.substringAfterLast('.'))
            ZoomableImage(waifu.url)
            DetailFabFavorites(isFavorite = waifu.isFavorite, onFavoriteClicked = onFavoriteClicked)
            DetailTitle(title = waifu.title)
            DetailFabDownload(onDownloadClick = { onDownloadClick(download, coroutineScope, context, permissionLauncher) }, onSearchClick = { openAlertDialog = true })
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