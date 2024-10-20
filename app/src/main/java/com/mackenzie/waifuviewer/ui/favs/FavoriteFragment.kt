package com.mackenzie.waifuviewer.ui.favs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.databinding.FragmentFavoriteBinding
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.detail.ui.LoadingAnimation
import com.mackenzie.waifuviewer.ui.detail.ui.LoadingAnimationError
import com.mackenzie.waifuviewer.ui.favs.ui.FavoriteScreenContent
import com.mackenzie.waifuviewer.ui.favs.ui.ShimmerEffect
import com.mackenzie.waifuviewer.ui.main.MainState
import com.mackenzie.waifuviewer.ui.main.buildMainState
import com.mackenzie.waifuviewer.ui.main.ui.MainTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(), FavoriteAdapter.OnItemClickListener {

    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var mainState: MainState
    private var numIsShowed : Boolean = false
    // private val favoriteAdapter = FavoriteAdapter(this)

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainState = buildMainState()

        val binding = FragmentFavoriteBinding.bind(view).apply {
            recycler.adapter = favoriteAdapter
        }

        viewLifecycleOwner.launchAndCollect(viewModel.state) { binding updateUI it }

    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainState = buildMainState()
        // return super.onCreateView(inflater, container, savedInstanceState)
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MainTheme {
                    LaunchFavoriteScreen()
                    // WaifuItem()
                }
            }
        }
    }

    @Composable
    private fun LaunchFavoriteScreen() {

        FavoriteScreenContent(
            state = viewModel.state.collectAsState().value,
            onItemClick = { onClick(it) },
            onFabClick = { fabFavoriteDelete() }
        )
    }


    private infix fun FragmentFavoriteBinding.updateUI(state: FavoriteViewModel.UiState) {

        var count: Int

        state.waifus?.let { favoritesWaifus ->
            if (favoritesWaifus.isEmpty()) {
                ivError.visibility = View.VISIBLE
            }
            // favoriteAdapter.submitList(favoritesWaifus.reversed())
            // waifuPic = savedPicWaifus
            count = favoritesWaifus.size
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
            Log.e(Constants.CATEGORY_TAG_FAVORITE, mainState.errorToString(it))
        }

        state.isLoading.let {
            progress.visibility = if (it) View.VISIBLE else View.GONE
        }

        fabRecycler.setOnClickListener {
            // Toast.makeText(requireContext(), getString(R.string.waifu_favorite_delete), Toast.LENGTH_SHORT).show()
        }

    }

    fun fabFavoriteDelete() {
        Toast.makeText(requireContext(), getString(R.string.waifu_favorite_delete), Toast.LENGTH_SHORT).show()
    }

    override fun onClick(waifu: FavoriteItem) {
        mainState.onWaifuFavoriteClicked(waifu)
    }

    override fun onLongClick(waifu: FavoriteItem) {
        viewModel.onDeleteFavorite(waifu)
        Toast.makeText(requireContext(), getString(R.string.waifu_deleted), Toast.LENGTH_SHORT).show()
    }

}