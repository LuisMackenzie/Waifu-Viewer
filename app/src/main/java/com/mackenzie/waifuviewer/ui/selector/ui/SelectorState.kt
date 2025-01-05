package com.mackenzie.waifuviewer.ui.selector.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.mackenzie.waifuviewer.domain.RemoteConfigValues
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.selector.SwitchState
import com.mackenzie.waifuviewer.domain.selector.TagsState
import com.mackenzie.waifuviewer.ui.common.getConfig
import com.mackenzie.waifuviewer.ui.common.getServerModeOnly
import com.mackenzie.waifuviewer.ui.common.getServerType
import com.mackenzie.waifuviewer.ui.common.getServerTypeByMode
import com.mackenzie.waifuviewer.ui.common.saveServerType
import kotlinx.coroutines.CoroutineScope


@Composable
fun rememberSelectorState(
    scope: CoroutineScope = rememberCoroutineScope(),
    isSelectorBgLoaded: Boolean = rememberSaveable { false },
    selectedTag: String = remember { "" },
    reqPermisions : Boolean = remember { false },
    remoteValues: RemoteConfigValues = remember { RemoteConfigValues() },
    switchState: SwitchState = remember { SwitchState() },
    tagsState: TagsState = remember { TagsState() },
    serverState: ServerType = remember {  ServerType.NORMAL },
    serverMode: Int = remember { remoteValues.mode },
): SelectorState = remember(scope, isSelectorBgLoaded, selectedTag, remoteValues, switchState, tagsState, serverState, serverMode) {
    SelectorState(
        scope = scope,
        isSelectorBgLoaded = isSelectorBgLoaded,
        selectedTag = selectedTag,
        reqPermisions = reqPermisions,
        remoteValues = remoteValues,
        switchState = switchState,
        tagsState = tagsState,
        serverState = serverState,
        serverMode = serverMode,
        // type = remoteValues.type,
        // mode = remoteValues.mode,
    )
}

class SelectorState(
    val scope: CoroutineScope,
    var isSelectorBgLoaded: Boolean,
    var selectedTag: String,
    val reqPermisions: Boolean,
    val remoteValues: RemoteConfigValues,
    var switchState: SwitchState,
    val tagsState: TagsState,
    var serverState: ServerType,
    var serverMode: Int,
    // val type: ServerType?,
    // val mode: Int,
) {


    val remote: RemoteConfigValues
        @Composable
        get() = remoteValues.getConfig(LocalContext.current as Activity).apply {
            type = getServerType(LocalContext.current as Activity)
            mode = getServerModeOnly(LocalContext.current as Activity)
        }

    val remoteState : RemoteConfigValues
        @Composable
        get() = remote

    // val currentType : ServerType = this.type ?: ServerType.NORMAL

    // val serverState: MutableState<ServerType>
        // @Composable
        // get() = remember { mutableStateOf(remote.type ?: ServerType.NORMAL) } // mutableStateOf(remote.type ?: ServerType.NORMAL)
        // set(value) { remoteValues.type = value }
        // get() = remoteState.type ?: ServerType.NORMAL
        // get() = remoteValues.type ?: ServerType.NORMAL
        // set(value) = value.saveServerType(LocalContext.current as Activity)


    val loadedServer: ServerType
        get() = getServerTypeByMode(remoteValues.mode)

}