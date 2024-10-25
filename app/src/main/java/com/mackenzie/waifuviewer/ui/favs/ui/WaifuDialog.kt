package com.mackenzie.waifuviewer.ui.favs.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mackenzie.waifuviewer.R

@Preview(showBackground = true)
@Composable
fun WaifuDialog(
    onDismissRequest: (Boolean) -> Unit = { },
    onConfirmation: () -> Unit = { },
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest(false) },
        title = { Text(text = stringResource(id = R.string.favorite_delete_title)) },
        text = { Text(text = stringResource(id = R.string.favorite_delete_subtitle)) },
        icon = { Icon(painter = painterResource(id = R.drawable.ic_baseline_delete), contentDescription = "Delete Icon") },
        // icon = { Image(painter = painterResource(id = R.mipmap.ic_launcher), contentDescription = "Example Icon") },
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