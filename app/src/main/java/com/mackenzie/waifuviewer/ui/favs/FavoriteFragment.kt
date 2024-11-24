package com.mackenzie.waifuviewer.ui.favs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.ui.common.composeView
import com.mackenzie.waifuviewer.ui.common.showToast
import com.mackenzie.waifuviewer.ui.favs.ui.FavoriteScreenContent
import com.mackenzie.waifuviewer.ui.main.MainState
import com.mackenzie.waifuviewer.ui.main.buildMainState
import com.mackenzie.waifuviewer.ui.main.ui.MainTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var mainState: MainState

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainState = buildMainState()
        return composeView {
            LaunchFavoriteScreen()
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun LaunchFavoriteScreen() {
        FavoriteScreenContent(
            state = viewModel.state.collectAsStateWithLifecycle().value,
            onItemClick = { mainState.onWaifuFavoriteClicked(it) },
            onItemLongClick = { viewModel.onDeleteFavorite(it); getString(R.string.waifu_deleted).showToast(requireContext()) },
            onFabClick = { viewModel.onDeleteAllFavorites(); getString(R.string.waifus_favorites_gone).showToast(requireContext()) },
            hideInfoCount = { viewModel.hideInfoCount() }
        )
    }
}