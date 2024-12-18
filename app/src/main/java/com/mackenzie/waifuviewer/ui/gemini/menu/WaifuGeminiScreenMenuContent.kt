package com.mackenzie.waifuviewer.ui.gemini.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.GeminiMenuItem
import com.mackenzie.waifuviewer.ui.common.ui.isNavigationBarVisible
import com.mackenzie.waifuviewer.ui.common.ui.menuGenerator
import com.mackenzie.waifuviewer.ui.main.ui.MainThemeForPreview

@Composable
fun WaifuGeminiScreenMenuContent(
    menuItems: List<GeminiMenuItem> = menuGenerator(),
    onItemClicked: (String) -> Unit = { }
) {

    Box(
        modifier = if (isNavigationBarVisible()) {
            Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .background(MaterialTheme.colorScheme.background)
        } else {
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        }
    ) {

        AsyncImage(
            model= ImageRequest.Builder(LocalContext.current)
                .data("https://cdn.waifu.im/7374.jpg")
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.ic_error_grey),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        LazyColumn(
            Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .align(Alignment.BottomCenter)
        ) {
            items(menuItems) { menuItem ->
                MenuItemView(
                    menuItem = menuItem,
                    onItemClicked = onItemClicked
                )
            }
        }
    }
}



@Preview(showSystemUi = true)
@Composable
fun MenuScreenPreview() {
    MainThemeForPreview(darkTheme = false) {
        WaifuGeminiScreenMenuContent()
    }
}