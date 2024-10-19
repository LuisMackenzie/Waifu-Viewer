package com.mackenzie.waifuviewer.ui.detail.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.ui.detail.DetailImViewModel

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    DetailImScreenContent(
        state = previewState(),
        isPreview = true,
        prepareDownload = { title, link, imageExt ->  },
        onFavoriteClicked = {  },
        onDownloadClick = {  }
    )
}

@Composable
private fun previewState() = DetailImViewModel.UiState(
    waifuIm = WaifuImItem(
        id = 1,
        signature = "signature",
        extension = "jpg",
        dominantColor = "dominantColor",
        source = "source",
        uploadedAt = "uploadedAt",
        isNsfw = false,
        width = "width",
        height = "height",
        imageId = 11,
        url = "https://nekos.best/api/v2/neko/f09f1d72-4d7d-43ac-9aec-79f0544b95c3.png",
        previewUrl = "previewUrl",
        isFavorite = false
    ),
    error = null
)