package com.mackenzie.waifuviewer.ui.selector

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.mackenzie.waifuviewer.BuildConfig
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.RemoteConfigValues
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.ENHANCED
import com.mackenzie.waifuviewer.domain.ServerType.NEKOS
import com.mackenzie.waifuviewer.domain.ServerType.NORMAL
import com.mackenzie.waifuviewer.domain.selector.SwitchState
import com.mackenzie.waifuviewer.domain.selector.TagsState
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.common.PermissionRequester
import com.mackenzie.waifuviewer.ui.common.composeView
import com.mackenzie.waifuviewer.ui.common.isLandscape
import com.mackenzie.waifuviewer.ui.common.showToast
import com.mackenzie.waifuviewer.ui.main.MainState
import com.mackenzie.waifuviewer.ui.selector.ui.SelectorScreenContent
import com.mackenzie.waifuviewer.ui.splash.SplashScreenRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectorFragment : Fragment() {

    private val vm: SelectorViewModel by viewModels()
    private var loaded: Boolean = false
    private var loadedServer: ServerType? = null
    private var requirePermissions: Boolean = false
    private lateinit var mainState: MainState
    private var selectedTag: String = ""
    // private var remoteValues : RemoteConfigValues = RemoteConfigValues()
    // Aqui se guardan 3 valores de los Switches en este orden 1. NSFW, 2. Gifs, 3. Portrait
    private var switchValues: SwitchState = SwitchState()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainState = MainState(
            requireContext(),
            viewLifecycleOwner.lifecycleScope,
            findNavController(),
            PermissionRequester(this , Manifest.permission.ACCESS_COARSE_LOCATION)
        )
        /// getRemoteConfig()

        /*if (BuildConfig.BUILD_TYPE == ENHANCED.value) {
            if (loadedServer == null) {
                loadedServer = ENHANCED
                loadedServer?.let { loadWaifu(requirePermissions, it) }
            }
        } else if (loadedServer == null) loadInitialServer()*/

        return composeView {
            // LaunchSelectorScreen()
            // SplashScreenRoute()
        }
    }

    @Composable
    private fun LaunchSelectorScreen() {
        // var switchState by remember { mutableStateOf(switchValues) }
        // var serverState by remember { mutableStateOf(remoteValues.type ?: NORMAL) }
        // val tagsState by remember { mutableStateOf(TagsState()) }

        /*SelectorScreenContent(
            state = vm.state.collectAsStateWithLifecycle().value,
            onServerButtonClicked = {
                switchState = SwitchState()
                switchValues = switchState
                when (serverState) {
                    NORMAL -> {
                        remoteValues.type = ENHANCED
                        serverState = ENHANCED
                        // saveServerMode()
                    }
                    ENHANCED -> {
                        remoteValues.type = NEKOS
                        serverState = NEKOS
                        // saveServerMode()
                    }
                    NEKOS -> {
                        remoteValues.type = NORMAL
                        serverState = NORMAL
                        // saveServerMode()
                    }
                    else -> {
                        "WTF=$serverState".showToast(requireContext())
                    }
                }
            },
            onWaifuButtonClicked = {  tag -> selectedTag = tag ; navigateTo(serverState) },
            onFavoriteClicked = {navigateTo(null, toFavorites = true)},
            onRestartClicked = {
                loadedServer?.let{ vm.loadErrorOrWaifu(orientation = requireContext().isLandscape(), serverType = it) }
                Snackbar.make(requireView(), "server=$loadedServer", Snackbar.LENGTH_SHORT).show()
            },
            onGptClicked = {navigateTo(null, toGpt = true)},
            onGeminiClicked = {navigateTo(null, toGemini = true)},
            switchStateCallback = { stateCallback ->
                switchState = stateCallback
                switchValues = switchState
            },
            switchState = switchState,
            tags = tagsState,
            backgroundState = {}, // { backgroundLoaded() },
            server = serverState
        )*/
    }

    private fun navigateTo(mode: ServerType?, toFavorites: Boolean= false, toGpt: Boolean= false, toGemini: Boolean= false) {
        mode?.let { remoteValues.type = it }
        remoteValues.isFavorite = toFavorites
        val bun = saveBundle(mode)
        if (toFavorites) {
            mainState.onButtonFavoritesClicked(bun)
        } else if (toGpt) {
            mainState.onButtonGptClicked()
        } else if (toGemini) {
            mainState.onButtonGeminiClicked()
        } else {
            mainState.onButtonGetWaifuClicked(bun)
        }
    }

    private fun saveBundle(mode: ServerType?): Bundle {
        val bun = bundleOf()
        bun.putString(Constants.SERVER_MODE, mode?.value)
        bun.putBoolean(Constants.IS_NSFW_WAIFU, switchValues.nsfw)
        bun.putBoolean(Constants.IS_GIF_WAIFU, switchValues.gifs)
        bun.putBoolean(Constants.IS_LANDS_WAIFU, switchValues.portrait)
        bun.putString(Constants.CATEGORY_TAG_WAIFU, tagFilter(selectedTag))
        // saveServerMode()
        Log.v("saveBundle", "mode=${mode?.value} SERVER_MODE=${remoteValues.type?.value}")
        return bun
    }

    private fun tagFilter(tag: String): String {
        var updatedTag: String = tag
        if (tag == getString(R.string.categories) || tag == getString(R.string.categories_items)) {
            when (remoteValues.type) {
                NORMAL, ENHANCED -> {
                    updatedTag = getString(R.string.tag_waifu)
                }
                else -> {
                    if (!switchValues.gifs) {
                        updatedTag = getString(R.string.tag_neko)
                    } else {
                        updatedTag = getString(R.string.tag_pat)
                    }
                }
            }
        }
        return updatedTag
    }

    /*private fun loadInitialServer() {
        when (Build.VERSION.SDK_INT) {
            in 0..Build.VERSION_CODES.LOLLIPOP_MR1 -> {
                // Android 9 Hacia Abajo
                loadWaifu(requirePermissions, ENHANCED).apply { loadedServer = ENHANCED }
            }
            in 35..40 -> {
                // Android 15 Hacia Arriba
                loadWaifu(requirePermissions, NEKOS).apply { loadedServer = NEKOS }
            }
            else -> { loadWaifu(requirePermissions, NORMAL).apply { loadedServer = NORMAL } }
        }
    }*/

    /*private fun loadWaifu(reqPermisions: Boolean, serverType: ServerType) {
        remoteValues.type = serverType
        if (reqPermisions) {
            mainState.requestPermissionLauncher {
                if (!loaded) {
                    vm.loadErrorOrWaifu(
                        orientation = requireContext().isLandscape(),
                        serverType = serverType
                    )
                    vm.requestTags()
                    Toast.makeText(requireContext(), getString(R.string.server_toast_holder, getSimpleText(serverType.value)), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            if (!loaded) {
                vm.loadErrorOrWaifu(
                    orientation = requireContext().isLandscape(),
                    serverType = serverType
                )
                vm.requestTags()
                Toast.makeText(requireContext(), getString(R.string.server_toast_holder, getSimpleText(serverType.value)), Toast.LENGTH_SHORT).show()
            }
        }
    }*/

    /*private fun backgroundLoaded() {
        loaded = true
    }*/

    /*private fun getSimpleText(type: String): String {
        return when (type) {
            NORMAL.value -> getString(R.string.server_normal_toast)
            ENHANCED.value -> getString(R.string.server_enhanced_toast)
            NEKOS.value -> getString(R.string.server_best_toast)
            else -> getString(R.string.server_unknown_toast)
        }
    }*/
}