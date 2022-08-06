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
import com.mackenzie.waifuviewer.ui.common.app
import com.mackenzie.waifuviewer.ui.common.launchAndCollect
import com.mackenzie.waifuviewer.usecases.*

class WaifuFragment: Fragment(R.layout.fragment_waifu) {

    private val safeArgs: WaifuFragmentArgs by navArgs()
    private val picsViewModel: WaifuPicsViewModel by viewModels {
        val repo = WaifusPicRepository(RoomPicDataSource(requireActivity().app.db.waifuPicDao()), ServerPicDataSource())
        WaifuPicsViewModelFactory(GetWaifuPicUseCase(repo), RequestWaifuPicUseCase(repo), RequestMoreWaifuPicUseCase(repo)) }
    private val imViewModel: WaifuImViewModel by viewModels {
        val repo = WaifusImRepository(RoomImDataSource(requireActivity().app.db.waifuImDao()), ServerImDataSource())
        WaifuImViewModelFactory(GetWaifuImUseCase(repo), RequestWaifuImUseCase(repo), RequestMoreWaifuImUseCase(repo)) }
    private val waifuImAdapter = WaifuImAdapter{ mainState.onWaifuClicked(it) }
    private val waifuPicsAdapter = WaifuPicsAdapter{ mainState.onWaifuPicsClicked(it) }
    private lateinit var mainState: MainState
    private lateinit var binding: FragmentWaifuBinding
    private lateinit var bun :Bundle
    private var mainServer: Boolean = false
    private var numOfWaifusIsShowed : Boolean= false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainState = buildMainState()
        mainServer = safeArgs.bundleInfo.getBoolean(IS_SERVER_SELECTED)
        bun = safeArgs.bundleInfo
        binding = FragmentWaifuBinding.bind(view).apply {
            if (mainServer) {
                recyclerPics.adapter = waifuPicsAdapter
            } else {
                recyclerIm.adapter = waifuImAdapter
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



        // binding.recyclerIm.visibility = View.GONE

        state.waifus?.let { savedPicWaifus ->
            // waifuPicsAdapter.submitList(savedPicWaifus)
            binding.waifuPic = savedPicWaifus
            // Toast.makeText(requireContext(), "Showing PICS Waifus $savedPicWaifus", Toast.LENGTH_SHORT).show()
            count = savedPicWaifus.size
            if (count != 0 && !numOfWaifusIsShowed) {
                Toast.makeText(requireContext(), "Total Waifus = $count", Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), "Pics Waifus ID First ${savedPicWaifus.first().id}", Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), "Pics Waifus ID Last ${savedPicWaifus.last().id}", Toast.LENGTH_SHORT).show()
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
            recyclerPics.visibility = if(it) View.GONE else View.VISIBLE
            recyclerIm.visibility = if(it) View.GONE else View.GONE
        }

        fabRecycler.setOnClickListener {
            // picsViewModel.onRequestMore(isNsfw, categoryTag)
            // activity?.onBackPressed()
            Toast.makeText(requireContext(), "30 More Waifus are incoming. Hit button for show", Toast.LENGTH_SHORT).show()
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
            // waifuImAdapter.submitList(savedImWaifus)
            // waifuImAdapter.imAdapter.addAll(savedImWaifus)
            binding.waifuIm = savedImWaifus
            // waifuImAdapter.currentList.addAll(savedImWaifus)
            // Toast.makeText(requireContext(), "Showing IM Waifus $savedImWaifus", Toast.LENGTH_SHORT).show()
            count = savedImWaifus.size
            if (count != 0 && !numOfWaifusIsShowed) {
                Toast.makeText(requireContext(), "Total Waifus = $count", Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), "IM Waifus ID First ${savedImWaifus.first().id}", Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), "IM Waifus ID Last ${savedImWaifus.last().id}", Toast.LENGTH_SHORT).show()
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
            recyclerIm.visibility = if (it) View.GONE else View.VISIBLE
            recyclerPics.visibility = if(it) View.GONE else View.GONE
        }

        fabRecycler.setOnClickListener {
            // imViewModel.onRequestMore(isNsfw, isGif, categoryTag, orientation)
            // activity?.onBackPressed()
            // Toast.makeText(requireContext(), "30 More Waifus are incoming. Hit button for show", Toast.LENGTH_SHORT).show()
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

