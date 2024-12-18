package com.mackenzie.waifuviewer.ui.selector.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.ui.theme.Dimens

@Composable
fun FabMenuItem(
    onButtonClicked: () -> Unit = {},
    icon: Int = R.drawable.ic_waifu_gemini
) {
    FloatingActionButton(
        modifier = Modifier
            .padding(Dimens.homeButtonFabPadding),
        onClick = { onButtonClicked() },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Icon(
            painter = painterResource(id = icon),
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = null
        )
    }
}