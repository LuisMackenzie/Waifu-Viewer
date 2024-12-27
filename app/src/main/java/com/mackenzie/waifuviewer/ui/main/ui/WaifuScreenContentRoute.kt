package com.mackenzie.waifuviewer.ui.main.ui

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.WaifuPicsViewModel
import com.mackenzie.waifuviewer.domain.LoadingState
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.ENHANCED
import com.mackenzie.waifuviewer.domain.ServerType.NEKOS
import com.mackenzie.waifuviewer.domain.ServerType.NORMAL
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.common.showToast
import com.mackenzie.waifuviewer.ui.main.WaifuBestViewModel
import com.mackenzie.waifuviewer.ui.main.WaifuImViewModel

@Composable
fun WaifuScreenContentRoute(
    bundle: Bundle = bundleOf(),
    imViewModel: WaifuImViewModel = hiltViewModel(),
    picsViewModel : WaifuPicsViewModel = hiltViewModel(),
    bestViewModel: WaifuBestViewModel = hiltViewModel(),
    onNavigate: () -> Unit = {}
) {

    val serverMode by remember { mutableStateOf(bundle.getString(Constants.SERVER_MODE) ?: "") }
    val lmState by remember { mutableStateOf(LoadingState()) }
    val context = LocalContext.current
    val activity = LocalContext.current as Activity

    LoadCustomResult(bundle, serverMode, imViewModel, picsViewModel, bestViewModel)

    when (serverMode) {
        NORMAL.value -> {
            WaifuImScreenContent(
                state = imViewModel.state.collectAsStateWithLifecycle().value,
                onWaifuClicked = {}, // { mainState.onWaifuImClicked(it) },
                onRequestMore = {}, // { onLoadMoreWaifusIm() },
                onFabClick = {
                    "Eliminando la database.. .".showToast(context)
                    // imViewModel.onClearImDatabase()
                    // onBackPressedDispatcher?.onBackPressed()
                    // Toast.makeText(requireContext(), getString(R.string.waifus_gone), Toast.LENGTH_SHORT).show()
                }
            )
        }
        ENHANCED.value -> {} // WaifuPicsScreen()
        NEKOS.value -> {} // WaifuNekosScreen()
        else -> {}
    }



}

@Composable
private fun LoadCustomResult(
    bun: Bundle,
    serverMode: String,
    imViewModel: WaifuImViewModel,
    picsViewModel: WaifuPicsViewModel,
    bestViewModel: WaifuBestViewModel
) {
    val isNsfw = bun.getBoolean(Constants.IS_NSFW_WAIFU)
    val isGif = bun.getBoolean(Constants.IS_GIF_WAIFU)
    val orientation = bun.getBoolean(Constants.IS_LANDS_WAIFU)
    val categoryTag = bun.getString(Constants.CATEGORY_TAG_WAIFU) ?: ""

    if (serverMode == stringResource(R.string.server_normal_string)) {
        when (categoryTag) {
            "uniform", "maid", "marin-kitagawa", "oppai" -> {
                imViewModel.onImReady(isNsfw, isGif = false, categoryTag, orientation)
            }
            "mori-calliope", "raiden-shogun" -> {
                imViewModel.onImReady(isNsfw, isGif = false, categoryTag, false)
            }
            else -> {
                imViewModel.onImReady(isNsfw, isGif, categoryTag, orientation)
            }
        }
    } else if (serverMode == stringResource(R.string.server_enhanced_string)) {
        picsViewModel.onPicsReady(isNsfw, categoryTag)
    } else {
        bestViewModel.onBestReady(categoryTag)
    }
}

@Composable
private fun LaunchWaifuScreen(mode: ServerType) {
    when (mode) {
        NORMAL -> {} // WaifuImScreen()
        ENHANCED -> {} //  WaifuPicsScreen()
        NEKOS -> {} // WaifuNekosScreen()
        else -> {}
    }
}