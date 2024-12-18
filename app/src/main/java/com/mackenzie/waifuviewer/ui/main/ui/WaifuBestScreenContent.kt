package com.mackenzie.waifuviewer.ui.main.ui

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
import androidx.compose.ui.res.painterResource
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.WaifuBestItem
import com.mackenzie.waifuviewer.ui.common.ui.isNavigationBarVisible
import com.mackenzie.waifuviewer.ui.detail.ui.LoadingAnimation
import com.mackenzie.waifuviewer.ui.favs.ui.WaifuDialog
import com.mackenzie.waifuviewer.ui.main.WaifuBestViewModel
import com.mackenzie.waifuviewer.ui.theme.Dimens

@Composable
fun WaifuBestScreenContent(
    state: WaifuBestViewModel.UiState,
    onWaifuClicked: (WaifuBestItem) -> Unit,
    onRequestMore: () -> Unit,
    onFabClick: () -> Unit
) {
    var openAlertDialog by remember { mutableStateOf(false) }

    if (openAlertDialog) {
        WaifuDialog(
            onDismissRequest = { openAlertDialog = it },
            onConfirmation = { onFabClick() ; openAlertDialog = false }
        )
    }

    state.waifus?.let { waifus ->
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
            if (waifus.isEmpty()) LoadingAnimation(modifier = Modifier.fillMaxSize())
            WaifuBestList(
                items = waifus,
                onItemClick = { onWaifuClicked(it) },
                onLoadMore = { onRequestMore() },
            )
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