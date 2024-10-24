package com.mackenzie.waifuviewer.ui.main.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.ui.main.WaifuImViewModel

@Preview(showBackground = true)
@Composable
fun ShowWaifuScreenPreview() {
    WaifuImScreenContent(
        state = previewState(),
        onWaifuClicked = { },
        onRequestMore = { },
        onFabClick = { }
    )
}

@Composable
private fun previewState() = WaifuImViewModel.UiState(
    waifus = getMedia(),
    error = null
)

fun getMedia() = (1..20).map {
    WaifuImItem(
        id = it,
        signature = "signature",
        extension = "jpg",
        dominantColor = "dominantColor",
        source = "source",
        uploadedAt = "uploadedAt",
        isNsfw = false,
        width = "width",
        height = "height",
        imageId = it,
        url = "https://nekos.best/api/v2/neko/f09f1d72-4d7d-43ac-9aec-79f0544b95c3.png",
        previewUrl = "previewUrl",
        isFavorite = if(it % 3 == 0) true else false
    )
}