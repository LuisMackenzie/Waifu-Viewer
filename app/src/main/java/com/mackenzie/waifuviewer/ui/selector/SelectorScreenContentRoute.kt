package com.mackenzie.waifuviewer.ui.selector

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
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
import com.mackenzie.waifuviewer.ui.common.ui.PermissionRequestEffect
import com.mackenzie.waifuviewer.ui.selector.ui.SelectorScreenContent
import com.mackenzie.waifuviewer.ui.selector.ui.rememberSelectorState
import java.text.Normalizer

@Composable
internal fun SelectorScreenContentRoute(
    vm: SelectorViewModel = hiltViewModel(),
    onWaifuButtonClicked: (String, String, Boolean, Boolean, Boolean) -> Unit = { _, _, _, _, _-> },
    onGptButtonClicked: (Boolean) -> Unit = {},
    onFavoriteButtonClicked: () -> Unit = {},
    onServerCleaned: () -> Unit = {},
    onSwitchChanged: (SwitchState) -> Unit = {},
    switchState: SwitchState = SwitchState(),
    serverToClean: ServerType? = null,
) {
    val selectorState by vm.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val view = LocalView.current
    val activity = LocalContext.current as Activity

    val state = rememberSelectorState(switchState = switchState)

    state.remoteValues.getConfig(context as Activity).apply {
        state.remoteValues = this
        state.remoteValues.type = getServerType(activity)
        state.remoteValues.mode = getServerModeOnly(activity)
    }

    var loadedServer by remember { mutableStateOf(getServerTypeByMode(state.remoteValues.mode)) }
    var serverState by remember { mutableStateOf(state.remoteValues.type ?: NORMAL) }

    serverToClean?.let {
        when(it) {
            NORMAL -> { getString(context, R.string.waifus_gone).showToast(context); vm.onClearImDatabase() }
            ENHANCED -> { getString(context, R.string.waifus_gone).showToast(context); vm.onClearPicsDatabase() }
            NEKOS -> { getString(context, R.string.waifus_gone).showToast(context); vm.onClearBestDatabase() }
            else -> {}
        }
        onServerCleaned()
    }

    if (BuildConfig.BUILD_TYPE == ENHANCED.value) {
        if (!state.isSelectorLoaded) {
            loadedServer = ENHANCED
            serverState = loadedServer
            // TODO redundante
            state.remoteValues.type = loadedServer

            state.remoteValues.mode = 1
            state.remoteValues.saveServerType(LocalContext.current as Activity)
            LoadWaifuServer(loadedServer, vm) { state.isSelectorLoaded = true }
        }
    } else if (!state.isSelectorLoaded) {
        loadedServer = loadInitialServer()
        serverState = loadedServer
        // TODO redundante
        state.remoteValues.type = loadedServer

        when (loadedServer) {
            NORMAL -> state.remoteValues.mode = 0
            ENHANCED -> state.remoteValues.mode = 1
            NEKOS -> state.remoteValues.mode = 2
            else -> state.remoteValues.mode = 0
        }
        state.remoteValues.saveServerType(LocalContext.current as Activity)
        LoadWaifuServer(loadedServer, vm) { state.isSelectorLoaded = true }
    }

    if(state.reqPermisions) {
        PermissionRequestEffect(ACCESS_COARSE_LOCATION) { granted -> if (!granted) { getString(context, R.string.waifus_permissions_content).showToast(context) } }
    }

    SelectorScreenContent(
        state = selectorState,
        onServerButtonClicked = {
            onSwitchChanged(SwitchState())
            when (serverState) {
                NORMAL -> {
                    state.remoteValues.type = ENHANCED
                    serverState = ENHANCED
                }
                ENHANCED -> {
                    state.remoteValues.type = NEKOS
                    serverState = NEKOS
                }
                NEKOS -> {
                    state.remoteValues.type = NORMAL
                    serverState = NORMAL
                }
                else -> {
                    "WTF=${serverState}".showToast(context)
                }
            }
            state.remoteValues.saveServerType(context as Activity)
        },
        onWaifuButtonClicked = { tag ->
            state.tag = tag.tagFilter(context, serverState, state.switchState)
            // "tag=${state.tag}".showToast(context)
            onWaifuButtonClicked(serverState.value, state.tag , state.switchState.nsfw, state.switchState.gifs, state.switchState.portrait)
        },
        onFavoriteClicked = onFavoriteButtonClicked,
        onRestartClicked = {
            vm.loadErrorOrWaifu(orientation = context.isLandscape(), serverType = loadedServer)
            Snackbar.make(view, "server=$loadedServer", Snackbar.LENGTH_SHORT).show()
        },
        onGptClicked = { onGptButtonClicked(true) },
        onGeminiClicked = { onGptButtonClicked(false) },
        switchStateCallback = { stateCallback -> onSwitchChanged(stateCallback) },
        switchState = state.switchState,
        tags = state.tagsState,
        server = serverState
    )
}

@Composable
private fun LoadWaifuServer(
    mode: ServerType,
    vm: SelectorViewModel,
    onLoaded: () -> Unit = {}
) {
    vm.loadErrorOrWaifu(
        orientation = LocalContext.current.isLandscape(),
        serverType = mode
    )
    vm.requestTags()
    val msg = stringResource(R.string.server_toast_holder)
    val newMsg = mode.value.getSimpleText().plus(" $msg")
    newMsg.showToast(LocalContext.current)
    onLoaded()
}