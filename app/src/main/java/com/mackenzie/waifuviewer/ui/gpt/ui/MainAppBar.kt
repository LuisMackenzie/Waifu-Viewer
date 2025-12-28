package com.mackenzie.waifuviewer.ui.gpt.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mackenzie.waifuviewer.R

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar() {
    TopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        actions = {
            AppBarAction(Icons.Default.Search, onClick = { /*TODO*/ })
            AppBarAction(Icons.Default.Settings, onClick = { /*TODO*/ })
        },
        navigationIcon = {
            NavigationMenuButton(Icons.Default.Menu, onClick = { /*TODO*/ })
        }
    )
}

@Composable
private fun AppBarAction(
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    IconButton(onClick = {onClick()}) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = contentColorFor(MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp)
        )
    }
}

@Composable
private fun NavigationMenuButton(
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    IconButton(
        onClick = { onClick() }
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = contentColorFor(MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp)
        )
    }
}