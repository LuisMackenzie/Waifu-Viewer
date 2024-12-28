package com.mackenzie.waifuviewer.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mackenzie.waifuviewer.WaifuPicsViewModel
import com.mackenzie.waifuviewer.domain.ServerType.ENHANCED
import com.mackenzie.waifuviewer.domain.ServerType.NEKOS
import com.mackenzie.waifuviewer.domain.ServerType.NORMAL
import com.mackenzie.waifuviewer.ui.detail.ui.DetailBestScreenContent
import com.mackenzie.waifuviewer.ui.detail.ui.DetailFavsScreenContent
import com.mackenzie.waifuviewer.ui.detail.ui.DetailImScreenContent
import com.mackenzie.waifuviewer.ui.detail.ui.DetailPicsScreenContent
import com.mackenzie.waifuviewer.ui.favs.FavoriteViewModel
import com.mackenzie.waifuviewer.ui.main.WaifuBestViewModel
import com.mackenzie.waifuviewer.ui.main.WaifuImViewModel

@Composable
fun DetailScreenContentRoute(
    imViewModel: WaifuImViewModel = hiltViewModel(),
    picsViewModel : WaifuPicsViewModel = hiltViewModel(),
    bestViewModel: WaifuBestViewModel = hiltViewModel(),
    favsViewModel: FavoriteViewModel = hiltViewModel()
) {



    if (isFav) {
        DetailFavsScreenContent(
            state = favsViewModel.state.collectAsStateWithLifecycle().value,
            onFavoriteClicked = { favsViewModel.onFavoriteClicked() },
            onSearchClick = { favsViewModel.onSearchClicked(it) }
        )
    }
    else {
        when (mode) {
            NORMAL -> {
                DetailImScreenContent(
                    state = imViewModel.state.collectAsStateWithLifecycle().value,
                    onFavoriteClicked = { imViewModel.onFavoriteClicked() },
                    onSearchClick = { imViewModel.onSearchClicked(it) }
                )
            }
            ENHANCED -> {
                DetailPicsScreenContent(
                    state = picsViewModel.state.collectAsStateWithLifecycle().value,
                    onFavoriteClicked = { picsViewModel.onFavoriteClicked() },
                    onSearchClick = { picsViewModel.onSearchClicked(it) }
                )
            }
            NEKOS -> {
                DetailBestScreenContent(
                    state = bestViewModel.state.collectAsStateWithLifecycle().value,
                    onFavoriteClicked = { bestViewModel.onFavoriteClicked() },
                    // El servicio no permite enviar imagenes de este server
                    onSearchClick = { notReady() } //  { bestViewModel.onSearchClicked(it) }
                )
            }
            else -> {}
        }
    }


}