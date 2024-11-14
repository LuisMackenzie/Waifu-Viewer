package com.mackenzie.waifuviewer.ui

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.core.os.bundleOf
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import com.mackenzie.waifuviewer.domain.im.WaifuImTagList
import com.mackenzie.waifuviewer.ui.common.*
import com.mackenzie.waifuviewer.ui.main.MainState
import com.mackenzie.waifuviewer.ui.main.SelectorImViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectorFragment : Fragment(R.layout.fragment_selector) {

    private val picsViewModel: SelectorPicViewModel by viewModels()
    private val imViewModel: SelectorImViewModel by viewModels()
    private lateinit var binding: FragmentSelectorBinding
    private var backgroudImage: ImageView? = null
    private var loaded: Boolean = false
    private var requirePermissions: Boolean = false
    private lateinit var mainState: MainState
    private var tagsIm: WaifuImTagList? = null
    private var remoteValues : RemoteConfigValues = RemoteConfigValues()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainState = MainState(
            requireContext(),
            viewLifecycleOwner.lifecycleScope,
            findNavController(),
            PermissionRequester(this , Manifest.permission.ACCESS_COARSE_LOCATION)
        )
        binding = FragmentSelectorBinding.bind(view)
        getRemoteConfig()
        setUpElements()
        if (BuildConfig.BUILD_TYPE == ServerType.ENHANCED.value) loadPics(requirePermissions) else loadInitialServer()
    }

    /*override fun onCreateView(
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
        // TODO
        // setUpElements()
        // TODO
        // updateSpinner()
        if (remoteValues == null) getRemoteConfig()
        // TODO
        // loadInitialServer()
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MainTheme {
                    // LaunchHomeScreen(serverMode.getTypes())
                }
            }
        }
    }*/

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
        if (remoteValues.type == ServerType.NORMAL) binding.sNsfw.visible = nsfw
        binding.waifuGpt.visible = hasGpt
        binding.waifuGemini.visible = hasGemini
    }

    private fun updateImWaifu(state: SelectorImViewModel.UiState) {
        updateChips(getServerMode())
        state.waifu?.let { waifu ->
            setBackground(waifu.url)
            imViewModel.requestTags()
            loaded = true
        }

        /*state.type.let { type ->
            updateChips(type)
            updateSwitches()
        }*/

        state.tags?.let {
            tagsIm = it
            updateSpinner(tagsIm)
        }

        state.error?.let { error ->
            Glide.with(requireContext())
                .load(R.drawable.ic_offline_background)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error_grey)
                .into(binding.ivBackdrop)
            Toast.makeText(requireContext(), mainState.errorToString(error), Toast.LENGTH_SHORT).show()
            Log.e(Constants.CATEGORY_TAG_SELECTOR_IM_ERROR, mainState.errorToString(error))
        }
    }

    private fun updatePicWaifu(state: SelectorPicViewModel.UiState) {
        state.waifu?.let { waifu ->
            setBackground(waifu.url)
            loaded = true
        }

        updateChips(getServerMode())
        /*state.type.let { type ->
            updateChips(type)
            updateSwitches()
        }*/

        state.error?.let { error ->
            Glide.with(requireContext())
                .load(R.drawable.ic_offline_background)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error_grey)
                .into(binding.ivBackdrop)
            Toast.makeText(requireContext(), getString(R.string.require_connection), Toast.LENGTH_SHORT).show()
            Log.e(Constants.CATEGORY_TAG_SELECTOR_PICS_ERROR, mainState.errorToString(error))
        }
    }

    private fun setBackground(url: String) {
        backgroudImage?.loadUrlCenterCrop(url)
    }

    // TODO - Refactor this method
    private fun setUpElements() = with(binding) {
        cGroup.setOnCheckedStateChangeListener { group, checkedId ->
            group.forEach {
                val chip = it as Chip
                if (chip.id == checkedId.first()) {
                    val type = when (chip.text) {
                        getString(R.string.server_normal) -> ServerType.NORMAL
                        getString(R.string.server_enhanced) -> ServerType.ENHANCED
                        getString(R.string.server_best) -> ServerType.NEKOS
                        else -> ServerType.NORMAL
                    }
                    updateChips(type)
                }
            }

        }
        btnWaifu.setOnClickListener { navigateTo(remoteValues.type) }

        if (sOrientation.isChecked) sOrientation.text = getString(R.string.landscape)
        sOrientation.setOnClickListener { updateSwitches() }

        if (sNsfw.isChecked) sNsfw.text = getString(R.string.nsfw_content)
        sNsfw.setOnClickListener { updateSwitches() ; updateSpinner(tagsIm) }
        sGifs.setOnClickListener {
            if (remoteValues.type == ServerType.NEKOS) { updateSpinner(tagsIm) }
            updateSwitches()
        }
        reloadBackground.setOnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                imViewModel.loadErrorOrWaifu(requireContext().isLandscape())
            } else {
                picsViewModel.loadErrorOrWaifu()
            }
        }
        favorites.setOnClickListener {
            remoteValues.type = ServerType.FAVORITE
            navigateTo(ServerType.FAVORITE)
        }
        waifuGpt.setOnClickListener {
            remoteValues.type = ServerType.WAIFUGPT
            navigateTo(ServerType.WAIFUGPT)
        }
        waifuGemini.setOnClickListener {
            remoteValues.type = ServerType.WAIFUGEMINI
            navigateTo(ServerType.WAIFUGEMINI)
            Snackbar.make(requireView(), "Under Development!", Snackbar.LENGTH_SHORT).show()
        }
        backgroudImage = ivBackdrop
    }

    private fun updateChips(type: ServerType) {
        remoteValues.type = type
        when (type) {
            ServerType.NORMAL -> {
                binding.cNormal.isChecked = true
                binding.cEnhanced.isChecked = false
                binding.cNekos.isChecked = false
                binding.cNormal.isClickable = false
                binding.cEnhanced.isClickable = true
                binding.cNekos.isClickable = true
                binding.cNormal.setTextColor(getColor(resources, R.color.white, null))
                binding.cEnhanced.setTextColor(getColor(resources, R.color.black, null))
                binding.cNekos.setTextColor(getColor(resources, R.color.black, null))
            }
            ServerType.ENHANCED -> {
                binding.cNormal.isChecked = false
                binding.cEnhanced.isChecked = true
                binding.cNekos.isChecked = false
                binding.cNormal.isClickable = true
                binding.cEnhanced.isClickable = false
                binding.cNekos.isClickable = true
                binding.cNormal.setTextColor(getColor(resources, R.color.black, null))
                binding.cEnhanced.setTextColor(getColor(resources, R.color.white, null))
                binding.cNekos.setTextColor(getColor(resources, R.color.black, null))
            }
            ServerType.NEKOS -> {
                binding.cNormal.isChecked = false
                binding.cEnhanced.isChecked = false
                binding.cNekos.isChecked = true
                binding.cNormal.isClickable = true
                binding.cEnhanced.isClickable = true
                binding.cNekos.isClickable = false
                binding.cNormal.setTextColor(getColor(resources, R.color.black, null))
                binding.cEnhanced.setTextColor(getColor(resources, R.color.black, null))
                binding.cNekos.setTextColor(getColor(resources, R.color.white, null))
            }
            else -> {
                binding.cNormal.isChecked = true
                binding.cEnhanced.isChecked = false
                binding.cNekos.isChecked = false
                binding.cNormal.isClickable = false
                binding.cEnhanced.isClickable = true
                binding.cNekos.isClickable = true
                binding.cNormal.setTextColor(getColor(resources, R.color.white, null))
                binding.cEnhanced.setTextColor(getColor(resources, R.color.black, null))
                binding.cNekos.setTextColor(getColor(resources, R.color.black, null))
            }
        }
        saveServerMode()
        updateSpinner(tagsIm)
        updateSwitches()
    }

    // TODO - Refactor this method
    private fun updateSpinner(tags: WaifuImTagList?) = with(binding) {
        val spinnerContent: Array<String>
        when (remoteValues.type) {
            ServerType.ENHANCED -> {
                spinnerContent = if (sNsfw.isChecked) { Constants.ENHANCEDNSFW } else { Constants.ENHANCEDSFW }
            }
            ServerType.NORMAL -> {
                spinnerContent = if (sNsfw.isChecked) {
                    tags?.nsfw?.toTypedArray() ?: Constants.NORMALNSFW
                } else {
                     tags?.versatile?.toTypedArray() ?: Constants.NORMALSFW
                }
            }
            ServerType.NEKOS -> {
                spinnerContent = if (sGifs.isChecked) { Constants.NEKOSGIF } else { Constants.NEKOSPNG }
            }
            else -> {
                spinnerContent = if (sNsfw.isChecked) { Constants.NORMALNSFW } else { Constants.NORMALSFW }
            }
        }

        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_calc, spinnerContent)
        spinner.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    // TODO - Refactor this method
    private fun updateSwitches() = with(binding) {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val workMode = sharedPref.getBoolean(Constants.WORK_MODE, false)
        when (remoteValues.type) {
            ServerType.ENHANCED -> {
                sNsfw.visible = workMode
                sGifs.visible = false
                sOrientation.visible = false
                if (sNsfw.isChecked) sNsfw.text = getString(R.string.nsfw_content)
                else sNsfw.text = getString(R.string.sfw_content)
            }
            ServerType.NORMAL -> {
                sNsfw.visible = workMode
                sGifs.visible = true
                sOrientation.visible = true
                if (sOrientation.isChecked) sOrientation.text = getString(R.string.landscape)
                else sOrientation.text = getString(R.string.portrait_default)
                if (sNsfw.isChecked) sNsfw.text = getString(R.string.nsfw_content)
                else sNsfw.text = getString(R.string.sfw_content)
            }
            ServerType.NEKOS -> {
                sGifs.visible = true
                sNsfw.visible = false
                sOrientation.visible = false
            }
            else -> {
                Toast.makeText(requireContext(), "${getString(R.string.unknown_mode)}: ServerMode not Found Exception", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateTo(mode: ServerType?) {
        val bun = saveBundle()
        when (mode) {
            ServerType.FAVORITE -> mainState.onButtonFavoritesClicked(bun)
            ServerType.WAIFUGPT -> mainState.onButtonGptClicked()
            ServerType.WAIFUGEMINI -> mainState.onButtonGeminiClicked()
            else -> mainState.onButtonGetWaifuClicked(bun)
        }
    }

    private fun saveBundle(): Bundle {
        val bun = bundleOf()
        bun.putString(Constants.SERVER_MODE, remoteValues.type?.value)
        bun.putBoolean(Constants.IS_NSFW_WAIFU, binding.sNsfw.isChecked)
        bun.putBoolean(Constants.IS_GIF_WAIFU, binding.sGifs.isChecked)
        bun.putBoolean(Constants.IS_LANDS_WAIFU, binding.sOrientation.isChecked)
        bun.putString(Constants.CATEGORY_TAG_WAIFU, tagFilter(binding.spinner.selectedItem.toString()))
        saveServerMode()
        Log.v("saveBundle", "SERVER_MODE=${remoteValues.type?.value}")
        return bun
    }

    private fun saveServerMode() {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString(Constants.SERVER_MODE, remoteValues.type?.value)
            apply()
        }
    }

    private fun getServerMode(): ServerType {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val mode = sharedPref.getString(Constants.SERVER_MODE, ServerType.NORMAL.value)
        Log.v("getServerMode", "SERVER_MODE=${mode}")
        // requireNotNull(mode)
        return when (mode) {
            ServerType.NORMAL.value -> ServerType.NORMAL
            ServerType.ENHANCED.value -> ServerType.ENHANCED
            ServerType.NEKOS.value -> ServerType.NEKOS
            ServerType.FAVORITE.value -> ServerType.FAVORITE
            ServerType.WAIFUGPT.value -> ServerType.WAIFUGPT
            ServerType.WAIFUGEMINI.value -> ServerType.WAIFUGEMINI
            else -> ServerType.NORMAL
        }
    }

    private fun tagFilter(tag: String): String {
        var updatedTag: String = tag
        if (tag == getString(R.string.categories) || tag == getString(R.string.categories_items)) {
            when (remoteValues.type) {
                ServerType.NORMAL, ServerType.ENHANCED -> {
                    updatedTag = getString(R.string.tag_waifu)
                }
                else -> {
                    if (!binding.sGifs.isChecked) {
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
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            loadIm(requirePermissions)
        } else {
            loadPics(requirePermissions)
        }
    }

    private fun loadIm(reqPermisions: Boolean) {
        viewLifecycleOwner.launchAndCollect(imViewModel.state) { updateImWaifu(it) }
        if (reqPermisions) {
            mainState.requestPermissionLauncher {
                if (!loaded) {
                    imViewModel.loadErrorOrWaifu(requireContext().isLandscape())
                    Toast.makeText(requireContext(), getString(R.string.server_normal_toast), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            if (!loaded) {
                imViewModel.loadErrorOrWaifu(requireContext().isLandscape())
                Toast.makeText(requireContext(), getString(R.string.server_normal_toast), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun loadPics(reqPermisions: Boolean) {
        viewLifecycleOwner.launchAndCollect(picsViewModel.state) { updatePicWaifu(it) }
        if (reqPermisions) {
            mainState.requestPermissionLauncher {
                if (!loaded) {
                    picsViewModel.loadErrorOrWaifu()
                    Toast.makeText(requireContext(), getString(R.string.server_enhanced_toast), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            if (!loaded) {
                picsViewModel.loadErrorOrWaifu()
                Toast.makeText(requireContext(), getString(R.string.server_enhanced_toast), Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*override fun onChooseTypeChanged(type: ServerType) {
        remoteValues.type = type
        Log.v("onChooseTypeChanged", "SERVER_MODE=${type}")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            imViewModel.onChangeType(type)
        } else {
            picsViewModel.onChangeType(type)
        }
    }*/

}