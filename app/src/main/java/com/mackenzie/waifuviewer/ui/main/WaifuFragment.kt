package com.mackenzie.waifuviewer.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.WaifuPicsViewModel
import com.mackenzie.waifuviewer.databinding.FragmentWaifuBinding
import com.mackenzie.waifuviewer.ui.common.launchAndCollect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WaifuFragment: Fragment(R.layout.fragment_waifu) {

    private val safeArgs: WaifuFragmentArgs by navArgs()
    private val picsViewModel: WaifuPicsViewModel by viewModels()
    private val imViewModel: WaifuImViewModel by viewModels()
    private val waifuImAdapter = WaifuImAdapter{ mainState.onWaifuImClicked(it) }
    private val waifuPicsAdapter = WaifuPicsAdapter{ mainState.onWaifuPicsClicked(it) }
    private lateinit var mainState: MainState
    private lateinit var bun :Bundle
    private var mainServer: Boolean = false
    private var numIsShowed : Boolean= false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainState = buildMainState()
        mainServer = safeArgs.bundleInfo.getBoolean(IS_SERVER_SELECTED)
        bun = safeArgs.bundleInfo
        val binding = FragmentWaifuBinding.bind(view).apply {
            if (mainServer) {
                recycler.adapter = waifuPicsAdapter
            } else {
                recycler.adapter = waifuImAdapter
            }
        }

        if (mainServer) {
            viewLifecycleOwner.launchAndCollect(picsViewModel.state) { binding.withPicsUpdateUI(it) }
        } else {
            viewLifecycleOwner.launchAndCollect(imViewModel.state) { binding.withImUpdateUI(it) }
        }

        loadCustomResult(bun)

    }

    private fun loadCustomResult(bun: Bundle) {
        val isNsfw = bun.getBoolean(IS_NSFW_WAIFU)
        val isGif = bun.getBoolean(IS_GIF_WAIFU)
        val orientation = bun.getBoolean(IS_LANDS_WAIFU)
        val categoryTag = bun.getString(CATEGORY_TAG)!!

        if (!mainServer) {
            when (categoryTag) {
                "uniform" -> {
                    imViewModel.onImReady(isNsfw, isGif = false, categoryTag, orientation)
                }
                "maid" -> {
                    imViewModel.onImReady(isNsfw, isGif = false, categoryTag, orientation)
                }
                "marin-kitagawa" -> {
                    imViewModel.onImReady(isNsfw, isGif = false, categoryTag, orientation)
                }
                "mori-calliope" -> {
                    imViewModel.onImReady(isNsfw, isGif = false, categoryTag, false)
                }
                "raiden-shogun" -> {
                    imViewModel.onImReady(isNsfw, isGif = false, categoryTag, false)
                }
                "oppai" -> {
                    imViewModel.onImReady(isNsfw, isGif = false, categoryTag, orientation)
                }
                else -> {
                    imViewModel.onImReady(isNsfw, isGif, categoryTag, orientation)
                }
            }
        } else {
            when(categoryTag) {
                "all" -> {
                    picsViewModel.onPicsReady(isNsfw, "waifu")
                }
                else -> {
                    picsViewModel.onPicsReady(isNsfw, categoryTag)
                }
            }
        }
    }

    private fun FragmentWaifuBinding.withPicsUpdateUI(state: WaifuPicsViewModel.UiState) {

        var count: Int
        // val bun = safeArgs.bundleInfo
        val isNsfw = bun.getBoolean(IS_NSFW_WAIFU)
        val categoryTag = bun.getString(CATEGORY_TAG)!!

        state.waifus?.let { savedPicWaifus ->
            waifuPicsAdapter.submitList(savedPicWaifus.reversed())
            // waifuPic = savedPicWaifus
            count = savedPicWaifus.size
            if (count != 0 && !numIsShowed) {
                Toast.makeText(requireContext(), "Total Waifus = $count", Toast.LENGTH_SHORT).show()
                numIsShowed = true
            }
        }

        state.error?.let {
            mainState.errorToString(it)
            ivError.visibility = View.VISIBLE
            tvError.visibility = View.VISIBLE
        }

        state.isLoading.let {
            progress.visibility = if (it) View.VISIBLE else View.GONE
        }

        fabRecycler.setOnClickListener {
            picsViewModel.onRequestMore(isNsfw, categoryTag)
            activity?.onBackPressed()
            Toast.makeText(requireContext(), "More Waifus are coming.", Toast.LENGTH_SHORT).show()
        }
        fabDelete.setOnClickListener {
            picsViewModel.onClearPicsDatabase()
            Toast.makeText(requireContext(), "Some waifus are gone. PICS", Toast.LENGTH_SHORT).show()
        }
    }



    private fun FragmentWaifuBinding.withImUpdateUI(state: WaifuImViewModel.UiState) {

        var count: Int
        val isNsfw = bun.getBoolean(IS_NSFW_WAIFU)
        val isGif = bun.getBoolean(IS_GIF_WAIFU)
        val orientation = bun.getBoolean(IS_LANDS_WAIFU)
        val categoryTag = bun.getString(CATEGORY_TAG)!!

        // progress.visible = state.isLoading
        // recycler.visibility = if(state.isLoading) View.GONE else View.VISIBLE

        // waifuIm = state.waifus
        // loading = state.isLoading
        // error = state.error?.let{mainState::errorToString}.toString()
        // Toast.makeText(requireContext(), "Showing IM Waifus ${state.waifus}", Toast.LENGTH_SHORT).show()

        state.waifus?.let { savedImWaifus ->
            // waifuIm = savedImWaifus
            waifuImAdapter.submitList(savedImWaifus.reversed())
            // recycler.scrollToPosition(savedImWaifus.last().id)
            count = savedImWaifus.size
            if (count != 0 && !numIsShowed) {
                Toast.makeText(requireContext(), "Total Waifus = $count", Toast.LENGTH_SHORT).show()
                numIsShowed = true
            }
        }

        state.error?.let {
            mainState.errorToString(it)
            ivError.visibility = View.VISIBLE
            tvError.visibility = View.VISIBLE
        }

        state.isLoading.let {
            progress.visibility = if (it) View.VISIBLE else View.GONE
        }

        fabRecycler.setOnClickListener {
            imViewModel.onRequestMore(isNsfw, isGif, categoryTag, orientation)
            activity?.onBackPressed()
            Toast.makeText(requireContext(), "More Waifus are coming.", Toast.LENGTH_SHORT).show()
        }
        fabDelete.setOnClickListener {
            imViewModel.onClearImDatabase()
            Toast.makeText(requireContext(), "Some waifus are gone. IM", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val IS_SERVER_SELECTED = "WaifuFragment:server"
        const val IS_FAVORITES = "WaifuFragment:favorites"
        const val IS_NSFW_WAIFU = "WaifuFragment:nsfw"
        const val IS_GIF_WAIFU = "WaifuFragment:gif"
        const val IS_LANDS_WAIFU = "WaifuFragment:lands"
        const val CATEGORY_TAG = "WaifuFragment:tag"
    }

}

