package com.mackenzie.waifuviewer.ui.selector

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.content.ContextCompat.getString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.material.snackbar.Snackbar
import com.mackenzie.waifuviewer.BuildConfig
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.RemoteConfigValues
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.ENHANCED
import com.mackenzie.waifuviewer.domain.ServerType.NEKOS
import com.mackenzie.waifuviewer.domain.ServerType.NORMAL
import com.mackenzie.waifuviewer.domain.selector.SwitchState
import com.mackenzie.waifuviewer.domain.selector.TagsState
import com.mackenzie.waifuviewer.ui.common.getConfig
import com.mackenzie.waifuviewer.ui.common.getServerModeOnly
import com.mackenzie.waifuviewer.ui.common.getServerType
import com.mackenzie.waifuviewer.ui.common.getServerTypeByMode
import com.mackenzie.waifuviewer.ui.common.getSimpleText
import com.mackenzie.waifuviewer.ui.common.isLandscape
import com.mackenzie.waifuviewer.ui.common.loadInitialServer
import com.mackenzie.waifuviewer.ui.common.saveServerType
import com.mackenzie.waifuviewer.ui.common.showToast
import com.mackenzie.waifuviewer.ui.common.tagFilter
import com.mackenzie.waifuviewer.ui.selector.ui.SelectorScreenContent

@Composable
internal fun SelectorScreenContentRoute(
    vm: SelectorViewModel = hiltViewModel(),
    onWaifuButtonClicked: (String, String, Boolean, Boolean, Boolean) -> Unit = { _, _, _, _, _-> },
    onGptButtonClicked: (Boolean) -> Unit = {},
    onFavoriteButtonClicked: () -> Unit = {}
) {
    val selectorState by vm.state.collectAsStateWithLifecycle()
    var loaded by rememberSaveable { mutableStateOf(false) }
    // val reqPermisions by remember { mutableStateOf(false) }
    var selectedTag by remember { mutableStateOf("") }
    val context = LocalContext.current
    val view = LocalView.current
    // val activity = LocalContext.current as Activity
    val remoteValues by remember { mutableStateOf( RemoteConfigValues().getConfig(context as Activity)) }
    remoteValues.type = getServerType(context as Activity)
    remoteValues.mode = getServerModeOnly(context as Activity)

    var switchState by remember { mutableStateOf(SwitchState()) }
    var loadedServer by remember { mutableStateOf(getServerTypeByMode(remoteValues.mode)) }
    var serverState by remember { mutableStateOf(remoteValues.type ?: NORMAL) }
    val tagsState by remember { mutableStateOf(TagsState()) }

    if (BuildConfig.BUILD_TYPE == ENHANCED.value) {
        if (!loaded) {
            loadedServer = ENHANCED
            serverState = loadedServer
            remoteValues.type = loadedServer
            remoteValues.mode = 1
            LoadWaifuServer(loadedServer, vm, loaded) { loaded = !loaded}
        }
    } else if (!loaded) {
        loadedServer = loadInitialServer()
        serverState = loadedServer
        remoteValues.type = loadedServer
        when (loadedServer) {
            NORMAL -> remoteValues.mode = 0
            ENHANCED -> remoteValues.mode = 1
            NEKOS -> remoteValues.mode = 2
            else -> remoteValues.mode = 0
        }
        LoadWaifuServer(loadedServer, vm, loaded) { loaded = !loaded}
    }

    remoteValues.saveServerType(context as Activity)

    SelectorScreenContent(
        state = selectorState,
        onServerButtonClicked = {
            switchState = SwitchState()
            when (serverState) {
                NORMAL -> {
                    remoteValues.type = ENHANCED
                    serverState = ENHANCED
                }
                ENHANCED -> {
                    remoteValues.type = NEKOS
                    serverState = NEKOS
                }
                NEKOS -> {
                    remoteValues.type = NORMAL
                    serverState = NORMAL
                }
                else -> {
                    "WTF=$serverState".showToast(context)
                }
            }
            remoteValues.saveServerType(context as Activity)
        },
        onWaifuButtonClicked = { tag ->
            selectedTag = tag.tagFilter(context, serverState, switchState)
            // onWaifuButtonClicked(selectedTag, saveBundle(context, serverState, switchState, selectedTag))
            onWaifuButtonClicked(serverState.value, selectedTag, switchState.nsfw, switchState.gifs, switchState.portrait)
        },
        onFavoriteClicked = onFavoriteButtonClicked, // {navigateTo(null, toFavorites = true)},
        onRestartClicked = {
            vm.loadErrorOrWaifu(orientation = context.isLandscape(), serverType = loadedServer)
            Snackbar.make(view, "server=$loadedServer", Snackbar.LENGTH_SHORT).show()
        },
        onGptClicked = { onGptButtonClicked(true) }, //  {navigateTo(null, toGpt = true)},
        onGeminiClicked = { onGptButtonClicked(false) }, // {navigateTo(null, toGemini = true)},
        switchStateCallback = { stateCallback ->
            switchState = stateCallback
            // switchValues = switchState
        },
        switchState = switchState,
        tags = tagsState,
        // backgroundState = { loaded = !loaded },
        server = serverState
    )
}

@Composable
private fun LoadWaifuServer(
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