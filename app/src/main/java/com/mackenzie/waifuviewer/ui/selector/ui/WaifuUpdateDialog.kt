package com.mackenzie.waifuviewer.ui.selector.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import com.mackenzie.waifuviewer.BuildConfig
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.common.removeVersionSuffix

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
        text = {
            Column {
                Text(text = stringResource(id = R.string.dialog_update_subtitle))
                Row {
                    Text(text = stringResource(id = R.string.dialog_update_local_version) )
                    Text(
                        text = Constants.SPACE + BuildConfig.VERSION_NAME.removeVersionSuffix(),
                        fontFamily = FontFamily.Monospace
                    )
                }
                Row {
                    Text(text = stringResource(id = R.string.dialog_update_latest_version) )
                    Text(text = Constants.SPACE + latestVersion, color = Color.Red, fontStyle = FontStyle.Italic, fontFamily = FontFamily.Monospace)
                }
            }
        },
        icon = { Image(painter = painterResource(id = R.drawable.ic_splash), contentDescription = "Update Icon") },
        confirmButton = {
            Button(onClick = {
                onConfirmation()
            }) {
                Text(text = stringResource(id = R.string.dialog_update_accept) + Constants.SPACE + stringResource(id = R.string.app_name))
            }
        }
    )
}