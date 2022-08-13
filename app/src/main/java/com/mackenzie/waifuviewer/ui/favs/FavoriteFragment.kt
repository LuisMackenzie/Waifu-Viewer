package com.mackenzie.waifuviewer.ui.favs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.databinding.FragmentFavoriteBinding
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.ui.common.launchAndCollect
import com.mackenzie.waifuviewer.ui.main.MainState
import com.mackenzie.waifuviewer.ui.main.buildMainState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite), FavoriteAdapter.OnItemClickListener {

    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var mainState: MainState
    private var numIsShowed : Boolean = false
    private val favoriteAdapter = FavoriteAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainState = buildMainState()

        val binding = FragmentFavoriteBinding.bind(view).apply {
            recycler.adapter = favoriteAdapter
        }

        viewLifecycleOwner.launchAndCollect(viewModel.state) { binding.updateUI(it) }

    }

    private fun FragmentFavoriteBinding.updateUI(state: FavoriteViewModel.UiState) {

        var count: Int

        state.waifus?.let { favoritesWaifus ->
            favoriteAdapter.submitList(favoritesWaifus.reversed())
            // waifuPic = savedPicWaifus
            count = favoritesWaifus.size
            if (count != 0 && !numIsShowed) {
                Toast.makeText(requireContext(), "Total Waifus = $count", Toast.LENGTH_SHORT).show()
                numIsShowed = true
            }
        }

        state.error?.let {
            mainState.errorToString(it)
            ivError.visibility = View.VISIBLE
            tvError.visibility = View.VISIBLE
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }

        state.isLoading.let {
            progress.visibility = if (it) View.VISIBLE else View.GONE
        }

        fabRecycler.setOnClickListener {
            Toast.makeText(requireContext(), "Delete All Favorites no implemented yet. Try Long Press on a waifu", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onClick(waifu: FavoriteItem) {
        mainState.onWaifuFavoriteClicked(waifu)
    }

    override fun onLongClick(waifu: FavoriteItem) {
        viewModel.onDeleteFavorite(waifu)
        Toast.makeText(requireContext(), "Waifu Deleted", Toast.LENGTH_SHORT).show()
    }

}