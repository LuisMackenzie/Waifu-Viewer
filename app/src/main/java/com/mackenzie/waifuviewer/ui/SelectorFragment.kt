package com.mackenzie.waifuviewer.ui

import android.Manifest
import android.content.Context
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
import com.mackenzie.waifuviewer.data.datasource.WaifusPicRepository
import com.mackenzie.waifuviewer.framework.db.RoomPicDataSource
import com.mackenzie.waifuviewer.framework.server.ServerPicDataSource
import com.mackenzie.waifuviewer.framework.server.WaifuPic
import com.mackenzie.waifuviewer.usecases.RequestOnlyWaifuPicUseCase
import com.mackenzie.waifuviewer.ui.common.PermissionRequester
import com.mackenzie.waifuviewer.ui.common.app
import com.mackenzie.waifuviewer.ui.common.launchAndCollect
import com.mackenzie.waifuviewer.ui.common.loadUrl
import com.mackenzie.waifuviewer.ui.main.MainState
import com.mackenzie.waifuviewer.ui.main.WaifuFragment.Companion.CATEGORY_TAG
import com.mackenzie.waifuviewer.ui.main.WaifuFragment.Companion.IS_GIF_WAIFU
import com.mackenzie.waifuviewer.ui.main.WaifuFragment.Companion.IS_LANDS_WAIFU
import com.mackenzie.waifuviewer.ui.main.WaifuFragment.Companion.IS_NSFW_WAIFU
import com.mackenzie.waifuviewer.ui.main.WaifuFragment.Companion.IS_SERVER_SELECTED
import kotlinx.coroutines.launch

class SelectorFragment : Fragment(R.layout.fragment_selector) {

    private val viewModel: SelectorViewModel by viewModels {
        val localDataSource = RoomPicDataSource(requireActivity().app.db.waifuPicDao())
        val remoteDataSource = ServerPicDataSource()
        val repo = WaifusPicRepository(localDataSource, remoteDataSource)
        SelectorViewModelFactory(RequestOnlyWaifuPicUseCase(repo)) }
    private lateinit var binding: FragmentSelectorBinding
    private var backgroudImage: ImageView? = null
    private lateinit var mainState: MainState

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainState = MainState(requireContext(), viewLifecycleOwner.lifecycleScope, findNavController(), PermissionRequester(this , Manifest.permission.ACCESS_COARSE_LOCATION))
        binding = FragmentSelectorBinding.bind(view)
        setUpElements()
        updateSpinner(binding.sServer.isChecked)

        viewLifecycleOwner.launchAndCollect(viewModel.state) { updateWaifu(it) }
        viewLifecycleOwner.lifecycleScope.launch {
            mainState.requestPermissionLauncher {
                viewModel.loadErrorOrWaifu()
            }
        }
    }




    private fun updateWaifu(state: SelectorViewModel.UiState) {
        state.waifu?.let { waifu ->
            setBackground(waifu)
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

    private fun setBackground(waifu: WaifuPic) {
        backgroudImage?.loadUrl(waifu.url)
    }

    private fun setUpElements() = with(binding) {
        btnWaifu.setOnClickListener {
            navigateTo()
            // Toast.makeText(this, "Mostrando Waifus", Toast.LENGTH_SHORT).show()
        }
        /*btnSearch.setOnClickListener {
            // Toast.makeText(this, "Buscando Waifu", Toast.LENGTH_SHORT).show()
        }*/
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
            // viewLifecycleOwner.launchAndCollect(viewModel.state) { updateWaifu(it) }
            viewModel.loadWaifu()
        }
        backgroudImage = ivBackdrop
    }

    private fun updateSpinner(state: Boolean) = with(binding) {
        val spinnerContent: Array<String>
        // val spinnerIsNsfw: Boolean = sNsfw.isEnabled
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

    private fun navigateTo() = with(binding) {
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
            apply()
        }

        mainState.onButtonGetWaifuClicked(bun)

    }

    override fun onResume() {
        super.onResume()
        binding.sNsfw.isChecked = false
        updateSpinner(binding.sServer.isChecked)
        updateSwitches(binding.sServer.isChecked)
    }


}