package com.mackenzie.waifuviewer.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.WaifuPicsViewModel
import com.mackenzie.waifuviewer.WaifuPicsViewModelFactory
import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.databinding.FragmentWaifuBinding
import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.data.db.RoomImDataSource
import com.mackenzie.waifuviewer.data.db.RoomPicDataSource
import com.mackenzie.waifuviewer.data.server.ServerImDataSource
import com.mackenzie.waifuviewer.data.server.ServerPicDataSource
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.ui.common.app
import com.mackenzie.waifuviewer.ui.common.launchAndCollect
import com.mackenzie.waifuviewer.ui.common.visible
import com.mackenzie.waifuviewer.usecases.*

class WaifuFragment: Fragment(R.layout.fragment_waifu) {

    private val safeArgs: WaifuFragmentArgs by navArgs()
    private val picsViewModel: WaifuPicsViewModel by viewModels {
        val repo = WaifusPicRepository(RoomPicDataSource(requireActivity().app.db.waifuPicDao()), ServerPicDataSource())
        WaifuPicsViewModelFactory(GetWaifuPicUseCase(repo), RequestWaifuPicUseCase(repo), RequestMoreWaifuPicUseCase(repo)) }
    private val imViewModel: WaifuImViewModel by viewModels {
        val repo = WaifusImRepository(RoomImDataSource(requireActivity().app.db.waifuImDao()), ServerImDataSource())
        WaifuImViewModelFactory(GetWaifuImUseCase(repo), RequestWaifuImUseCase(repo), RequestMoreWaifuImUseCase(repo)) }
    private lateinit var mainState: MainState
    private lateinit var binding: FragmentWaifuBinding
    private var mainServer: Boolean = false
    private var numOfWaifusIsShowed : Boolean= false
    private val waifuImAdapter = WaifuImAdapter{ mainState.onWaifuClicked(it) }
    private val waifuPicsAdapter = WaifuPicsAdapter{ mainState.onWaifuPicsClicked(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainState = buildMainState()
        mainServer = safeArgs.bundleInfo.getBoolean(IS_SERVER_SELECTED)
        binding = FragmentWaifuBinding.bind(view).apply {
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

        loadCustomResult(safeArgs.bundleInfo)

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
                    Toast.makeText(requireContext(), "$categoryTag $isNsfw", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            when(categoryTag) {
                "all" -> {
                    picsViewModel.onPicsReady(isNsfw, "waifu")
                }
                else -> {
                    picsViewModel.onPicsReady(isNsfw, categoryTag)
                    Toast.makeText(requireContext(), "$categoryTag $isNsfw", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun FragmentWaifuBinding.withPicsUpdateUI(state: WaifuPicsViewModel.UiState) {

        var count: Int
        val bun = safeArgs.bundleInfo
        val isNsfw = bun.getBoolean(IS_NSFW_WAIFU)
        val categoryTag = bun.getString(CATEGORY_TAG)!!

        state.waifus?.let { savedPicWaifus ->
            waifuPicsAdapter.submitList(savedPicWaifus)
            Toast.makeText(requireContext(), "Showing Waifus", Toast.LENGTH_SHORT).show()
            count = waifuPicsAdapter.itemCount
            if (count != 0 && !numOfWaifusIsShowed) {
                Toast.makeText(requireContext(), "Total Waifus = $count", Toast.LENGTH_SHORT).show()
                numOfWaifusIsShowed = true
            }
        }

        state.error?.let {
            mainState.errorToString(it)
            ivError.visibility = View.VISIBLE
            tvError.visibility = View.VISIBLE
            // Toast.makeText(requireContext(), "Loading Error", Toast.LENGTH_SHORT).show()
        }
        state.isLoading.let {
            progress.visibility = if (it) View.VISIBLE else View.GONE
            recycler.visibility = if(it) View.GONE else View.VISIBLE
            // Toast.makeText(requireContext(), "Loading Waifus", Toast.LENGTH_SHORT).show()
        }

        fabRecycler.setOnClickListener {
            picsViewModel.onRequestMore(isNsfw, categoryTag)
            activity?.onBackPressed()
            // count = waifuPicsAdapter.itemCount
            Toast.makeText(requireContext(), "30 More Waifus are incoming. Hit button for show", Toast.LENGTH_SHORT).show()
        }
    }



    private fun FragmentWaifuBinding.withImUpdateUI(state: WaifuImViewModel.UiState) {

        var count: Int
        val bun = safeArgs.bundleInfo
        val isNsfw = bun.getBoolean(IS_NSFW_WAIFU)
        val isGif = bun.getBoolean(IS_GIF_WAIFU)
        val orientation = bun.getBoolean(IS_LANDS_WAIFU)
        val categoryTag = bun.getString(CATEGORY_TAG)!!

        // progress.visible = state.isLoading
        // recycler.visibility = if(state.isLoading) View.GONE else View.VISIBLE

        state.waifusSavedIm?.let { savedImWaifus ->
            waifuImAdapter.submitList(savedImWaifus)
            Toast.makeText(requireContext(), "Showing Waifus", Toast.LENGTH_SHORT).show()
            count = waifuImAdapter.itemCount
            if (count != 0 && !numOfWaifusIsShowed) {
                Toast.makeText(requireContext(), "Total Waifus = $count", Toast.LENGTH_SHORT).show()
                numOfWaifusIsShowed = true
            }
        }
        state.error?.let {
            mainState.errorToString(it)
            ivError.visibility = View.VISIBLE
            tvError.visibility = View.VISIBLE
        }
        state.isLoading.let {
            progress.visibility = if (it) View.VISIBLE else View.GONE
            recycler.visibility = if (it) View.GONE else View.VISIBLE
            // Toast.makeText(requireContext(), "Loading Waifus", Toast.LENGTH_SHORT).show()
        }

        fabRecycler.setOnClickListener {
            imViewModel.onRequestMore(isNsfw, isGif, categoryTag, orientation)
            activity?.onBackPressed()
            Toast.makeText(requireContext(), "30 More Waifus are incoming. Hit button for show", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val IS_SERVER_SELECTED = "WaifuFragment:server"
        const val IS_NSFW_WAIFU = "WaifuFragment:nsfw"
        const val IS_GIF_WAIFU = "WaifuFragment:gif"
        const val IS_LANDS_WAIFU = "WaifuFragment:lands"
        const val CATEGORY_TAG = "WaifuFragment:tag"
    }

}

