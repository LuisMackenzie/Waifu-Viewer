package com.mackenzie.waifuviewer.ui.common.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.mackenzie.waifuviewer.ui.common.isLandscape

@Composable
fun isNavigationBarVisible(): Boolean {
    val insets = LocalView.current.rootWindowInsets
    val navBarHeightPx = insets.stableInsetBottom
    val navBarHeightDp = with(LocalDensity.current) { navBarHeightPx.toDp() }
    // Para tabletas la barra de navegacion puede ser mayor
    if (LocalContext.current.isLandscape()) {
        return navBarHeightDp > 32.dp
    } else {
        return navBarHeightDp > 20.dp
    }
}