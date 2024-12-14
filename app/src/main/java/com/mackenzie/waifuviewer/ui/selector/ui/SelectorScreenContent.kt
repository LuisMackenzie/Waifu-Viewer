package com.mackenzie.waifuviewer.ui.selector.ui

import android.util.Log
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.NORMAL
import com.mackenzie.waifuviewer.domain.selector.ImTags
import com.mackenzie.waifuviewer.domain.selector.NekosTags
import com.mackenzie.waifuviewer.domain.selector.PicsTags
import com.mackenzie.waifuviewer.domain.selector.SwitchState
import com.mackenzie.waifuviewer.domain.selector.TagsState
import com.mackenzie.waifuviewer.ui.common.isLandscape
import com.mackenzie.waifuviewer.ui.common.ui.isNavigationBarVisible
import com.mackenzie.waifuviewer.ui.common.ui.previewSelectorState
import com.mackenzie.waifuviewer.ui.selector.SelectorViewModel
import com.mackenzie.waifuviewer.ui.theme.Dimens
import com.mackenzie.waifuviewer.ui.theme.WaifuViewerTheme

@Composable
internal fun SelectorScreenContentRoute(
    vm: SelectorViewModel = viewModel()
) {
    val selectorState by vm.state.collectAsStateWithLifecycle()
    // TODO Changes for navigation
    // TODO changes for bypass fragment
    // val loaded by remember { mutableStateOf(false) }
    // var loadedServer by remember { mutableStateOf<ServerType?>(null) }
    // val requirePermissions by remember { mutableStateOf(false) }
    // val mainState by remember { mutableStateOf<MainState?>(null) }
    // val selectedTag by remember { mutableStateOf("") }
    // val remoteValues by remember { mutableStateOf(RemoteConfigValues()) }
    // Aqui se guardan 3 valores de los Switches en este orden 1. NSFW, 2. Gifs, 3. Portrait
    // val switchValues by remember { mutableStateOf(Triple(false, false, false)) }

    /*if (BuildConfig.BUILD_TYPE == ENHANCED.value) {
        if (loadedServer == null) {
            loadedServer = ENHANCED
            loadedServer?.let {
                loadWaifu(requirePermissions, it)
            }
        }
    } else if (loadedServer == null) loadInitialServer()*/

    // remoteValues.getConfig(Activity())

    SelectorScreenContent(
        state = selectorState,
        onServerButtonClicked = {},
        onWaifuButtonClicked = {},
        onFavoriteClicked = {},
        onRestartClicked = {},
        onGptClicked = {},
        onGeminiClicked = {},
        switchStateCallback = {},
        switchState = SwitchState(),
        tags = TagsState(),
        backgroundState = {},
        server = NORMAL
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
            server = server
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSelector() {
    WaifuViewerTheme(darkTheme = false) {
        SelectorScreenContent()
    }
}