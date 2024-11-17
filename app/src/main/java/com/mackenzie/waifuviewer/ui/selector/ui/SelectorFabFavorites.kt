package com.mackenzie.waifuviewer.ui.selector.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.ui.main.ui.MainTheme
import com.mackenzie.waifuviewer.ui.theme.Dimens
import com.mackenzie.waifuviewer.ui.theme.WaifuViewerTheme

@Composable
fun SelectorFabFavorites(
    onFavoriteClicked: () -> Unit = {},
    onRestartClicked: () -> Unit = {},
    onGptClicked: () -> Unit = {},
    onGeminiClicked: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            AnimatedVisibility(
                visible = expanded,
            ) {

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    FabMenuItem(
                        onButtonClicked = onGptClicked,
                        icon = R.drawable.ic_waifu_gpt
                    )
                    FabMenuItem(
                        onButtonClicked = onGeminiClicked,
                        icon = R.drawable.ic_waifu_gemini
                    )
                    FabMenuItem(
                        onButtonClicked = onRestartClicked,
                        icon = R.drawable.ic_baseline_update
                    )
                    FabMenuItem(
                        onButtonClicked = onFavoriteClicked,
                        icon = R.drawable.ic_favorite_on
                    )
                }
            }

            FabMenuItem(
                onButtonClicked = { expanded = !expanded },
                icon = R.drawable.ic_baseline_add_24
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    WaifuViewerTheme(darkTheme = true) {
        SelectorFabFavorites()
    }
}