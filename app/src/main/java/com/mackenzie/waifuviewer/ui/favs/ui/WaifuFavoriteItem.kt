package com.mackenzie.waifuviewer.ui.favs.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.ui.common.isLandscape
import com.mackenzie.waifuviewer.ui.common.ui.getFavoriteMediaItem
import com.mackenzie.waifuviewer.ui.theme.Dimens

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun WaifuFavoriteItem(
    waifu: FavoriteItem = getFavoriteMediaItem(),
    modifier: Modifier = Modifier,
    onWaifuClick: (FavoriteItem) -> Unit = {},
    onWaifuLongClick: (FavoriteItem) -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(
                if (LocalContext.current.isLandscape()) Dimens.itemHeightLandscape
                else Dimens.itemHeight
            )
            .combinedClickable(
                onLongClick = { onWaifuLongClick(waifu) },
                onClick = { onWaifuClick(waifu) }
            ),
            // .clickable { onWaifuClick(waifu) },
        elevation = CardDefaults.cardElevation(Dimens.cardElevation),
        shape = RoundedCornerShape(Dimens.defaultCorner)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ShimmerEffect(
                modifier = Modifier.fillMaxSize()
            )
            AsyncImage(
                model= ImageRequest.Builder(LocalContext.current)
                    .data(waifu.url)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_error_grey),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )

            Text(
                text = waifu.title,
                fontSize = Dimens.cardTextSize,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(Color.Transparent)
                    .padding(bottom = Dimens.cardPaddingBottom)
            )

            Icon(
                painter = if (waifu.isFavorite) painterResource(id = R.drawable.ic_favorite_on) else painterResource(id = R.drawable.ic_favorite_off),
                contentDescription = stringResource(id = R.string.waifus_content),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(Dimens.cardIconSize)
                    .align(Alignment.BottomStart)
                    .padding(start = Dimens.cardIconPaddingStart, bottom = Dimens.cardPaddingBottom)
            )
        }
    }
}