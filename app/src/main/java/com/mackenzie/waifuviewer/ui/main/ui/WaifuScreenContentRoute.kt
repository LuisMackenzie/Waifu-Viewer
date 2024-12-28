package com.mackenzie.waifuviewer.ui.main.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.getString
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
import com.mackenzie.waifuviewer.domain.getTypes
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.common.showToast
import com.mackenzie.waifuviewer.ui.main.WaifuBestViewModel
import com.mackenzie.waifuviewer.ui.main.WaifuImViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun WaifuScreenContentRoute(
    bundle: Bundle = bundleOf(),
    imViewModel: WaifuImViewModel = hiltViewModel(),
    picsViewModel : WaifuPicsViewModel = hiltViewModel(),
    bestViewModel: WaifuBestViewModel = hiltViewModel(),
    onNavigate: () -> Unit = {}
) {

    val serverMode by remember { mutableStateOf(bundle.getString(Constants.SERVER_MODE) ?: "") }
    var lmState by remember { mutableStateOf(LoadingState()) }
    val context = LocalContext.current
    val activity = LocalContext.current as Activity

    LoadCustomResult(bundle, serverMode, imViewModel, picsViewModel, bestViewModel)

    when (serverMode.getTypes()) {
        NORMAL -> {
            WaifuImScreenContent(
                state = imViewModel.state.collectAsStateWithLifecycle().value,
                onWaifuClicked = {}, // { mainState.onWaifuImClicked(it) },
                onRequestMore = {
                    onLoadMoreWaifusIm(bundle, context, lmState, imViewModel) { serverId ->
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(6000)
                            when (serverId) {
                                0 -> lmState.loadMoreIm = false
                                1 -> lmState.loadMorePics = false
                                2 -> lmState.loadMoreBest = false
                                else -> {lmState = LoadingState()}
                            }
                        }
                    }
                },
                onFabClick = {
                    "Eliminando la database.. .".showToast(context)
                    // imViewModel.onClearImDatabase()
                    // onBackPressedDispatcher?.onBackPressed()
                    // Toast.makeText(requireContext(), getString(R.string.waifus_gone), Toast.LENGTH_SHORT).show()
                }
            )
        }
        ENHANCED -> {
            WaifuPicsScreenContent(
                state = picsViewModel.state.collectAsStateWithLifecycle().value,
                onWaifuClicked = {}, // { mainState.onWaifuPicsClicked(it) },
                onRequestMore = {
                    onLoadMoreWaifusPics(bundle, context, lmState, picsViewModel) { serverId ->
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(6000)
                            when (serverId) {
                                0 -> lmState.loadMoreIm = false
                                1 -> lmState.loadMorePics = false
                                2 -> lmState.loadMoreBest = false
                                else -> {lmState = LoadingState()}
                            }
                        }
                    }
                },
                onFabClick = {
                    "Eliminando la database.. .".showToast(context)
                    // picsViewModel.onClearPicsDatabase()
                    // activity?.onBackPressedDispatcher?.onBackPressed()
                    // Toast.makeText(requireContext(), getString(R.string.waifus_gone), Toast.LENGTH_SHORT).show()
                }
            )
        }
        NEKOS -> {
            WaifuBestScreenContent(
                state = bestViewModel.state.collectAsStateWithLifecycle().value,
                onWaifuClicked = {}, // { mainState.onWaifuBestClicked(it) },
                onRequestMore = {
                    onLoadMoreWaifusBest(bundle, context, lmState, bestViewModel) { serverId ->
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(6000)
                            when (serverId) {
                                0 -> lmState.loadMoreIm = false
                                1 -> lmState.loadMorePics = false
                                2 -> lmState.loadMoreBest = false
                                else -> {lmState = LoadingState()}
                            }
                        }
                    }
                },
                onFabClick = {
                    "Eliminando la database.. .".showToast(context)
                    // bestViewModel.onClearDatabase()
                    // activity?.onBackPressedDispatcher?.onBackPressed()
                    // Toast.makeText(requireContext(), getString(R.string.waifus_gone), Toast.LENGTH_SHORT).show()
                }
            )
        }
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

private fun onLoadMoreWaifusIm(
    bun: Bundle,
    ctx: Context,
    lmState: LoadingState,
    imViewModel: WaifuImViewModel,
    onResetLoadingMore: (Int) -> Unit
) {
    val isNsfw = bun.getBoolean(Constants.IS_NSFW_WAIFU)
    val isGif = bun.getBoolean(Constants.IS_GIF_WAIFU)
    val orientation = bun.getBoolean(Constants.IS_LANDS_WAIFU)
    val categoryTag = bun.getString(Constants.CATEGORY_TAG_WAIFU) ?: ""
    if (!lmState.loadMoreIm) {
        imViewModel.onRequestMore(isNsfw, isGif, categoryTag , orientation)
        lmState.loadMoreIm = true
        getString(ctx, R.string.waifus_coming).showToast(ctx)
        onResetLoadingMore(0)
    }
}

private fun onLoadMoreWaifusPics(
    bun: Bundle,
    ctx: Context,
    lmState: LoadingState,
    picsViewModel: WaifuPicsViewModel,
    onResetLoadingMore: (Int) -> Unit
) {
    val isNsfw = bun.getBoolean(Constants.IS_NSFW_WAIFU)
    val categoryTag = bun.getString(Constants.CATEGORY_TAG_WAIFU) ?: ""
    if (!lmState.loadMorePics ) {
        picsViewModel.onRequestMore(isNsfw, categoryTag)
        lmState.loadMorePics = true
        getString(ctx, R.string.waifus_coming).showToast(ctx)
        onResetLoadingMore(1)
    }
}

private fun onLoadMoreWaifusBest(
    bun: Bundle,
    ctx: Context,
    lmState: LoadingState,
    bestViewModel: WaifuBestViewModel,
    onResetLoadingMore: (Int) -> Unit
) {
    val categoryTag = bun.getString(Constants.CATEGORY_TAG_WAIFU) ?: ""
    if (!lmState.loadMoreBest ) {
        bestViewModel.onRequestMore(categoryTag)
        lmState.loadMoreBest = true
        getString(ctx, R.string.waifus_coming).showToast(ctx)
        onResetLoadingMore(2)
    }
}