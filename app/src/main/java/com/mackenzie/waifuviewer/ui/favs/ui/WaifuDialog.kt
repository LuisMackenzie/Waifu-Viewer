package com.mackenzie.waifuviewer.ui.favs.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import com.mackenzie.waifuviewer.R

@Composable
fun WaifuDialog(
    onDismissRequest: (Boolean) -> Unit,
    onConfirmation: () -> Unit,
    icon: Painter,
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest(false) },
        title = { Text(text = stringResource(id = R.string.favorite_delete_title)) },
        text = { Text(text = stringResource(id = R.string.favorite_delete_subtitle)) },
        icon = { Icon(icon, contentDescription = "Example Icon") },
        confirmButton = {
            Button(onClick = {
                onConfirmation()
            }) {
                Text(text = stringResource(id = R.string.favorite_delete_accept))
            }
        },
        dismissButton = {
            Button(onClick = { onDismissRequest(false) }) {
                Text(text = stringResource(id = R.string.favorite_delete_deny))
            }
        }
    )
}