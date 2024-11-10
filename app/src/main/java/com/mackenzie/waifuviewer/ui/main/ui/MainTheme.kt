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

@Composable
fun MainThemeForPreview(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    WaifuViewerTheme(darkTheme = darkTheme) {
        Surface(color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}