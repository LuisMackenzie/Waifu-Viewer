package com.mackenzie.waifuviewer.ui.main

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.getString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.WaifuPicsViewModel
import com.mackenzie.waifuviewer.domain.LoadingState
import com.mackenzie.waifuviewer.domain.ServerType.ENHANCED
import com.mackenzie.waifuviewer.domain.ServerType.NEKOS
import com.mackenzie.waifuviewer.domain.ServerType.NORMAL
import com.mackenzie.waifuviewer.domain.getTypes
import com.mackenzie.waifuviewer.domain.selector.SwitchState
import com.mackenzie.waifuviewer.ui.common.showToast
import com.mackenzie.waifuviewer.ui.main.ui.WaifuBestScreenContent
import com.mackenzie.waifuviewer.ui.main.ui.WaifuImScreenContent
import com.mackenzie.waifuviewer.ui.main.ui.WaifuPicsScreenContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun WaifuScreenContentRoute(
    server: String,
    tag: String,
    switchState: SwitchState,
    imViewModel: WaifuImViewModel = hiltViewModel(),
    picsViewModel : WaifuPicsViewModel = hiltViewModel(),
    bestViewModel: WaifuBestViewModel = hiltViewModel(),
    onNavigate: (Int) -> Unit = {}
) {

    var lmState by remember { mutableStateOf(LoadingState()) }
    val context = LocalContext.current

    LoadCustomResult(server, tag, switchState, imViewModel, picsViewModel, bestViewModel)

    when (server.getTypes()) {
        NORMAL -> {
            WaifuImScreenContent(
                state = imViewModel.state.collectAsStateWithLifecycle().value,
                onWaifuClicked = { onNavigate(it.id) },
                onRequestMore = {
                    onLoadMoreWaifusIm(tag, switchState, context, lmState, imViewModel) { serverId ->
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
                    imViewModel.onClearImDatabase()
                    (context as Activity).onBackPressed()
                    getString(context, R.string.waifus_gone).showToast(context)
                }
            )
        }
        ENHANCED -> {
            WaifuPicsScreenContent(
                state = picsViewModel.state.collectAsStateWithLifecycle().value,
                onWaifuClicked = { onNavigate(it.id) }, // { mainState.onWaifuPicsClicked(it) },
                onRequestMore = {
                    onLoadMoreWaifusPics(tag, switchState, context, lmState, picsViewModel) { serverId ->
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
                    picsViewModel.onClearPicsDatabase()
                    (context as Activity).onBackPressed()
                    getString(context, R.string.waifus_gone).showToast(context)
                }
            )
        }
        NEKOS -> {
            WaifuBestScreenContent(
                state = bestViewModel.state.collectAsStateWithLifecycle().value,
                onWaifuClicked = { onNavigate(it.id) }, // { mainState.onWaifuBestClicked(it) },
                onRequestMore = {
                    onLoadMoreWaifusBest(tag, context, lmState, bestViewModel) { serverId ->
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
                    bestViewModel.onClearDatabase()
                    (context as Activity).onBackPressed()
                    getString(context, R.string.waifus_gone).showToast(context)
                }
            )
        }
        else -> {}
    }
}

@Composable
private fun LoadCustomResult(
    serverMode: String,
    tag: String,
    switchState: SwitchState,
    imViewModel: WaifuImViewModel,
    picsViewModel: WaifuPicsViewModel,
    bestViewModel: WaifuBestViewModel
) {

    if (serverMode == stringResource(R.string.server_normal_string)) {
        when (tag) {
            "uniform", "maid", "marin-kitagawa", "oppai" -> {
                imViewModel.onImReady(switchState.nsfw, isGif = false, tag, switchState.portrait)
            }
            "mori-calliope", "raiden-shogun" -> {
                imViewModel.onImReady(switchState.nsfw, isGif = false, tag, false)
            }
            else -> {
                imViewModel.onImReady(switchState.nsfw, switchState.gifs, tag, switchState.portrait)
            }
        }
    } else if (serverMode == stringResource(R.string.server_enhanced_string)) {
        picsViewModel.onPicsReady(switchState.nsfw, tag)
    } else {
        bestViewModel.onBestReady(tag)
    }
}

private fun onLoadMoreWaifusIm(
    tag: String,
    switchState: SwitchState,
    ctx: Context,
    lmState: LoadingState,
    imViewModel: WaifuImViewModel,
    onResetLoadingMore: (Int) -> Unit
) {
    if (!lmState.loadMoreIm) {
        // imViewModel.onRequestMore(switchState.nsfw, switchState.gifs, tag , switchState.portrait)
        lmState.loadMoreIm = true
        getString(ctx, R.string.waifus_coming).showToast(ctx)
        onResetLoadingMore(0)
    }
}

private fun onLoadMoreWaifusPics(
    tag: String,
    switchState: SwitchState,
    ctx: Context,
    lmState: LoadingState,
    picsViewModel: WaifuPicsViewModel,
    onResetLoadingMore: (Int) -> Unit
) {
    if (!lmState.loadMorePics ) {
        // picsViewModel.onRequestMore(switchState.nsfw, tag)
        lmState.loadMorePics = true
        getString(ctx, R.string.waifus_coming).showToast(ctx)
        onResetLoadingMore(1)
    }
}

private fun onLoadMoreWaifusBest(
    tag: String,
    ctx: Context,
    lmState: LoadingState,
    bestViewModel: WaifuBestViewModel,
    onResetLoadingMore: (Int) -> Unit
) {
    if (!lmState.loadMoreBest ) {
        // bestViewModel.onRequestMore(tag)
        lmState.loadMoreBest = true
        getString(ctx, R.string.waifus_coming).showToast(ctx)
        onResetLoadingMore(2)
    }
}