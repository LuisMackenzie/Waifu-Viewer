package com.mackenzie.waifuviewer.ui.selector.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.getString
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.snackbar.Snackbar
import com.mackenzie.waifuviewer.BuildConfig
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.RemoteConfigValues
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.ENHANCED
import com.mackenzie.waifuviewer.domain.ServerType.NEKOS
import com.mackenzie.waifuviewer.domain.ServerType.NORMAL
import com.mackenzie.waifuviewer.domain.selector.ImTags
import com.mackenzie.waifuviewer.domain.selector.NekosTags
import com.mackenzie.waifuviewer.domain.selector.PicsTags
import com.mackenzie.waifuviewer.domain.selector.SwitchState
import com.mackenzie.waifuviewer.domain.selector.TagsState
import com.mackenzie.waifuviewer.ui.common.getConfig
import com.mackenzie.waifuviewer.ui.common.getSimpleText
import com.mackenzie.waifuviewer.ui.common.isLandscape
import com.mackenzie.waifuviewer.ui.common.loadInitialServer
import com.mackenzie.waifuviewer.ui.common.saveBundle
import com.mackenzie.waifuviewer.ui.common.saveServerMode
import com.mackenzie.waifuviewer.ui.common.showToast
import com.mackenzie.waifuviewer.ui.common.tagFilter
import com.mackenzie.waifuviewer.ui.common.ui.isNavigationBarVisible
import com.mackenzie.waifuviewer.ui.common.ui.previewSelectorState
import com.mackenzie.waifuviewer.ui.selector.SelectorViewModel
import com.mackenzie.waifuviewer.ui.theme.Dimens
import com.mackenzie.waifuviewer.ui.theme.WaifuViewerTheme

@Composable
internal fun SelectorScreenContentRoute(
    vm: SelectorViewModel = hiltViewModel(),
    onWaifuButtonClicked: (String, Bundle) -> Unit = { tag, bundle -> },
) {
    val selectorState by vm.state.collectAsStateWithLifecycle()
    var loaded by rememberSaveable { mutableStateOf(false) }
    // var loaded2: Boolean = false
    val reqPermisions by remember { mutableStateOf(false) }
    var loadedServer by remember { mutableStateOf<ServerType?>(null) }
    var selectedTag by remember { mutableStateOf("") }
    val context = LocalContext.current
    val view = LocalView.current
    val activity = LocalContext.current as Activity
    var remoteValues by remember { mutableStateOf<RemoteConfigValues?>( null) }
    // Aqui se guardan 3 valores de los Switches en este orden 1. NSFW, 2. Gifs, 3. Portrait
    // val switchValues by remember { mutableStateOf(Triple(false, false, false)) }

    remoteValues = RemoteConfigValues().getConfig(activity)

    var switchState by remember { mutableStateOf(SwitchState()) }
    var serverState by remember { mutableStateOf(NORMAL) }
    val tagsState by remember { mutableStateOf(TagsState()) }

    if (BuildConfig.BUILD_TYPE == ENHANCED.value) {
        if (loadedServer == null && !loaded) {
            loadedServer = ENHANCED
            loadedServer?.let {
                serverState = it
                LoadWaifuServer(it, vm, loaded) { loaded = !loaded}
            }
        }
        Log.e("Selectorroute", "loaded=$loaded, server=$loadedServer")
    } else if (loadedServer == null && !loaded) {
        loadedServer = loadInitialServer()
        loadedServer?.let {
            serverState = it
            LoadWaifuServer(it, vm, loaded) { loaded = !loaded}
        }
        Log.e("Selectorroute", "loaded=$loaded, server=$loadedServer")
    }

    SelectorScreenContent(
        state = selectorState,
        onServerButtonClicked = {
            switchState = SwitchState()
            when (serverState) {
                NORMAL -> {
                    remoteValues?.type = ENHANCED
                    serverState = ENHANCED
                }
                ENHANCED -> {
                    remoteValues?.type = NEKOS
                    serverState = NEKOS
                }
                NEKOS -> {
                    remoteValues?.type = NORMAL
                    serverState = NORMAL
                }
                else -> {
                    "WTF=$serverState".showToast(context)
                }
            }
            remoteValues?.saveServerMode(context as Activity)
        },
        onWaifuButtonClicked = { tag ->
            selectedTag = tag.tagFilter(context, serverState, switchState)
            onWaifuButtonClicked(selectedTag, saveBundle(context, serverState, switchState, selectedTag))
        },
        onFavoriteClicked = {}, // {navigateTo(null, toFavorites = true)},
        onRestartClicked = {
            loadedServer?.let{ vm.loadErrorOrWaifu(orientation = context.isLandscape(), serverType = it) }
            Snackbar.make(view, "server=$loadedServer", Snackbar.LENGTH_SHORT).show()
        },
        onGptClicked = {}, //  {navigateTo(null, toGpt = true)},
        onGeminiClicked = {}, // {navigateTo(null, toGemini = true)},
        switchStateCallback = { stateCallback ->
            switchState = stateCallback
            // switchValues = switchState
        },
        switchState = switchState,
        tags = tagsState,
        backgroundState = { loaded = true },
        server = serverState
    )
}

