package com.mackenzie.waifuviewer.ui.main

import android.app.NotificationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var bun: Bundle
    private var serverMode: String = ""
    private var numIsShowed: Boolean = false
    private var loadingMore: Boolean = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainState = buildMainState()
        serverMode = safeArgs.bundleInfo.getString(SERVER_MODE)!!
        bun = safeArgs.bundleInfo
        val binding = FragmentWaifuBinding.bind(view)
        when (serverMode) {
            "enhanced" -> {
                binding.recycler.adapter = waifuPicsAdapter
                viewLifecycleOwner.launchAndCollect(picsViewModel.state) { binding.withPicsUpdateUI(it) }
            }
            else -> {
                binding.recycler.adapter = waifuImAdapter
                viewLifecycleOwner.launchAndCollect(imViewModel.state) { binding.withImUpdateUI(it) }
            }
        }

        loadCustomResult(bun)
    }

    private fun loadCustomResult(bun: Bundle) {
        val isNsfw = bun.getBoolean(IS_NSFW_WAIFU)
        val isGif = bun.getBoolean(IS_GIF_WAIFU)
        val orientation = bun.getBoolean(IS_LANDS_WAIFU)
        val categoryTag = bun.getString(CATEGORY_TAG) ?: ""

        if (serverMode == "normal") {
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
        val isNsfw = bun.getBoolean(IS_NSFW_WAIFU)
        val categoryTag = bun.getString(CATEGORY_TAG) ?: ""

        state.waifus?.let { savedPicWaifus ->
            // waifuPicsAdapter.submitList(savedPicWaifus.reversed())
            appendProgress.visibility = View.GONE
            waifuPicsAdapter.submitList(savedPicWaifus)
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
            Toast.makeText(requireContext(), mainState.errorToString(it), Toast.LENGTH_SHORT).show()
            Log.e("PICS error", mainState.errorToString(it))
        }

        state.isLoading?.let {
            loading = it
        }

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recycler.canScrollVertically(1)) {
                    if (loadingMore == false) {
                        Toast.makeText(requireContext(), "More Waifus are coming", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(requireContext(), "Some waifus are gone", Toast.LENGTH_SHORT).show()
        }
    }

    private fun FragmentWaifuBinding.withImUpdateUI(state: WaifuImViewModel.UiState) {

        var count: Int
        val isNsfw = bun.getBoolean(IS_NSFW_WAIFU)
        val isGif = bun.getBoolean(IS_GIF_WAIFU)
        val orientation = bun.getBoolean(IS_LANDS_WAIFU)
        val categoryTag = bun.getString(CATEGORY_TAG) ?: ""

        state.waifus?.let { savedImWaifus ->
            appendProgress.visibility = View.GONE
            waifuImAdapter.submitList(savedImWaifus)
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
            Toast.makeText(requireContext(), mainState.errorToString(it), Toast.LENGTH_SHORT).show()
            Log.e("IM error", mainState.errorToString(it))
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
                        Toast.makeText(requireContext(), "More Waifus are coming $loadingMore", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(requireContext(), "Some waifus are gone", Toast.LENGTH_SHORT).show()
        }
    }

    

    companion object {
        const val SERVER_MODE = "WaifuFragment:server_mode"
        const val IS_FAVORITES = "WaifuFragment:favorites"
        const val IS_NSFW_WAIFU = "WaifuFragment:nsfw"
        const val IS_GIF_WAIFU = "WaifuFragment:gif"
        const val IS_LANDS_WAIFU = "WaifuFragment:lands"
        const val CATEGORY_TAG = "WaifuFragment:tag"
    }

}

