package com.mackenzie.waifuviewer.ui.selector

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.core.os.bundleOf
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.mackenzie.waifuviewer.BuildConfig
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.databinding.FragmentSelectorBinding
import com.mackenzie.waifuviewer.domain.RemoteConfigValues
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.ENHANCED
import com.mackenzie.waifuviewer.domain.ServerType.FAVORITE
import com.mackenzie.waifuviewer.domain.ServerType.NEKOS
import com.mackenzie.waifuviewer.domain.ServerType.NORMAL
import com.mackenzie.waifuviewer.domain.im.WaifuImTagList
import com.mackenzie.waifuviewer.ui.common.*
import com.mackenzie.waifuviewer.ui.main.MainState
import com.mackenzie.waifuviewer.ui.main.ui.MainTheme
import com.mackenzie.waifuviewer.ui.selector.ui.SelectorScreenContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectorFragment : Fragment(R.layout.fragment_selector) {

    private val vm: SelectorViewModel by viewModels()
    private var loaded: Boolean = false
    private var loadedServer: ServerType? = null
    private var requirePermissions: Boolean = false
    private lateinit var mainState: MainState
    private var selectedTag: String = ""
    private var remoteValues : RemoteConfigValues = RemoteConfigValues()
    // Aqui se guardan 3 valores de los Switches en este orden 1. NSFW, 2. Gifs, 3. Portrait
    private var switchValues: Triple<Boolean, Boolean, Boolean> = Triple(false, false, false)

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
        getRemoteConfig()

        if (BuildConfig.BUILD_TYPE == ENHANCED.value) {
            if (loadedServer == null) {
                loadedServer = ENHANCED
                loadedServer?.let { loadWaifu(requirePermissions, it) }
            }
        } else if (loadedServer == null) loadInitialServer()

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MainTheme {
                    LaunchSelectorScreen()
                }
            }
        }
    }

    @Composable
    private fun LaunchSelectorScreen() {
        var switchState by remember { mutableStateOf(Triple(false, false, false)) }
        var serverState by remember { mutableStateOf(remoteValues.type ?: NORMAL) }
        val tagsState by remember { mutableStateOf(Triple(
            Pair(Constants.NORMALSFW, Constants.NORMALNSFW),
            Pair(Constants.ENHANCEDSFW, Constants.ENHANCEDNSFW),
            Pair(Constants.NEKOSPNG, Constants.NEKOSGIF)
        )) }

        SelectorScreenContent(
            state = vm.state.collectAsStateWithLifecycle().value,
            onServerButtonClicked = {
                when (serverState) {
                    NORMAL -> {
                        remoteValues.type = ENHANCED
                        serverState = ENHANCED
                        saveServerMode()
                    }
                    ENHANCED -> {
                        remoteValues.type = NEKOS
                        serverState = NEKOS
                        saveServerMode()
                    }
                    NEKOS -> {
                        remoteValues.type = NORMAL
                        serverState = NORMAL
                        saveServerMode()
                    }
                    FAVORITE -> {
                        // remoteValues.type = serverState
                        "WTF2=$serverState".showToast(requireContext())
                        Log.e("SelectorFragment", "server=$serverState, remmote=${remoteValues.type}")
                    }
                    else -> {
                        "WTF=$serverState".showToast(requireContext())
                    }
                }
            },
            onWaifuButtonClicked = {  tag -> selectedTag = tag ;navigateTo(serverState) },
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
            backgroundState = { backgroundLoaded() },
            server = serverState
        )
    }

    private fun getRemoteConfig() {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = Constants.RELEASEINTERVALINSECONDS
            // minimumFetchIntervalInSeconds = Constants.DEBUGINTERVALINSECONDS
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                remoteValues = RemoteConfigValues(
                    remoteConfig.getBoolean("nsfw_mode"),
                    remoteConfig.getBoolean("waifu_gpt_service"),
                    remoteConfig.getBoolean("waifu_gemini_service"),
                    remoteConfig.getBoolean("automatic_server"),
                    remoteConfig.getLong("server_mode").toInt(),
                    getServerMode()
                )
                remoteValues.apply {
                    setNsfwMode(nsfwIsActive, gptIsActive, geminiIsActive)
                    setAutoMode(AutoModeIsEnabled)
                }
            } else {
                Log.e("getRemoteConfig", "Hubo un Error al recuperar de remote config: ${task.exception}")
            }
        }
    }

    private fun setAutoMode(isAutomatic: Boolean) {
        if (isAutomatic) {
            // TODO
            // Toast.makeText(requireContext(), "Automatic Mode Selected", Toast.LENGTH_SHORT).show()
        } else {
            // TODO
            // Toast.makeText(requireContext(), "Manual Mode Selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setNsfwMode(nsfw: Boolean, hasGpt: Boolean, hasGemini: Boolean) {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putBoolean(Constants.WORK_MODE, nsfw)
            putBoolean(Constants.IS_WAIFU_GPT, hasGpt)
            putBoolean(Constants.IS_WAIFU_GEMINI, hasGemini)
            apply()
        }
    }

    private fun navigateTo(mode: ServerType?, toFavorites: Boolean= false, toGpt: Boolean= false, toGemini: Boolean= false) {
        mode?.let { remoteValues.type = it }
        val bun = saveBundle(mode)
        if (toFavorites) {
            //
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
        bun.putBoolean(Constants.IS_NSFW_WAIFU, switchValues.first)
        bun.putBoolean(Constants.IS_GIF_WAIFU, switchValues.second)
        bun.putBoolean(Constants.IS_LANDS_WAIFU, switchValues.third)
        bun.putString(Constants.CATEGORY_TAG_WAIFU, tagFilter(selectedTag))
        saveServerMode()
        Log.v("saveBundle", "mode=${mode?.value} SERVER_MODE=${remoteValues.type?.value}")
        return bun
    }

    private fun saveServerMode() {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString(Constants.SERVER_MODE, remoteValues.type?.value)
            apply()
        }
        Log.v("SaveMode", "SERVER_MODE=${remoteValues.type}")
    }

    private fun getServerMode(): ServerType {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val mode = sharedPref.getString(Constants.SERVER_MODE, NORMAL.value)
        Log.v("GetMode", "SERVER_MODE=${mode}")
        return when (mode) {
            NORMAL.value -> NORMAL
            ENHANCED.value -> ENHANCED
            NEKOS.value -> NEKOS
            FAVORITE.value -> FAVORITE
            // ServerType.WAIFUGPT.value -> ServerType.WAIFUGPT
            // ServerType.WAIFUGEMINI.value -> ServerType.WAIFUGEMINI
            else -> NORMAL
        }
    }

    private fun tagFilter(tag: String): String {
        var updatedTag: String = tag
        if (tag == getString(R.string.categories) || tag == getString(R.string.categories_items)) {
            when (remoteValues.type) {
                NORMAL, ENHANCED -> {
                    updatedTag = getString(R.string.tag_waifu)
                }
                else -> {
                    if (!switchValues.second) {
                        updatedTag = getString(R.string.tag_neko)
                    } else {
                        updatedTag = getString(R.string.tag_pat)
                    }
                }
            }
        }
        return updatedTag
    }

    private fun loadInitialServer() {
        when (Build.VERSION.SDK_INT) {
            in 0..Build.VERSION_CODES.P -> {
                // Android 9 Hacia Abajo
                loadWaifu(requirePermissions, ENHANCED).apply { loadedServer = ENHANCED }
                // loadedServer = ENHANCED
                // loadedServer?.let { loadWaifu(requirePermissions, it) }
            }
            in 36..40 -> {
                // Android 15 Hacia Arriba
                loadWaifu(requirePermissions, NEKOS).apply { loadedServer = NEKOS }
                // loadedServer = NEKOS
                // loadedServer?.let { loadWaifu(requirePermissions, it) }
            }
            else -> {

                loadWaifu(requirePermissions, NORMAL).apply { loadedServer = NORMAL }
                // loadedServer = NORMAL
                // loadedServer?.let { loadWaifu(requirePermissions, it) }
            }
        }
    }

    private fun loadWaifu(reqPermisions: Boolean, serverType: ServerType) {
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
    }

    private fun backgroundLoaded() {
        loaded = true
    }

    private fun getSimpleText(type: String): String {
        return when (type) {
            NORMAL.value -> getString(R.string.server_normal_toast)
            ENHANCED.value -> getString(R.string.server_enhanced_toast)
            NEKOS.value -> getString(R.string.server_best_toast)
            else -> getString(R.string.server_unknown_toast)
        }
    }
}