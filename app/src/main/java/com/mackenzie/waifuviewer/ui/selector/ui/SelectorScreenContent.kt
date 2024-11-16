package com.mackenzie.waifuviewer.ui.selector.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.android.material.color.MaterialColors
import com.google.firebase.annotations.concurrent.Background
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.common.isLandscape
import com.mackenzie.waifuviewer.ui.common.ui.previewSelectorState
import com.mackenzie.waifuviewer.ui.selector.SelectorViewModel
import com.mackenzie.waifuviewer.ui.theme.Dimens
import com.mackenzie.waifuviewer.ui.theme.WaifuViewerTheme

@Composable
fun SelectorScreenContent(
    state : SelectorViewModel.UiState = previewSelectorState(),
    onWaifuButtonClicked: (String) -> Unit = {},
    onFavoriteClicked: () -> Unit = {},
    onRestartClicked: () -> Unit = {},
    onGptClicked: () -> Unit = {},
    onGeminiClicked: () -> Unit = {},
    nsfwState: (Boolean) -> Unit = {},
    gifState: (Boolean) -> Unit = {},
    portraitState: (Boolean) -> Unit = {},
    backgroundState: (Boolean) -> Unit = {},
) {

    // var nsfwSwitchState by remember { mutableStateOf(nsfwStateSwitch) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        state.waifuIm?.let { waifu ->
            BackgroundImage(waifu.url)
            backgroundState(true)
        }
        state.waifuPic?.let { waifu ->
            BackgroundImage(waifu.url)
            backgroundState(true)
        }
        state.waifuNeko?.let { waifu ->
            BackgroundImage(waifu.url)
            backgroundState(true)
        }

        state.tags?.let {

        }

        state.error?.let {
            BackgroundImageError(R.drawable.ic_offline_background)
        }

        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(Dimens.homeTitlePadding),
            text = stringResource(id = R.string.waifu_viewer),
            color = Color.White,
            fontSize = Dimens.homeTitleFontSize,
            style = MaterialTheme.typography.bodyMedium
        )
        SelectorMainButtons(
            categorias = Pair(Constants.NORMALSFW, Constants.NORMALNSFW),
            onWaifuButtonClicked = onWaifuButtonClicked,
            // nsfwState = nsfwStateSwitch
        )

        SelectorFabFavorites(
            onFavoriteClicked = onFavoriteClicked,
            onRestartClicked = onRestartClicked,
            onGptClicked = onGptClicked,
            onGeminiClicked = onGeminiClicked
        )

        SelectorSwitches(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(all = Dimens.homeSwitchesPadding)
                .padding(end = Dimens.homeSwitchesPaddingEnd),
            nsfwState = nsfwState,
            gifState = gifState,
            portraitState = portraitState,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSelector() {
    WaifuViewerTheme(darkTheme = false) {
        SelectorScreenContent()
    }
}