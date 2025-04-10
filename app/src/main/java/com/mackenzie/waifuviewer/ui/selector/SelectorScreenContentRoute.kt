package com.mackenzie.waifuviewer.ui.selector

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.ENHANCED
import com.mackenzie.waifuviewer.domain.ServerType.NEKOS
import com.mackenzie.waifuviewer.domain.ServerType.NORMAL
import com.mackenzie.waifuviewer.domain.selector.SwitchState
import com.mackenzie.waifuviewer.ui.common.compareVersion
import com.mackenzie.waifuviewer.ui.common.getConfig
import com.mackenzie.waifuviewer.ui.common.getLatestVersion
import com.mackenzie.waifuviewer.ui.common.getServerModeOnly
import com.mackenzie.waifuviewer.ui.common.getServerType
import com.mackenzie.waifuviewer.ui.common.getServerTypeByMode
import com.mackenzie.waifuviewer.ui.common.getSimpleText
import com.mackenzie.waifuviewer.ui.common.isLandscape
import com.mackenzie.waifuviewer.ui.common.loadInitialServer
import com.mackenzie.waifuviewer.ui.common.saveServerType
import com.mackenzie.waifuviewer.ui.common.showToast
import com.mackenzie.waifuviewer.ui.common.tagFilter
import com.mackenzie.waifuviewer.ui.common.ui.MultiplePermissionRequestEffect
import com.mackenzie.waifuviewer.ui.common.ui.PermissionRequestEffect
import com.mackenzie.waifuviewer.ui.selector.ui.SelectorScreenContent
import com.mackenzie.waifuviewer.ui.selector.ui.rememberSelectorState
import kotlinx.coroutines.Dispatchers.IO

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

    val state = rememberSelectorState(switchState = switchState)

    val remote by remember { mutableStateOf(
        state.remoteValues.getConfig(context as Activity).apply {
            state.remoteValues.type = getServerType(context)
            state.remoteValues.mode = getServerModeOnly(context)
        }) }
    LaunchedEffect(IO) {
        remote.latestServerVersion = remote.getLatestVersion().latestServerVersion
    }

    var loadedServer by remember { mutableStateOf(getServerTypeByMode(remote.mode)) }
    var serverState by remember { mutableStateOf(remote.type ?: NORMAL) }

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
            remote.type = loadedServer

            remote.mode = 1
            remote.saveServerType(LocalActivity.current as Activity)
            LoadWaifuServer(loadedServer, vm) { state.isSelectorLoaded = true }
        }
    } else if (!state.isSelectorLoaded) {
        loadedServer = loadInitialServer()
        serverState = loadedServer
        // TODO redundante
        remote.type = loadedServer

        when (loadedServer) {
            NORMAL -> remote.mode = 0
            ENHANCED -> remote.mode = 1
            NEKOS -> remote.mode = 2
            else -> remote.mode = 0
        }
        remote.saveServerType(LocalActivity.current as Activity)
        LoadWaifuServer(loadedServer, vm) { state.isSelectorLoaded = true }
    }

    if(state.reqPermisions) {
        // PermissionRequestEffect(ACCESS_COARSE_LOCATION) { granted -> if (!granted) { getString(context, R.string.waifus_permissions_content).showToast(context) } }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            MultiplePermissionRequestEffect(
                listOf(POST_NOTIFICATIONS, ACCESS_COARSE_LOCATION)
            ) { permissionsMap -> handlePermissions(permissionsMap) }
        } else {
            // en este caso no se solicita permiso de de notificacion
            PermissionRequestEffect(ACCESS_COARSE_LOCATION) { granted -> if (!granted) { getString(context, R.string.waifus_permissions_content).showToast(context) } }
        }
    }

    remote.latestServerVersion?.let { latestVersion ->
        when (latestVersion.compareVersion()) {
            0 -> {
                Log.e("SelectorScreenContentRoute", "La version del Servidor es la misma que la local")
                Log.e("SelectorScreenContentRoute", "local Version=${BuildConfig.VERSION_NAME}")
                Log.e("SelectorScreenContentRoute", "Server Version=${remote.latestServerVersion}")
            }
            1 -> {
                Log.e("SelectorScreenContentRoute", "La version del Servidor es MENOR que la local")
                Log.e("SelectorScreenContentRoute", "local Version=${BuildConfig.VERSION_NAME}")
                Log.e("SelectorScreenContentRoute", "Server Version=${remote.latestServerVersion}")
            }
            -1 -> {
                Log.e("SelectorScreenContentRoute", "La version del Servidor es MAYOR que la local")
                Log.e("SelectorScreenContentRoute", "local Version=${BuildConfig.VERSION_NAME}")
                Log.e("SelectorScreenContentRoute", "Server Version=${remote.latestServerVersion}")
            }
        }
    }

    SelectorScreenContent(
        state = selectorState,
        onServerButtonClicked = {
            onSwitchChanged(SwitchState())
            when (remote.type) {
                NORMAL -> {
                    remote.type = ENHANCED
                    serverState = ENHANCED
                }
                ENHANCED -> {
                    remote.type = NEKOS
                    serverState = NEKOS
                }
                NEKOS -> {
                    remote.type = NORMAL
                    serverState = NORMAL
                }
                else -> {
                    "WTF=${remote.type}".showToast(context)
                }
            }
            remote.saveServerType(context as Activity)
        },
        onWaifuButtonClicked = { tag ->
            state.tag = tag.tagFilter(context, serverState, state.switchState)
            // "tag=${state.tag}".showToast(context)
            onWaifuButtonClicked(serverState.value, state.tag , state.switchState.nsfw, state.switchState.gifs, state.switchState.portrait)
        },
        onFavoriteClicked = onFavoriteButtonClicked,
        onRestartClicked = {
            vm.loadErrorOrWaifu(orientation = context.isLandscape(), serverType = loadedServer)
            Snackbar.make(view, "server=${loadedServer}", Snackbar.LENGTH_SHORT).show()
        },
        onGptClicked = { onGptButtonClicked(true) },
        onGeminiClicked = { onGptButtonClicked(false) },
        switchStateCallback = { stateCallback -> onSwitchChanged(stateCallback) },
        switchState = state.switchState,
        tags = state.tagsState,
        server = serverState
    )
}

private fun handlePermissions(permissionsMap: Map<String, Boolean>) {
    permissionsMap.forEach { (permiso, granted) ->
        if (!granted) {
            when (permiso) {
                // en caso denegado, no emitimos aviso
                POST_NOTIFICATIONS -> {} // { getString(context, R.string.waifus_permissions_push).showToast(context) }
                // en caso denegado, no emitimos aviso
                ACCESS_COARSE_LOCATION -> {} // { getString(context, R.string.waifus_permissions_location).showToast(context) }
                else -> {}
            }
        }
    }
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