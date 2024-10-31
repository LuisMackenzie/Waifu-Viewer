package com.mackenzie.waifuviewer.ui.detail.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mackenzie.waifuviewer.ui.theme.Dimens

@Preview(showBackground = true)
@Composable
fun DetailErrorTitle(
    error: String = "error",
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(Dimens.detailTitlePadding),
            text = error,
            fontSize = Dimens.detailTitleFontSize,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
        )
    }
}