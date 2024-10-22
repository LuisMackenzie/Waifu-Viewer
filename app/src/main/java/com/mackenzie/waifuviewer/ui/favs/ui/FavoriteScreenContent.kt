package com.mackenzie.waifuviewer.ui.favs.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.ui.detail.ui.LoadingAnimation
import com.mackenzie.waifuviewer.ui.detail.ui.LoadingAnimationError
import com.mackenzie.waifuviewer.ui.favs.FavoriteViewModel
import com.mackenzie.waifuviewer.ui.main.ui.LoadingErrorView

@Composable
fun FavoriteScreenContent(
    state: FavoriteViewModel.UiState,
    onItemClick: (FavoriteItem) -> Unit,
    onItemLongClick: (FavoriteItem) -> Unit,
    onFabClick: () -> Unit,
    hideInfoCount: () -> Unit
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
        Box(modifier = Modifier.fillMaxSize()) {
            if (waifus.isEmpty()) LoadingAnimationError(modifier = Modifier.fillMaxSize())
            FavoriteWaifuList(
                items = waifus.reversed(),
                onItemClick = { onItemClick(it) },
            ) { onItemLongClick(it) }
            FloatingActionButton(
                onClick = { openAlertDialog = true },
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

    state.error?.let { error ->
        LoadingErrorView(error = error.toString())
    }
}