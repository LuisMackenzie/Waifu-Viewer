package com.mackenzie.waifuviewer.ui.detail

import android.app.Activity
import android.content.Context
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.ServerType.ENHANCED
import com.mackenzie.waifuviewer.domain.ServerType.NEKOS
import com.mackenzie.waifuviewer.domain.ServerType.NORMAL
import com.mackenzie.waifuviewer.domain.getTypes
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.common.showToast
import com.mackenzie.waifuviewer.ui.detail.ui.DetailBestScreenContent
import com.mackenzie.waifuviewer.ui.detail.ui.DetailFavsScreenContent
import com.mackenzie.waifuviewer.ui.detail.ui.DetailImScreenContent
import com.mackenzie.waifuviewer.ui.detail.ui.DetailPicsScreenContent

@Composable
internal fun DetailScreenContentRoute(
    waifuId: Int,
    isFavorite: Boolean,
    imViewModel: DetailImViewModel = hiltViewModel(),
    picsViewModel : DetailPicsViewModel = hiltViewModel(),
    bestViewModel: DetailBestViewModel = hiltViewModel(),
    favsViewModel: DetailFavsViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val sharedPref = (LocalActivity.current as Activity).getPreferences(Context.MODE_PRIVATE)
    requireNotNull(sharedPref)
    val serverMode = sharedPref.getString(Constants.SERVER_MODE, "") ?: ""

    if (isFavorite) {
        favsViewModel.getWaifuResultNavigate(waifuId)
        DetailFavsScreenContent(
            state = favsViewModel.state.collectAsStateWithLifecycle().value,
            onFavoriteClicked = { favsViewModel.onFavoriteClicked() },
            onSearchClick = { favsViewModel.onSearchClicked(it) }
        )
    } else {
        when (serverMode.getTypes()) {
            NORMAL -> {
                imViewModel.getWaifuResultNavigate(waifuId)
                DetailImScreenContent(
                    state = imViewModel.state.collectAsStateWithLifecycle().value,
                    onFavoriteClicked = { imViewModel.onFavoriteClicked() },
                    onSearchClick = { imViewModel.onSearchClicked(it) }
                )
            }
            ENHANCED -> {
                picsViewModel.getWaifuResultNavigate(waifuId)
                DetailPicsScreenContent(
                    state = picsViewModel.state.collectAsStateWithLifecycle().value,
                    onFavoriteClicked = { picsViewModel.onFavoriteClicked() },
                    onSearchClick = { picsViewModel.onSearchClicked(it) }
                )
            }
            NEKOS -> {
                bestViewModel.getWaifuResultNavigate(waifuId)
                DetailBestScreenContent(
                    state = bestViewModel.state.collectAsStateWithLifecycle().value,
                    onFavoriteClicked = { bestViewModel.onFavoriteClicked() },
                    // El servicio no permite enviar imagenes de este server
                    onSearchClick = { notReady(context) } //  { bestViewModel.onSearchClicked(it) }
                )
            }
            else -> {}
        }
    }
}

private fun notReady(ctx: Context) {
    getString(ctx, R.string.function_not_implemented).showToast(ctx)
}