package com.mackenzie.waifuviewer.ui

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
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
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.databinding.FragmentSelectorBinding
import com.mackenzie.waifuviewer.ui.common.PermissionRequester
import com.mackenzie.waifuviewer.ui.common.launchAndCollect
import com.mackenzie.waifuviewer.ui.common.loadUrl
import com.mackenzie.waifuviewer.ui.main.MainState
import com.mackenzie.waifuviewer.ui.main.SelectorImViewModel
import com.mackenzie.waifuviewer.ui.main.WaifuFragment.Companion.CATEGORY_TAG
import com.mackenzie.waifuviewer.ui.main.WaifuFragment.Companion.IS_FAVORITES
import com.mackenzie.waifuviewer.ui.main.WaifuFragment.Companion.IS_GIF_WAIFU
import com.mackenzie.waifuviewer.ui.main.WaifuFragment.Companion.IS_LANDS_WAIFU
import com.mackenzie.waifuviewer.ui.main.WaifuFragment.Companion.IS_NSFW_WAIFU
import com.mackenzie.waifuviewer.ui.main.WaifuFragment.Companion.IS_SERVER_SELECTED
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectorFragment : Fragment(R.layout.fragment_selector) {

    private val picsViewModel: SelectorPicViewModel by viewModels()
    private val imViewModel: SelectorImViewModel by viewModels()
    private lateinit var binding: FragmentSelectorBinding
    private var backgroudImage: ImageView? = null
    private var loaded: Boolean = false
    private lateinit var mainState: MainState

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainState = MainState(requireContext(), viewLifecycleOwner.lifecycleScope, findNavController(), PermissionRequester(this , Manifest.permission.ACCESS_COARSE_LOCATION))
        binding = FragmentSelectorBinding.bind(view)
        setUpElements()
        updateSpinner(binding.sServer.isChecked)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            viewLifecycleOwner.launchAndCollect(imViewModel.state) { updateImWaifu(it) }
            mainState.requestPermissionLauncher {
                if (!loaded) {
                    imViewModel.loadErrorOrWaifu()
                    Toast.makeText(requireContext(), "IM server loaded", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            viewLifecycleOwner.launchAndCollect(picsViewModel.state) { updatePicWaifu(it) }
            mainState.requestPermissionLauncher {
                if (!loaded) {
                    picsViewModel.loadErrorOrWaifu()
                    Toast.makeText(requireContext(), "PICS server loaded", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }




    private fun updateImWaifu(state: SelectorImViewModel.UiState) {
        state.waifu?.let { waifu ->
            setBackground(waifu.url)
            loaded = true
        }

        state.error?.let { error ->
            mainState.errorToString(error)
            Glide.with(requireContext())
                .load(R.drawable.ic_offline_background)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error_grey)
                .into(binding.ivBackdrop)
            Toast.makeText(requireContext(), "Se requiere conexion para funcionar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePicWaifu(state: SelectorPicViewModel.UiState) {
        state.waifu?.let { waifu ->
            setBackground(waifu.url)
            loaded = true
        }

        state.error?.let { error ->
            mainState.errorToString(error)
            Glide.with(requireContext())
                .load(R.drawable.ic_offline_background)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error_grey)
                .into(binding.ivBackdrop)
            Toast.makeText(requireContext(), "Se requiere conexion para funcionar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setBackground(url: String) {
        backgroudImage?.loadUrl(url)
    }

    private fun setUpElements() = with(binding) {
        btnWaifu.setOnClickListener {
            navigateTo(false)
        }
        sOrientation.setOnClickListener {
            if (sOrientation.isChecked) {
                sOrientation.text = "Landscape"
            } else {
                sOrientation.text = "Portrait"
            }

        }
        sServer.setOnClickListener {
            updateSwitches(sServer.isChecked)
            updateSpinner(sServer.isChecked)
        }
        sNsfw.setOnClickListener {
            updateSpinner(sServer.isChecked)
        }
        fab.setOnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                imViewModel.loadErrorOrWaifu()
            } else {
                picsViewModel.loadErrorOrWaifu()
            }
        }
        favorites.setOnClickListener {
            navigateTo(true)
        }
        backgroudImage = ivBackdrop
    }

    private fun updateSpinner(state: Boolean) = with(binding) {
        val spinnerContent: Array<String>
        if (state) {
            spinnerContent = if (sNsfw.isChecked) {
                arrayOf("all", "waifu", "neko", "trap", "blowjob")
            } else {
                arrayOf("all", "waifu", "neko", "shinobu", "megumin", "bully", "cuddle", "cry", "hug", "awoo", "kiss", "lick", "pat", "smug", "bonk", "yeet", "blush", "smile", "wave", "highfive", "handhold", "nom", "bite", "glomp", "slap", "kill", "kick", "happy", "wink", "poke", "dance", "cringe")
            }
        } else {
            spinnerContent = if (sNsfw.isChecked) {
                arrayOf("all", "ass", "hentai", "milf", "oral", "paizuri", "ecchi", "ero")
            } else {
                arrayOf("all", "uniform", "maid", "waifu", "marin-kitagawa", "mori-calliope", "raiden-shogun", "oppai")
            }
        }

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.spinner_item_calc, spinnerContent)
        spinner.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun updateSwitches(state: Boolean) = with(binding) {
        val view : Int
        if (state) {
            view = View.GONE
            sServer.text = "Enhanced Mode"
        } else{
            view = View.VISIBLE
            sServer.text = "Fancy Mode"
        }
        sGifs.visibility = view
        sOrientation.visibility = view

    }

    private fun navigateTo(favorite :Boolean) = with(binding) {
        val bun = bundleOf()
        bun.putBoolean(IS_SERVER_SELECTED, sServer.isChecked)
        bun.putBoolean(IS_NSFW_WAIFU, sNsfw.isChecked)
        bun.putBoolean(IS_GIF_WAIFU, sGifs.isChecked)
        bun.putBoolean(IS_LANDS_WAIFU, sOrientation.isChecked)
        /*if (tietId.text != null) {
            bun.putString(ID_WAIFU, tietId.text.toString())
        }*/
        val selectedTag = spinner.selectedItem.toString()
        if (!selectedTag.isEmpty()) {
            bun.putString(CATEGORY_TAG, selectedTag)
        }

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean(IS_SERVER_SELECTED, sServer.isChecked)
            putBoolean(IS_FAVORITES, favorite)
            apply()
        }

        if (favorite) {
            mainState.onButtonFavoritesClicked(bun)
        } else {
            mainState.onButtonGetWaifuClicked(bun)
        }


    }

    override fun onResume() {
        super.onResume()
        binding.sNsfw.isChecked = false
        updateSpinner(binding.sServer.isChecked)
        updateSwitches(binding.sServer.isChecked)
    }


}