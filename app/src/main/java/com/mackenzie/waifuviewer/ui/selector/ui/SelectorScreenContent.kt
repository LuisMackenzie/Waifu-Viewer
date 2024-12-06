package com.mackenzie.waifuviewer.ui.selector.ui

import android.app.Activity
import android.util.Log
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.mackenzie.waifuviewer.BuildConfig
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.RemoteConfigValues
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.ENHANCED
import com.mackenzie.waifuviewer.domain.ServerType.NORMAL
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.common.GenerativeViewModelFactory
import com.mackenzie.waifuviewer.ui.common.app
import com.mackenzie.waifuviewer.ui.common.getConfig
import com.mackenzie.waifuviewer.ui.common.isLandscape
import com.mackenzie.waifuviewer.ui.common.ui.previewSelectorState
import com.mackenzie.waifuviewer.ui.gemini.resume.SummarizeWaifuScreen
import com.mackenzie.waifuviewer.ui.gemini.resume.SummarizeWaifuViewModel
import com.mackenzie.waifuviewer.ui.main.MainState
import com.mackenzie.waifuviewer.ui.selector.SelectorViewModel
import com.mackenzie.waifuviewer.ui.theme.Dimens
import com.mackenzie.waifuviewer.ui.theme.WaifuViewerTheme

@Composable
internal fun SelectorScreenContentRoute(
    vm: SelectorViewModel = viewModel()
) {
    val selectorState by vm.state.collectAsStateWithLifecycle()
    val loaded by remember { mutableStateOf(false) }
    var loadedServer by remember { mutableStateOf<ServerType?>(null) }
    val requirePermissions by remember { mutableStateOf(false) }
    // val mainState by remember { mutableStateOf<MainState?>(null) }
    val selectedTag by remember { mutableStateOf("") }
    val remoteValues by remember { mutableStateOf(RemoteConfigValues()) }
    // Aqui se guardan 3 valores de los Switches en este orden 1. NSFW, 2. Gifs, 3. Portrait
    val switchValues by remember { mutableStateOf(Triple(false, false, false)) }

    if (BuildConfig.BUILD_TYPE == ENHANCED.value) {
        if (loadedServer == null) {
            loadedServer = ENHANCED
            loadedServer?.let {
                // loadWaifu(requirePermissions, it)
            }
        }
    } else if (loadedServer == null) // loadInitialServer()

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
        switchState = Triple(false, false, false),
        tags = Triple(Pair(arrayOf(), arrayOf()), Pair(arrayOf(), arrayOf()), Pair(arrayOf(), arrayOf())),
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
    switchStateCallback: (Triple<Boolean, Boolean, Boolean>) -> Unit = {},
    switchState: Triple<Boolean, Boolean, Boolean> = Triple(false, false, false),
    tags: Triple<Pair<Array<String>, Array<String>>, Pair<Array<String>, Array<String>>, Pair<Array<String>, Array<String>>> = Triple(Pair(arrayOf(), arrayOf()), Pair(arrayOf(), arrayOf()), Pair(arrayOf(), arrayOf())),
    backgroundState: (Boolean) -> Unit = {},
    server: ServerType = NORMAL,
) {

    var tagsState by remember { mutableStateOf(tags) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
            tagsState = Triple(
                Pair(imTags.versatile.toTypedArray(), imTags.nsfw.toTypedArray()),
                Pair(tagsState.second.first, tagsState.second.second),
                Pair(tagsState.third.first, tagsState.third.second)
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