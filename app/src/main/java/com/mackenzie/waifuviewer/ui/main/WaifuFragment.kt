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
import com.mackenzie.waifuviewer.data.datasource.WaifusImRepository
import com.mackenzie.waifuviewer.databinding.FragmentWaifuBinding
import com.mackenzie.waifuviewer.data.datasource.WaifusPicRepository
import com.mackenzie.waifuviewer.framework.db.RoomImDataSource
import com.mackenzie.waifuviewer.framework.db.RoomPicDataSource
import com.mackenzie.waifuviewer.framework.server.ServerImDataSource
import com.mackenzie.waifuviewer.framework.server.ServerPicDataSource
import com.mackenzie.waifuviewer.ui.common.app
import com.mackenzie.waifuviewer.ui.common.launchAndCollect
import com.mackenzie.waifuviewer.ui.common.visible
import com.mackenzie.waifuviewer.usecases.*

class WaifuFragment: Fragment(R.layout.fragment_waifu) {

    private val safeArgs: WaifuFragmentArgs by navArgs()
    private val picsViewModel: WaifuPicsViewModel by viewModels {
        val localDataSource = RoomPicDataSource(requireActivity().app.db.waifuPicDao())
        val remoteDataSource = ServerPicDataSource()
        val repo = WaifusPicRepository(localDataSource, remoteDataSource)
        WaifuPicsViewModelFactory(GetWaifuPicUseCase(repo), RequestWaifuPicUseCase(repo), RequestMoreWaifuPicUseCase(repo)) }
    private val imViewModel: WaifuImViewModel by viewModels {
        val localDataSource = RoomImDataSource(requireActivity().app.db.waifuImDao())
        val remoteDataSource = ServerImDataSource()
        val repo = WaifusImRepository(localDataSource, remoteDataSource)
        WaifuImViewModelFactory(GetWaifuImUseCase(repo), RequestWaifuImUseCase(repo), RequestMoreWaifuImUseCase(repo)) }
    private lateinit var mainState: MainState
    private lateinit var bun: Bundle
    private var mainServer: Boolean = false
    private var numOfWaifusIsShowed : Boolean= false
    private val waifuImAdapter = WaifuImAdapter{mainState.onWaifuClicked(it)}
    private val waifuPicsAdapter = WaifuPicsAdapter{ mainState.onWaifuPicsClicked(it) }

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

        loadCustomResult()

    }

    private fun loadCustomResult() {
        val isNsfw = bun.getBoolean(IS_NSFW_WAIFU)
        val isGif = bun.getBoolean(IS_GIF_WAIFU)
        val orientation = bun.getBoolean(IS_LANDS_WAIFU)
        // val waifuId = bun.getString(ID_WAIFU)
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
                    // Toast.makeText(requireContext(), "$categoryTag $isNsfw", Toast.LENGTH_SHORT).show()
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
        val isNsfw = bun.getBoolean(IS_NSFW_WAIFU)
        val categoryTag = bun.getString(CATEGORY_TAG)!!

        progress.visible = state.isLoading
        recycler.visibility = if(state.isLoading) View.GONE else View.VISIBLE

        // ivError.visibility = View.GONE
        // tvError.visibility = View.GONE

        state.waifusSavedPics?.let { savedPicWaifus ->
            waifuPicsAdapter.submitList(savedPicWaifus)
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

        progress.visible = state.isLoading
        recycler.visibility = if(state.isLoading) View.GONE else View.VISIBLE
        // ivError.visibility = if(state.error) View.VISIBLE else View.GONE
        // tvError.visibility = if(state.error) View.VISIBLE else View.GONE

        state.waifusSavedIm?.let { savedImWaifus ->
            waifuImAdapter.submitList(savedImWaifus)
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

