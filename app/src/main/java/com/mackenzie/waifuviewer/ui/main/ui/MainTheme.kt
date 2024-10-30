package com.mackenzie.waifuviewer.ui.main.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.mackenzie.waifuviewer.ui.theme.WaifuViewerTheme

@Composable
fun MainTheme(content: @Composable () -> Unit) {
    WaifuViewerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}