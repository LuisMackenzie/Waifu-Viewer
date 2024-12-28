package com.mackenzie.waifuviewer.ui.favs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.getString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.ui.common.showToast
import com.mackenzie.waifuviewer.ui.common.ui.isNavigationBarVisible
import com.mackenzie.waifuviewer.ui.common.ui.previewFavoriteState
import com.mackenzie.waifuviewer.ui.detail.ui.LoadingAnimation
import com.mackenzie.waifuviewer.ui.detail.ui.LoadingAnimationError
import com.mackenzie.waifuviewer.ui.favs.FavoriteViewModel
import com.mackenzie.waifuviewer.ui.main.ui.LoadingErrorView
import com.mackenzie.waifuviewer.ui.theme.Dimens

@Composable
fun FavoriteScreenContentRoute(vm: FavoriteViewModel = hiltViewModel()) {

    val context = LocalContext.current

    FavoriteScreenContent(
        state = vm.state.collectAsStateWithLifecycle().value,
        onItemClick = {}, // { mainState.onWaifuFavoriteClicked(it) },
        onItemLongClick = {
            vm.onDeleteFavorite(it)
            getString(context, R.string.waifu_deleted).showToast(context)
        },
        onFabClick = {
            vm.onDeleteAllFavorites()
            getString(context,R.string.waifus_favorites_gone).showToast(context)
        },
        hideInfoCount = { vm.hideInfoCount() }
    )

}

@Preview(showBackground = true)
@Composable
fun FavoriteScreenContent(
    state: FavoriteViewModel.UiState = previewFavoriteState(),
    onItemClick: (FavoriteItem) -> Unit = { },
    onItemLongClick: (FavoriteItem) -> Unit = { },
    onFabClick: () -> Unit = { },
    hideInfoCount: () -> Unit = { }
) {

    var openAlertDialog by remember { mutableStateOf(false) }

    if (openAlertDialog) {
        WaifuDialog(
            onDismissRequest = { openAlertDialog = it },
            onConfirmation = { onFabClick() ; openAlertDialog = false }
        )
    }

    if (state.isLoading) LoadingAnimation(modifier = Modifier.fillMaxSize())

    state.waifus?.let { waifus ->
        val count = waifus.size
        if (count != 0 && !state.isShowedInfo) {
            // "${stringResource(id = R.string.waifus_size)} $count".showToast(LocalContext.current)
            hideInfoCount()
        }
        Box(
            modifier = if (isNavigationBarVisible()) {
                Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .background(MaterialTheme.colorScheme.background)
            } else {
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            }
        ) {
            if (waifus.isEmpty()) LoadingAnimationError(modifier = Modifier.fillMaxSize())
            FavoriteWaifuList(
                items = waifus.reversed(),
                onItemClick = { onItemClick(it) },
            ) { onItemLongClick(it) }
            if (waifus.isNotEmpty()) FloatingActionButton(
                onClick = { openAlertDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(Dimens.fabDeletePadding),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_delete),
                    contentDescription = null
                )
            }
        }
    }

    state.error?.let { error ->
        LoadingErrorView(error = error.toString())
    }
}