@Composable
fun SelectorScreenContent(
    state : SelectorViewModel.UiState = previewSelectorState(),
    onServerButtonClicked: () -> Unit = {},
    onWaifuButtonClicked: (String) -> Unit = {},
    onFavoriteClicked: () -> Unit = {},
    onRestartClicked: () -> Unit = {},
    onGptClicked: () -> Unit = {},
    onGeminiClicked: () -> Unit = {},
    switchStateCallback: (SwitchState) -> Unit = {},
    switchState: SwitchState = SwitchState(),
    tags: TagsState = TagsState(),
    backgroundState: (Boolean) -> Unit = {},
    server: ServerType = NORMAL,
) {

    var tagsState by remember { mutableStateOf(tags) }

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
            BackgroundImage(waifu.url)
            backgroundState(true)
        }
        state.waifuPic?.let { waifu ->
            BackgroundImage(waifu.url)
            backgroundState(true)
        }
        state.waifuNeko?.let { waifu ->
            BackgroundImage(waifu.url)
            backgroundState(true)
        }

        state.tags?.let { imTags ->
            tagsState = TagsState(
                ImTags(imTags.versatile, imTags.nsfw),
                PicsTags(tagsState.enhanced.sfw, tagsState.enhanced.nsfw),
                NekosTags(tagsState.nekos.png, tagsState.nekos.gifs)
            )
        }

        state.error?.let {
            BackgroundImageError(R.drawable.ic_offline_background)
            Log.e("Selector State Error", "Error: $it")
        }

        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(Dimens.homeTitlePadding),
            text = stringResource(id = R.string.waifu_viewer),
            color = Color.White,
            fontSize = Dimens.homeTitleFontSize,
            style = MaterialTheme.typography.bodyMedium
        )
        SelectorMainButtons(
            tags = tagsState,
            onServerButtonClicked = onServerButtonClicked,
            onWaifuButtonClicked = onWaifuButtonClicked,
            switchState = switchState,
            server = server
        )

        SelectorFabFavorites(
            onFavoriteClicked = onFavoriteClicked,
            onRestartClicked = onRestartClicked,
            onGptClicked = onGptClicked,
            onGeminiClicked = onGeminiClicked
        )

        SelectorSwitches(
            modifier = if (LocalContext.current.isLandscape()) {
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(all = Dimens.homeSwitchesPadding)
                    .padding(end = Dimens.homeSwitchesPaddingEnd)
            } else {
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(all = Dimens.homeSwitchesPadding)
                    .padding(end = Dimens.homeSwitchesPaddingEnd)
            },
            switchStateCallback = switchStateCallback,
            switchState = switchState,
            server = server
        )
    }
}

@Composable
fun LoadWaifuServer(
    mode: ServerType,
    vm: SelectorViewModel,
    loaded: Boolean,
    onLoaded: () -> Unit = {}
) {

    val ctx = LocalContext.current

    if (!loaded) {
        vm.loadErrorOrWaifu(
            orientation = ctx.isLandscape(),
            serverType = mode
        )
        vm.requestTags()
        val msg = getString(ctx, R.string.server_toast_holder)
        val newMsg = mode.value.getSimpleText(ctx).plus(" $msg")
        newMsg.showToast(ctx)
        onLoaded()
    } else {
        "Server already loaded".showToast(ctx)
        Log.e("LoadWaifuServer", "Server already loaded")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSelector() {
    WaifuViewerTheme(darkTheme = false) {
        SelectorScreenContent()
    }
}