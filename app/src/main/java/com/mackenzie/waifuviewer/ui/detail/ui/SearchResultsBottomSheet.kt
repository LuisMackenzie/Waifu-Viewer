package com.mackenzie.waifuviewer.ui.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.AnimeSearchItem

@Composable
fun SearchResultsBottomSheet(
    searchResults: List<AnimeSearchItem>,
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(searchResults) { item ->
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(3f)) {
                    Text(
                        text = "Name: ${item.filename}",
                        // color = Color.Green,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Start

                    )
                    Text(
                        text = "Episode: ${item.episode}",
                        color = Color.Cyan,
                        textAlign = androidx.compose.ui.text.style.TextAlign.End
                    )
                }

                AsyncImage(
                    model= ImageRequest.Builder(LocalContext.current)
                        .data(item.image)
                        .crossfade(true)
                        .build(),
                    error = painterResource(R.drawable.ic_error_grey),
                    contentDescription = null,
                    // alignment = Alignment.End,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.weight(1f),
                )
            }

            // Text(text = item.image)

        }
    }
}