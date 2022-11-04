package com.mackenzie.waifuviewer.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.WaifuPicsViewModel
import com.mackenzie.waifuviewer.databinding.FragmentWaifuBinding
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.common.launchAndCollect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WaifuFragment: Fragment(R.layout.fragment_waifu) {

    private val safeArgs: WaifuFragmentArgs by navArgs()
    private val picsViewModel: WaifuPicsViewModel by viewModels()
    private val imViewModel: WaifuImViewModel by viewModels()
    private val bestViewModel: WaifuBestViewModel by viewModels()
    private val waifuImAdapter = WaifuImAdapter{ mainState.onWaifuImClicked(it) }
    private val waifuPicsAdapter = WaifuPicsAdapter{ mainState.onWaifuPicsClicked(it) }
    private val waifuPngAdapter = WaifuPngAdapter{ mainState.onWaifuPngClicked(it) }
    private val waifuGifAdapter = WaifuGifAdapter{ mainState.onWaifuGifClicked(it) }
    private lateinit var mainState: MainState
    private lateinit var bun: Bundle
    private var serverMode: String = ""
    private var numIsShowed: Boolean = false
    private var loadingMore: Boolean = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainState = buildMainState()
        serverMode = safeArgs.bundleInfo.getString(Constants.SERVER_MODE) ?: ""
        bun = safeArgs.bundleInfo
        val binding = FragmentWaifuBinding.bind(view)
        when (serverMode) {
            getString(R.string.server_enhanced_string) -> {
                binding.recycler.adapter = waifuPicsAdapter
                viewLifecycleOwner.launchAndCollect(picsViewModel.state) { binding withPicsUpdateUI it }
            }
            getString(R.string.server_normal_string) -> {
                binding.recycler.adapter = waifuImAdapter
                viewLifecycleOwner.launchAndCollect(imViewModel.state) { binding withImUpdateUI it }
            }
            getString(R.string.server_nekos_string) -> {
                binding.recycler.adapter = waifuPngAdapter
                //TODO
                viewLifecycleOwner.launchAndCollect(bestViewModel.state) { binding withBestUpdateUI it }
            }
            else -> {

            }
        }

        loadCustomResult(bun)
    }

    private fun loadCustomResult(bun: Bundle) {
        val isNsfw = bun.getBoolean(Constants.IS_NSFW_WAIFU)
        val isGif = bun.getBoolean(Constants.IS_GIF_WAIFU)
        val orientation = bun.getBoolean(Constants.IS_LANDS_WAIFU)
        val categoryTag = bun.getString(Constants.CATEGORY_TAG_WAIFU) ?: ""

        if (serverMode == getString(R.string.server_normal_string)) {
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
        } else if (serverMode == getString(R.string.server_enhanced_string)) {
            when(categoryTag) {
                "All Categories" -> {
                    picsViewModel.onPicsReady(isNsfw, "waifu")
                }
                else -> {
                    picsViewModel.onPicsReady(isNsfw, categoryTag)
                }
            }
        } else {
            when(categoryTag) {
                "All Categories" -> {
                    bestViewModel.onBestReady(false, "waifu")
                }
                else -> {
                    bestViewModel.onBestReady(false, categoryTag)
                }
            }
        }
    }

    private infix fun FragmentWaifuBinding.withBestUpdateUI(state: WaifuBestViewModel.UiState) {

        var count: Int
        val categoryTag = bun.getString(Constants.CATEGORY_TAG_WAIFU) ?: ""

        state.waifusPng?.let { savedPngWaifus ->
            appendProgress.visibility = View.GONE
            waifuPngAdapter.submitList(savedPngWaifus)
            count = savedPngWaifus.size
            if (count != 0 && !numIsShowed) {
                Toast.makeText(requireContext(), "${getString(R.string.waifus_size)} $count", Toast.LENGTH_SHORT).show()
                numIsShowed = true
            }
        }

        state.waifusGif?.let { savedGifWaifus ->
            appendProgress.visibility = View.GONE
            waifuGifAdapter.submitList(savedGifWaifus)
            count = savedGifWaifus.size
            if (count != 0 && !numIsShowed) {
                Toast.makeText(requireContext(), "${getString(R.string.waifus_size)} $count", Toast.LENGTH_SHORT).show()
                numIsShowed = true
            }
        }

        state.error?.let {
            error = mainState.errorToString(it)
            ivError.visibility = View.VISIBLE
            tvError.visibility = View.VISIBLE
            Toast.makeText(requireContext(), mainState.errorToString(it), Toast.LENGTH_SHORT).show()
            Log.e(Constants.CATEGORY_TAG_WAIFU_BEST_ERROR, mainState.errorToString(it))
        }

        state.isLoading?.let {
            loading = it
        }

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recycler.canScrollVertically(1)) {
                    if (loadingMore == false) {
                        Toast.makeText(requireContext(), getString(R.string.waifus_coming), Toast.LENGTH_SHORT).show()
                        appendProgress.visibility = View.VISIBLE
                        bestViewModel.onRequestMore(false, categoryTag)
                        loadingMore = true
                        Handler(Looper.getMainLooper()).postDelayed({
                            loadingMore = false
                        }, 3000)
                    }
                }
            }
        })

        fabDelete.setOnClickListener {
            bestViewModel.onClearDatabase(false)
            requireActivity().onBackPressedDispatcher.onBackPressed()
            Toast.makeText(requireContext(), getString(R.string.waifus_gone), Toast.LENGTH_SHORT).show()
        }
    }

    private infix fun FragmentWaifuBinding.withPicsUpdateUI(state: WaifuPicsViewModel.UiState) {

        var count: Int
        val isNsfw = bun.getBoolean(Constants.IS_NSFW_WAIFU)
        val categoryTag = bun.getString(Constants.CATEGORY_TAG_WAIFU) ?: ""

        state.waifus?.let { savedPicWaifus ->
            appendProgress.visibility = View.GONE
            waifuPicsAdapter.submitList(savedPicWaifus)
            count = savedPicWaifus.size
            if (count != 0 && !numIsShowed) {
                Toast.makeText(requireContext(), "${getString(R.string.waifus_size)} $count", Toast.LENGTH_SHORT).show()
                numIsShowed = true
            }
        }

        state.error?.let {
            error = mainState.errorToString(it)
            ivError.visibility = View.VISIBLE
            tvError.visibility = View.VISIBLE
            Toast.makeText(requireContext(), mainState.errorToString(it), Toast.LENGTH_SHORT).show()
            Log.e(Constants.CATEGORY_TAG_WAIFU_PICS_ERROR, mainState.errorToString(it))
        }

        state.isLoading?.let {
            loading = it
        }

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recycler.canScrollVertically(1)) {
                    if (loadingMore == false) {
                        Toast.makeText(requireContext(), getString(R.string.waifus_coming), Toast.LENGTH_SHORT).show()
                        appendProgress.visibility = View.VISIBLE
                        picsViewModel.onRequestMore(isNsfw, categoryTag)
                        loadingMore = true
                        Handler(Looper.getMainLooper()).postDelayed({
                            loadingMore = false
                        }, 3000)
                    }
                }
            }
        })

        fabDelete.setOnClickListener {
            picsViewModel.onClearPicsDatabase()
            requireActivity().onBackPressedDispatcher.onBackPressed()
            Toast.makeText(requireContext(), getString(R.string.waifus_gone), Toast.LENGTH_SHORT).show()
        }
    }

    private infix fun FragmentWaifuBinding.withImUpdateUI(state: WaifuImViewModel.UiState) {

        var count: Int
        val isNsfw = bun.getBoolean(Constants.IS_NSFW_WAIFU)
        val isGif = bun.getBoolean(Constants.IS_GIF_WAIFU)
        val orientation = bun.getBoolean(Constants.IS_LANDS_WAIFU)
        val categoryTag = bun.getString(Constants.CATEGORY_TAG_WAIFU) ?: ""

        state.waifus?.let { savedImWaifus ->
            appendProgress.visibility = View.GONE
            waifuImAdapter.submitList(savedImWaifus)
            count = savedImWaifus.size
            if (count != 0 && !numIsShowed) {
                Toast.makeText(requireContext(), "${getString(R.string.waifus_size)} $count", Toast.LENGTH_SHORT).show()
                numIsShowed = true
            }
        }

        state.error?.let {
            error = mainState.errorToString(it)
            ivError.visibility = View.VISIBLE
            tvError.visibility = View.VISIBLE
            Toast.makeText(requireContext(), mainState.errorToString(it), Toast.LENGTH_SHORT).show()
            Log.e(Constants.CATEGORY_TAG_WAIFU_IM_ERROR, mainState.errorToString(it))
        }

        state.isLoading?.let {
            loading = it

        }

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recycler.canScrollVertically(1)) {
                    if (loadingMore == false) {
                        appendProgress.visibility = View.VISIBLE
                        imViewModel.onRequestMore(isNsfw, isGif, categoryTag , orientation)
                        loadingMore = true
                        Toast.makeText(requireContext(), getString(R.string.waifus_coming), Toast.LENGTH_SHORT).show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            loadingMore = false
                        }, 3000)
                    }
                }
            }
        })

        fabDelete.setOnClickListener {
            imViewModel.onClearImDatabase()
            requireActivity().onBackPressedDispatcher.onBackPressed()
            Toast.makeText(requireContext(), getString(R.string.waifus_gone), Toast.LENGTH_SHORT).show()
        }
    }
}

