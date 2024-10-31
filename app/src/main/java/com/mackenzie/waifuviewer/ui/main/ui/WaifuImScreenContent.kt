package com.mackenzie.waifuviewer.ui.main.ui

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.ui.common.showToast
import com.mackenzie.waifuviewer.ui.common.ui.previewMainState
import com.mackenzie.waifuviewer.ui.detail.ui.LoadingAnimation
import com.mackenzie.waifuviewer.ui.favs.ui.WaifuDialog
import com.mackenzie.waifuviewer.ui.main.WaifuImViewModel
import com.mackenzie.waifuviewer.ui.theme.Dimens

@Preview(showBackground = true)
@Composable
fun WaifuImScreenContent(
    state: WaifuImViewModel.UiState = previewMainState(),
    onWaifuClicked: (WaifuImItem) -> Unit = {},
    onRequestMore: () -> Unit = {},
    onFabClick: () -> Unit = {}
) {

    var openAlertDialog by remember { mutableStateOf(false) }

    if (openAlertDialog) {
        WaifuDialog(
            onDismissRequest = { openAlertDialog = it },
            onConfirmation = { onFabClick() ; openAlertDialog = false }
        )
    }

    /*state.isLoading?.let { isloading ->
        if (isloading) LoadingAnimation(modifier = Modifier.fillMaxSize())
    }*/

    state.waifus?.let { waifus ->
        "${stringResource(id = R.string.waifus_size)} ${waifus.size}".showToast(LocalContext.current)
        /*val count = waifus.size
        if (count != 0 && !state.isShowedInfo) {
            "${stringResource(id = R.string.waifus_size)} $count".showToast(LocalContext.current)
            hideInfoCount()
        }*/
        Box(modifier = Modifier.fillMaxSize()) {
            if (waifus.isEmpty()) LoadingAnimation(modifier = Modifier.fillMaxSize())
            WaifuImList(
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