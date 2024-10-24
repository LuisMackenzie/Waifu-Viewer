package com.mackenzie.waifuviewer.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.WaifuPicsViewModel
import com.mackenzie.waifuviewer.databinding.FragmentWaifuBinding
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.ENHANCED
import com.mackenzie.waifuviewer.domain.ServerType.FAVORITE
import com.mackenzie.waifuviewer.domain.ServerType.NEKOS
import com.mackenzie.waifuviewer.domain.ServerType.NORMAL
import com.mackenzie.waifuviewer.domain.ServerType.WAIFUGPT
import com.mackenzie.waifuviewer.domain.getTypes
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.common.launchAndCollect
import com.mackenzie.waifuviewer.ui.favs.ui.FavoriteScreenContent
import com.mackenzie.waifuviewer.ui.main.adapters.WaifuImAdapter
import com.mackenzie.waifuviewer.ui.main.adapters.WaifuPicsAdapter
import com.mackenzie.waifuviewer.ui.main.adapters.WaifuBestAdapter
import com.mackenzie.waifuviewer.ui.main.ui.MainTheme
import com.mackenzie.waifuviewer.ui.main.ui.WaifuBestScreenContent
import com.mackenzie.waifuviewer.ui.main.ui.WaifuImScreenContent
import com.mackenzie.waifuviewer.ui.main.ui.WaifuPicsScreenContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WaifuFragment : Fragment(R.layout.fragment_waifu) {

    private val safeArgs: WaifuFragmentArgs by navArgs()
    private val picsViewModel: WaifuPicsViewModel by viewModels()
    private val imViewModel: WaifuImViewModel by viewModels()
    private val bestViewModel: WaifuBestViewModel by viewModels()
    // private val waifuImAdapter = WaifuImAdapter { mainState.onWaifuImClicked(it) }
    // private val waifuPicsAdapter = WaifuPicsAdapter { mainState.onWaifuPicsClicked(it) }
    // private val waifuBestAdapter = WaifuBestAdapter { mainState.onWaifuBestClicked(it) }
    private lateinit var mainState: MainState
    private lateinit var bun: Bundle
    private var serverMode: String = ""
    private var numIsShowed: Boolean = false
    private var loadingMore: Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // mainState = buildMainState()
        // serverMode = safeArgs.bundleInfo.getString(Constants.SERVER_MODE) ?: ""
        // bun = safeArgs.bundleInfo
        // val binding = FragmentWaifuBinding.bind(view)
        when (serverMode) {
            getString(R.string.server_enhanced_string) -> {
                // binding.recycler.adapter = waifuPicsAdapter
                // viewLifecycleOwner.launchAndCollect(picsViewModel.state) { binding withPicsUpdateUI it }
            }
            getString(R.string.server_normal_string) -> {
                // binding.recycler.adapter = waifuImAdapter
                // viewLifecycleOwner.launchAndCollect(imViewModel.state) { binding withImUpdateUI it }
            }
            getString(R.string.server_nekos_string) -> {
                // binding.recycler.adapter = waifuBestAdapter
                // viewLifecycleOwner.launchAndCollect(bestViewModel.state) { binding withBestUpdateUI it }
            }
        }

        // loadCustomResult(bun)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainState = buildMainState()
        serverMode = safeArgs.bundleInfo.getString(Constants.SERVER_MODE) ?: ""
        bun = safeArgs.bundleInfo
        loadCustomResult(bun)
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MainTheme {
                    LaunchWaifuScreen(serverMode.getTypes())
                }
            }
        }
    }

    @Composable
    private fun LaunchWaifuScreen(mode: ServerType) {
        when (mode) {
            NORMAL -> WaifuImScreen()
            ENHANCED -> WaifuPicsScreen()
            NEKOS -> WaifuNekosScreen()
            FAVORITE -> {}
            WAIFUGPT -> {}
        }
    }

    @Composable
    private fun WaifuImScreen() {
        WaifuImScreenContent(
            state = imViewModel.state.collectAsState().value,
            // bun = bun,
            onWaifuClicked = { mainState.onWaifuImClicked(it) },
            onRequestMore = { onLoadMoreWaifusIm() },
            onFabClick = {
                imViewModel.onClearImDatabase()
                requireActivity().onBackPressedDispatcher.onBackPressed()
                Toast.makeText(requireContext(), getString(R.string.waifus_gone), Toast.LENGTH_SHORT).show()
            }
        )
    }

    @Composable
    private fun WaifuPicsScreen() {
        WaifuPicsScreenContent(
            state = picsViewModel.state.collectAsState().value,
            // bun = bun,
            onWaifuClicked = { mainState.onWaifuPicsClicked(it) },
            onRequestMore = { onLoadMoreWaifusIm() },
            onFabClick = {
                picsViewModel.onClearPicsDatabase()
                requireActivity().onBackPressedDispatcher.onBackPressed()
                Toast.makeText(requireContext(), getString(R.string.waifus_gone), Toast.LENGTH_SHORT).show()
            }
        )
    }

    @Composable
    private fun WaifuNekosScreen() {
        WaifuBestScreenContent(
            state = bestViewModel.state.collectAsState().value,
            // bun = bun,
            onWaifuClicked = { mainState.onWaifuBestClicked(it) },
            onRequestMore = { onLoadMoreWaifusIm() },
            onFabClick = {
                bestViewModel.onClearDatabase()
                requireActivity().onBackPressedDispatcher.onBackPressed()
                Toast.makeText(requireContext(), getString(R.string.waifus_gone), Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun loadCustomResult(bun: Bundle) {
        val isNsfw = bun.getBoolean(Constants.IS_NSFW_WAIFU)
        val isGif = bun.getBoolean(Constants.IS_GIF_WAIFU)
        val orientation = bun.getBoolean(Constants.IS_LANDS_WAIFU)
        val categoryTag = bun.getString(Constants.CATEGORY_TAG_WAIFU) ?: ""

        if (serverMode == getString(R.string.server_normal_string)) {
            when (categoryTag) {
                "uniform", "maid", "marin-kitagawa", "oppai" -> {
                    imViewModel.onImReady(isNsfw, isGif = false, categoryTag, orientation)
                }
                "mori-calliope", "raiden-shogun" -> {
                    imViewModel.onImReady(isNsfw, isGif = false, categoryTag, false)
                }
                else -> {
                    imViewModel.onImReady(isNsfw, isGif, categoryTag, orientation)
                }
            }
        } else if (serverMode == getString(R.string.server_enhanced_string)) {
            picsViewModel.onPicsReady(isNsfw, categoryTag)
        } else {
            bestViewModel.onBestReady(categoryTag)
        }
    }

    private fun onLoadMoreWaifusIm() {
        val isNsfw = bun.getBoolean(Constants.IS_NSFW_WAIFU)
        val isGif = bun.getBoolean(Constants.IS_GIF_WAIFU)
        val orientation = bun.getBoolean(Constants.IS_LANDS_WAIFU)
        val categoryTag = bun.getString(Constants.CATEGORY_TAG_WAIFU) ?: ""
        // parece que es victima de la recomposicion
        // imViewModel.onRequestMore(isNsfw, isGif, categoryTag, orientation)
        simpleToast(getString(R.string.waifus_coming))
    }

    private infix fun FragmentWaifuBinding.withImUpdateUI(state: WaifuImViewModel.UiState) {
        var count: Int
        val isNsfw = bun.getBoolean(Constants.IS_NSFW_WAIFU)
        val isGif = bun.getBoolean(Constants.IS_GIF_WAIFU)
        val orientation = bun.getBoolean(Constants.IS_LANDS_WAIFU)
        val categoryTag = bun.getString(Constants.CATEGORY_TAG_WAIFU) ?: ""

        state.waifus?.let { savedImWaifus ->
            appendProgress.visibility = View.GONE
            // waifuImAdapter.submitList(savedImWaifus)
            count = savedImWaifus.size
            if (count != 0 && !numIsShowed) {
                Toast.makeText(requireContext(), "${getString(R.string.waifus_size)} $count", Toast.LENGTH_SHORT).show()
                numIsShowed = true
            }
        }

        /*state.error?.let {
            error = mainState.errorToString(it)
            ivError.visibility = View.VISIBLE
            tvError.visibility = View.VISIBLE
            Toast.makeText(requireContext(), mainState.errorToString(it), Toast.LENGTH_SHORT).show()
            Log.e(Constants.CATEGORY_TAG_WAIFU_IM_ERROR, mainState.errorToString(it))
        }*/

        /*state.isLoading?.let {
            loading = it
        }*/

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

        /*fabDelete.setOnClickListener {
            imViewModel.onClearImDatabase()
            requireActivity().onBackPressedDispatcher.onBackPressed()
            Toast.makeText(requireContext(), getString(R.string.waifus_gone), Toast.LENGTH_SHORT).show()
        }*/
    }

    /*private infix fun FragmentWaifuBinding.withBestUpdateUI(state: WaifuBestViewModel.UiState) {
        var count: Int
        val categoryTag = bun.getString(Constants.CATEGORY_TAG_WAIFU) ?: ""

        state.waifus?.let { savedWaifus ->
            appendProgress.visibility = View.GONE
            // waifuBestAdapter.submitList(savedWaifus)
            count = savedWaifus.size
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
                        bestViewModel.onRequestMore(categoryTag)
                        loadingMore = true
                        Handler(Looper.getMainLooper()).postDelayed({
                            loadingMore = false
                        }, 3000)
                    }
                }
            }
        })

        fabDelete.setOnClickListener {
            bestViewModel.onClearDatabase()
            requireActivity().onBackPressedDispatcher.onBackPressed()
            Toast.makeText(requireContext(), getString(R.string.waifus_gone), Toast.LENGTH_SHORT).show()
        }
    }*/

    /*private infix fun FragmentWaifuBinding.withPicsUpdateUI(state: WaifuPicsViewModel.UiState) {
        var count: Int
        val isNsfw = bun.getBoolean(Constants.IS_NSFW_WAIFU)
        val categoryTag = bun.getString(Constants.CATEGORY_TAG_WAIFU) ?: ""

        state.waifus?.let { savedPicWaifus ->
            appendProgress.visibility = View.GONE
            // waifuPicsAdapter.submitList(savedPicWaifus)
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
    }*/

    private fun simpleToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }
}

