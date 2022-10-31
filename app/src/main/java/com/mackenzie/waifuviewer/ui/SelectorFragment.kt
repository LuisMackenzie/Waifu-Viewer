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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.databinding.FragmentSelectorBinding
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.ui.common.*
import com.mackenzie.waifuviewer.ui.main.MainState
import com.mackenzie.waifuviewer.ui.main.OnChooseTypeChanged
import com.mackenzie.waifuviewer.ui.main.SelectorImViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectorFragment : Fragment(R.layout.fragment_selector), OnChooseTypeChanged {

    private val picsViewModel: SelectorPicViewModel by viewModels()
    private val imViewModel: SelectorImViewModel by viewModels()
    private lateinit var binding: FragmentSelectorBinding
    private var backgroudImage: ImageView? = null
    private var loaded: Boolean = false
    private lateinit var mainState: MainState
    private var serverMode = ServerType.NORMAL

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainState = MainState(requireContext(), viewLifecycleOwner.lifecycleScope, findNavController(), PermissionRequester(this , Manifest.permission.ACCESS_COARSE_LOCATION))
        binding = FragmentSelectorBinding.bind(view)
        setUpElements()
        updateSpinner()
        getRemoteConfig()
        loadInitialServer()
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
                val nsfw = remoteConfig.getBoolean("nsfw_mode")
                val isAutomatic = remoteConfig.getBoolean("automatic_server")
                val mode = remoteConfig.getLong("server_mode")
                setNsfwMode(nsfw)
                setAutoMode(isAutomatic)
            } else {
                Toast.makeText(requireContext(), "Fetch failed", Toast.LENGTH_SHORT).show()
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

    private fun setNsfwMode(nsfw: Boolean) {
        if (nsfw) {
            binding.sNsfw.visibility = View.VISIBLE
        }
    }

    private fun updateImWaifu(state: SelectorImViewModel.UiState) {
        state.waifu?.let { waifu ->
            setBackground(waifu.url)
            loaded = true
        }

        state.type.let { type ->
            binding.type = type
            serverMode = type
            updateSwitches()
            updateSpinner()
        }

        state.error?.let { error ->
            Glide.with(requireContext())
                .load(R.drawable.ic_offline_background)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error_grey)
                .into(binding.ivBackdrop)
            Toast.makeText(requireContext(), getString(R.string.require_connection), Toast.LENGTH_SHORT).show()
            Log.e(Constants.CATEGORY_TAG_SELECTOR_IM_ERROR, mainState.errorToString(error))
        }
    }

    private fun updatePicWaifu(state: SelectorPicViewModel.UiState) {
        state.waifu?.let { waifu ->
            setBackground(waifu.url)
            loaded = true
        }

        state.type.let { type ->
            binding.type = type
            serverMode = type
            updateSwitches()
            updateSpinner()
        }

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

    private fun setUpElements() = with(binding) {
        onChooseTypeChanged = this@SelectorFragment
        btnWaifu.setOnClickListener {
            navigateTo(serverMode)
        }
        sOrientation.setOnClickListener {
            if (sOrientation.isChecked) {
                sOrientation.text = getString(R.string.landscape)
            } else {
                sOrientation.text = getString(R.string.portrait_default)
            }
        }
        sNsfw.setOnClickListener {
            if (sNsfw.isChecked) {
                sNsfw.text = getString(R.string.nsfw_content)
            } else {
                sNsfw.text = getString(R.string.sfw_content)
            }
            updateSpinner()
        }
        fab.setOnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                imViewModel.loadErrorOrWaifu()
            } else {
                picsViewModel.loadErrorOrWaifu()
            }
        }
        favorites.setOnClickListener {
            serverMode = ServerType.FAVORITE
            navigateTo(serverMode)
        }
        backgroudImage = ivBackdrop
    }

    private fun updateSpinner() = with(binding) {
        val spinnerContent: Array<String>
        if (serverMode == ServerType.ENHANCED) {
            spinnerContent = if (sNsfw.isChecked) {
                Constants.ENHANCEDNSFW
            } else {
                Constants.ENHANCEDSFW
            }
        } else {
            spinnerContent = if (sNsfw.isChecked) {
                Constants.NORMALNSFW
            } else {
                Constants.NORMALSFW
            }
        }

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.spinner_item_calc, spinnerContent)
        spinner.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun updateSwitches() = with(binding) {
        when (serverMode.value) {
            ServerType.ENHANCED.value -> {
                sGifs.visible = false
                sOrientation.visible = false
            }
            ServerType.NORMAL.value -> {
                sGifs.visibility = View.VISIBLE
                sOrientation.visibility = View.VISIBLE
            }
            else -> {
                Toast.makeText(requireContext(), "${getString(R.string.unknown_mode)} ${serverMode.value}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateTo(mode: ServerType) = with(binding) {
        val bun = bundleOf()
        bun.putString(Constants.SERVER_MODE, serverMode.value)
        bun.putBoolean(Constants.IS_NSFW_WAIFU, sNsfw.isChecked)
        bun.putBoolean(Constants.IS_GIF_WAIFU, sGifs.isChecked)
        bun.putBoolean(Constants.IS_LANDS_WAIFU, sOrientation.isChecked)
        val selectedTag = spinner.selectedItem.toString()
        if (selectedTag.isNotEmpty()) {
            bun.putString(Constants.CATEGORY_TAG_WAIFU, selectedTag)
        }

        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString(Constants.SERVER_MODE, serverMode.value)
            apply()
        }

        when (mode) {
            ServerType.ENHANCED -> mainState.onButtonGetWaifuClicked(bun)
            ServerType.NORMAL -> mainState.onButtonGetWaifuClicked(bun)
            ServerType.FAVORITE -> mainState.onButtonFavoritesClicked(bun)
            else -> mainState.onButtonGetWaifuClicked(bun)
        }
    }

    private fun loadInitialServer() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            loadIm()
        } else {
            loadPics()
        }
    }

    private fun loadIm() {
        viewLifecycleOwner.launchAndCollect(imViewModel.state) { updateImWaifu(it) }
        mainState.requestPermissionLauncher {
            if (!loaded) {
                imViewModel.loadErrorOrWaifu()
                Toast.makeText(requireContext(), getString(R.string.server_normal_toast), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadPics() {
        viewLifecycleOwner.launchAndCollect(picsViewModel.state) { updatePicWaifu(it) }
        mainState.requestPermissionLauncher {
            if (!loaded) {
                picsViewModel.loadErrorOrWaifu()
                Toast.makeText(requireContext(), getString(R.string.server_enhanced_toast), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onChooseTypeChanged(type: ServerType) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            imViewModel.onChangeType(type)
        } else {
            picsViewModel.onChangeType(type)
        }
    }

}