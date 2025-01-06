package com.mackenzie.waifuviewer.ui.selector.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.mackenzie.waifuviewer.domain.RemoteConfigValues
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.selector.SwitchState
import com.mackenzie.waifuviewer.domain.selector.TagsState


@Composable
fun rememberSelectorState(
    // scope: CoroutineScope = rememberCoroutineScope(),
    isSelectorBgLoaded: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    selectedTag: MutableState<String> = remember { mutableStateOf("") },
    reqPermisions : MutableState<Boolean> = remember { mutableStateOf(false) },
    remoteConfigValues: MutableState<RemoteConfigValues> = remember { mutableStateOf(RemoteConfigValues()) },
    switchState: SwitchState,
    tagsState: MutableState<TagsState> = remember { mutableStateOf(TagsState()) },
    // serverState: MutableState<ServerType> = remember {  mutableStateOf<ServerType>(ServerType.NORMAL) },
    // serverMode: Int = remember { remoteValues.mode },
): SelectorState = remember(isSelectorBgLoaded, selectedTag, switchState, tagsState) {
    SelectorState(
        // scope = scope,
        isSelectorBgLoaded = isSelectorBgLoaded,
        selectedTag = selectedTag,
        reqPermisions = reqPermisions.value,
        remoteInitValues = remoteConfigValues,
        switchState = switchState,
        tagsInitState = tagsState,
        // serverInitState = mutableStateOf(remoteConfigValues.value.type),
        // serverInitState2 = remoteConfigValues.value.type,
        // serverMode = serverMode,
        // type = remoteValues.type,
        // mode = remoteValues.mode,
    )
}

class SelectorState(
    // val scope: CoroutineScope,
    val isSelectorBgLoaded: MutableState<Boolean>,
    val selectedTag: MutableState<String>,
    val reqPermisions: Boolean,
    val remoteInitValues: MutableState<RemoteConfigValues>,
    val switchState: SwitchState,
    val tagsInitState: MutableState<TagsState>,
    // val serverInitState: MutableState<ServerType?>,
    // var serverInitState2: ServerType?,
    // var serverMode: Int,
    // val type: ServerType?,
    // val mode: Int,
) {

    var isSelectorLoaded by isSelectorBgLoaded
    var tag by selectedTag

    var remoteValues : RemoteConfigValues
        get() = remoteInitValues.value
        set(value) { remoteInitValues.value = value }

    /*var serverState: ServerType
        get() = serverInitState ?: ServerType.NORMAL
        set(value) { serverInitState = value }*/


    // var loadedServer by remember { mutableStateOf(getServerTypeByMode(remoteValues.mode)) }

    val tagsState by tagsInitState



    // var serverState by serverInitState

    /*var serverState2 : ServerType?
        get() = serverInitState2
        set(value) { serverInitState2 = value }*/


    /*val switchState : SwitchState
        @Composable
        get() = switchInitState.value*/
        // set(value) { switchInitState.value = value }



    /*val remote: RemoteConfigValues
        @Composable
        get() = remoteValues.getConfig(LocalContext.current as Activity).apply {
            type = getServerType(LocalContext.current as Activity)
            mode = getServerModeOnly(LocalContext.current as Activity)
        }*/

    /*val remoteState : RemoteConfigValues
        @Composable
        get() = remote*/

    // val currentType : ServerType = this.type ?: ServerType.NORMAL

    // val serverState: MutableState<ServerType>
        // @Composable
        // get() = remember { mutableStateOf(remote.type ?: ServerType.NORMAL) } // mutableStateOf(remote.type ?: ServerType.NORMAL)
        // set(value) { remoteValues.type = value }
        // get() = remoteState.type ?: ServerType.NORMAL
        // get() = remoteValues.type ?: ServerType.NORMAL
        // set(value) = value.saveServerType(LocalContext.current as Activity)


    /*val loadedServer: ServerType
        get() = getServerTypeByMode(remoteValues.mode)*/

}