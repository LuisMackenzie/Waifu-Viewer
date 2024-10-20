package com.mackenzie.waifuviewer.ui.favs.ui

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.FavoriteItem

@Composable
fun WaifuItem(
    waifu: FavoriteItem,
    modifier: Modifier = Modifier,
    onWaifuClick: (FavoriteItem) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(6.dp)
            .clickable { onWaifuClick(waifu) },
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.default_corner))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            /*ShimmerEffect(
                modifier = Modifier.fillMaxSize()
            )*/

            AsyncImage(
                model= ImageRequest.Builder(LocalContext.current)
                    .data(waifu.url)
                    .crossfade(true)
                    .build(),
                // placeholder = painterResource(R.drawable.baseline_downloading),
                error = painterResource(R.drawable.ic_error_grey),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )

            Text(
                text = waifu.title,
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
                    modifier = Modifier
                        .size(90.dp)
                        .align(Alignment.BottomStart)
                        .padding(start = 20.dp, bottom = 8.dp)
                )
            }
        }
    }
}