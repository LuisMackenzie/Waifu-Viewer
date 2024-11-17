package com.mackenzie.waifuviewer.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.navArgs
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.WaifuPicsViewModel
import com.mackenzie.waifuviewer.domain.LoadingState
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.ENHANCED
import com.mackenzie.waifuviewer.domain.ServerType.NEKOS
import com.mackenzie.waifuviewer.domain.ServerType.NORMAL
import com.mackenzie.waifuviewer.domain.getTypes
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.common.showToast
import com.mackenzie.waifuviewer.ui.main.ui.MainTheme
import com.mackenzie.waifuviewer.ui.main.ui.WaifuBestScreenContent
import com.mackenzie.waifuviewer.ui.main.ui.WaifuImScreenContent
import com.mackenzie.waifuviewer.ui.main.ui.WaifuPicsScreenContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WaifuFragment : Fragment() {

    private val safeArgs: WaifuFragmentArgs by navArgs()
    private val picsViewModel: WaifuPicsViewModel by viewModels()
    private val imViewModel: WaifuImViewModel by viewModels()
    private val bestViewModel: WaifuBestViewModel by viewModels()
    private lateinit var mainState: MainState
    private lateinit var bun: Bundle
    private var serverMode: String = ""
    private var lmState: LoadingState = LoadingState()

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
            else -> {}
        }
    }

    @Composable
    private fun WaifuImScreen() {
        WaifuImScreenContent(
            state = imViewModel.state.collectAsStateWithLifecycle().value,
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
            state = picsViewModel.state.collectAsStateWithLifecycle().value,
            // bun = bun,
            onWaifuClicked = { mainState.onWaifuPicsClicked(it) },
            onRequestMore = { onLoadMoreWaifusPics() },
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
            state = bestViewModel.state.collectAsStateWithLifecycle().value,
            onWaifuClicked = { mainState.onWaifuBestClicked(it) },
            onRequestMore = { onLoadMoreWaifusBest() },
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
        if (!lmState.loadMoreIm) {
            imViewModel.onRequestMore(isNsfw, isGif, categoryTag , orientation)
            lmState.loadMoreIm = true
            getString(R.string.waifus_coming).showToast(requireContext())
            resetLoadingMore(0)
        }
    }

    private fun onLoadMoreWaifusPics() {
        val isNsfw = bun.getBoolean(Constants.IS_NSFW_WAIFU)
        val categoryTag = bun.getString(Constants.CATEGORY_TAG_WAIFU) ?: ""
        if (!lmState.loadMorePics ) {
            picsViewModel.onRequestMore(isNsfw, categoryTag)
            lmState.loadMorePics = true
            getString(R.string.waifus_coming).showToast(requireContext())
            resetLoadingMore(1)
        }
    }

    private fun onLoadMoreWaifusBest() {
        val categoryTag = bun.getString(Constants.CATEGORY_TAG_WAIFU) ?: ""
        if (!lmState.loadMoreBest ) {
            bestViewModel.onRequestMore(categoryTag)
            lmState.loadMoreBest = true
            getString(R.string.waifus_coming).showToast(requireContext())
            resetLoadingMore(2)
        }
    }

    private fun resetLoadingMore(id :Int) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(6000)
            when (id) {
                0 -> lmState.loadMoreIm = false
                1 -> lmState.loadMorePics = false
                2 -> lmState.loadMoreBest = false
                else -> {lmState = LoadingState()}
            }
        }
    }
}

