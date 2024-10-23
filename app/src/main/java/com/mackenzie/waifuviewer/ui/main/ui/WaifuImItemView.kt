package com.mackenzie.waifuviewer.ui.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.ui.favs.ui.ShimmerEffect

@Composable
fun WaifuImItemView(
    waifu: WaifuImItem,
    modifier: Modifier = Modifier,
    onWaifuClick: (WaifuImItem) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.waifu_item_height))
            .clickable { onWaifuClick(waifu) },
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.default_corner))
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
                text = waifu.imageId.toString(),
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(Color.Transparent)
                    .padding(bottom = 8.dp)
            )

            if (waifu.isFavorite) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_favorite_on),
                    contentDescription = stringResource(id = R.string.waifus_content),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(58.dp)
                        .align(Alignment.BottomStart)
                        .padding(start = 20.dp, bottom = 8.dp)
                )
            }
        }
    }
}