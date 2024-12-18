package com.mackenzie.waifuviewer.ui.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp

@Composable
fun isNavigationBarVisible(): Boolean {
    val insets = LocalView.current.rootWindowInsets
    val navBarHeightPx = insets.stableInsetBottom
    val navBarHeightDp = with(LocalDensity.current) { navBarHeightPx.toDp() }
    return navBarHeightDp > 20.dp
}