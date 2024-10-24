package com.mackenzie.waifuviewer.ui.detail.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mackenzie.waifuviewer.R

@Composable
fun DetailFabFavorites(
    isFavorite: Boolean,
    onFavoriteClicked: () -> Unit,
) {

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            onClick = {onFavoriteClicked()},
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Icon(
                painter = painterResource(
                    id = if (isFavorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
                ),
                contentDescription = null
            )
        }
    }
}