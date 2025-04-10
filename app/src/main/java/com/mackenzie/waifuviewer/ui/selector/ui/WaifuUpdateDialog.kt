package com.mackenzie.waifuviewer.ui.selector.ui

import androidx.compose.foundation.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mackenzie.waifuviewer.BuildConfig
import com.mackenzie.waifuviewer.R

@Preview(showBackground = true)
@Composable
fun WaifuUpdateDialog(
    latestVersion: String = "0.0.0",
    onDismissRequest: (Boolean) -> Unit = { },
    onConfirmation: () -> Unit = { },
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest(false) },
        title = { Text(text = stringResource(id = R.string.dialog_update_title)) },
        text = { Text(
            text = stringResource(id = R.string.dialog_update_subtitle)
                    + BuildConfig.VERSION_NAME
                    + stringResource(id = R.string.dialog_update_subtitle_hint) + latestVersion
        ) },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_splash),
                contentDescription = "Example Icon"
            )
        },
        // icon = { Image(painter = painterResource(id = R.mipmap.ic_launcher), contentDescription = "Example Icon") },
        confirmButton = {
            Button(onClick = {
                onConfirmation()
            }) {
                Text(text = stringResource(id = R.string.dialog_update_accept) + stringResource(id = R.string.app_name))
            }
        }
    )
}