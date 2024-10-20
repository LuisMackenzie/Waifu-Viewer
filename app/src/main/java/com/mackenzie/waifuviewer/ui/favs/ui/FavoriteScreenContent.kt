package com.mackenzie.waifuviewer.ui.favs.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.ui.detail.DetailPicsViewModel
import com.mackenzie.waifuviewer.ui.detail.ui.LoadingAnimation
import com.mackenzie.waifuviewer.ui.detail.ui.LoadingAnimationError
import com.mackenzie.waifuviewer.ui.favs.FavoriteViewModel

@Composable
fun FavoriteScreenContent(
    state: FavoriteViewModel.UiState,
    onItemClick: (FavoriteItem) -> Unit,
    onFabClick: () -> Unit
) {

    if (state.isLoading) LoadingAnimation(modifier = Modifier.fillMaxSize())

    state.waifus?.let { waifus ->
        Box(modifier = Modifier.fillMaxSize()) {
            // LoadingAnimation(modifier = Modifier.fillMaxSize())
            FavoriteWaifuList(items = waifus.reversed()) { onItemClick(it) }
            FloatingActionButton(
                onClick = { onFabClick() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_delete),
                    contentDescription = null
                )
            }

        }
    }


    state.error?.let {
        Box(modifier = Modifier.fillMaxSize()) {
            LoadingAnimationError(modifier = Modifier.fillMaxSize())
        }
    }


}