package com.mackenzie.waifuviewer.ui.selector.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.ui.common.ui.previewSelectorState
import com.mackenzie.waifuviewer.ui.selector.SelectorViewModel
import com.mackenzie.waifuviewer.ui.theme.Dimens
import com.mackenzie.waifuviewer.ui.theme.WaifuViewerTheme

@Composable
fun SelectorScreenContent(
    state : SelectorViewModel.UiState = previewSelectorState(),
    onServerButtonClicked: () -> Unit = {},
    onWaifuButtonClicked: (String) -> Unit = {},
    onFavoriteClicked: () -> Unit = {},
    onRestartClicked: () -> Unit = {},
    onGptClicked: () -> Unit = {},
    onGeminiClicked: () -> Unit = {},
    switchStateCallback: (Triple<Boolean, Boolean, Boolean>) -> Unit = {},
    switchState: Triple<Boolean, Boolean, Boolean> = Triple(false, false, false),
    tags: Triple<Pair<Array<String>, Array<String>>, Pair<Array<String>, Array<String>>, Pair<Array<String>, Array<String>>> = Triple(Pair(arrayOf(), arrayOf()), Pair(arrayOf(), arrayOf()), Pair(arrayOf(), arrayOf())),
    backgroundState: (Boolean) -> Unit = {},
    server: ServerType = ServerType.NORMAL,
) {

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
            Log.e("Selector State Error", "Error: $it")
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
            tags = tags,
            onServerButtonClicked = onServerButtonClicked,
            onWaifuButtonClicked = onWaifuButtonClicked,
            switchState = switchState,
            server = server
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
            switchStateCallback = switchStateCallback,
            server = server
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