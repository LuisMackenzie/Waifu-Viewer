package com.mackenzie.waifuviewer.ui.detail.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.ui.theme.Dimens

@Preview(showBackground = true)
@Composable
fun DetailFabDownload(
    onDownloadClick: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = { onDownloadClick() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Dimens.detailFabsPadding),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_download),
                contentDescription = null
            )
        }
    }
}