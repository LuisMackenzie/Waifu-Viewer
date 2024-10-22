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
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.favs.ui.FavoriteScreenContent
import com.mackenzie.waifuviewer.ui.main.MainState
import com.mackenzie.waifuviewer.ui.main.buildMainState
import com.mackenzie.waifuviewer.ui.main.ui.MainTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(), FavoriteAdapter.OnItemClickListener {

    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var mainState: MainState
    private var numIsShowed : Boolean = false

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
            onItemClick = { onClick(it) },
            onItemLongClick = { onLongClick(it) },
            onFabClick = { onFabClick() }
        )
    }


    private infix fun FragmentFavoriteBinding.updateUI(state: FavoriteViewModel.UiState) {

        var count: Int

        state.waifus?.let { favoritesWaifus ->
            /*if (favoritesWaifus.isEmpty()) {
                ivError.visibility = View.VISIBLE
            }*/
            // favoriteAdapter.submitList(favoritesWaifus.reversed())
            // waifuPic = savedPicWaifus
            count = favoritesWaifus.size
            if (count != 0 && !numIsShowed) {
                simpleToast("${getString(R.string.waifus_size)} $count")
                numIsShowed = true
            }
        }

        state.error?.let {
            error = mainState.errorToString(it)
            ivError.visibility = View.VISIBLE
            tvError.visibility = View.VISIBLE
            Toast.makeText(requireContext(), mainState.errorToString(it), Toast.LENGTH_SHORT).show()
            Log.e(Constants.CATEGORY_TAG_FAVORITE, mainState.errorToString(it))
        }

    }

    private fun onFabClick() {
        viewModel.onDeleteAllFavorites()
        simpleToast(getString(R.string.waifus_favorites_gone))
    }

    private fun simpleToast(msg: String = getString(R.string.waifu_favorite_delete)) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    override fun onClick(waifu: FavoriteItem) {
        mainState.onWaifuFavoriteClicked(waifu)
    }

    override fun onLongClick(waifu: FavoriteItem) {
        viewModel.onDeleteFavorite(waifu)
        Toast.makeText(requireContext(), getString(R.string.waifu_deleted), Toast.LENGTH_SHORT).show()
    }
}