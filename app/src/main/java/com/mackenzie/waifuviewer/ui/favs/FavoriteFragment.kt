package com.mackenzie.waifuviewer.ui.favs

import android.os.Bundle
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
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.databinding.FragmentFavoriteBinding
import com.mackenzie.waifuviewer.ui.common.Constants
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
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MainTheme {
                    LaunchFavoriteScreen()
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun LaunchFavoriteScreen() {
        FavoriteScreenContent(
            state = viewModel.state.collectAsState().value,
            onItemClick = { mainState.onWaifuFavoriteClicked(it) },
            onItemLongClick = { viewModel.onDeleteFavorite(it); simpleToast(getString(R.string.waifu_deleted)) },
            onFabClick = { viewModel.onDeleteAllFavorites(); simpleToast(getString(R.string.waifus_favorites_gone)) },
            hideInfoCount = { viewModel.hideInfoCount() }
        )
    }

    private infix fun FragmentFavoriteBinding.updateUI(state: FavoriteViewModel.UiState) {





        state.error?.let {
            error = mainState.errorToString(it)
            ivError.visibility = View.VISIBLE
            tvError.visibility = View.VISIBLE
            Toast.makeText(requireContext(), mainState.errorToString(it), Toast.LENGTH_SHORT).show()
            Log.e(Constants.CATEGORY_TAG_FAVORITE, mainState.errorToString(it))
        }





    }

    private fun simpleToast(msg: String = getString(R.string.waifu_favorite_delete)) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }
}