package com.mackenzie.waifuviewer.ui.selector.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.*
import com.mackenzie.waifuviewer.domain.selector.SwitchState
import com.mackenzie.waifuviewer.ui.theme.Dimens
import com.mackenzie.waifuviewer.ui.theme.WaifuViewerTheme

@Composable
fun SelectorSwitches(
    modifier: Modifier = Modifier,
    switchStateCallback: (SwitchState) -> Unit = {},
    switchState: SwitchState = SwitchState(),
    server: ServerType = NORMAL,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {

        if (server == NORMAL || server == NEKOS) {
            Row(
                modifier = Modifier.padding(start = Dimens.homeSwitchesPadding),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.gif_content),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Switch(
                    checked = switchState.gifs,
                    onCheckedChange = { switchStateCallback(SwitchState(switchState.nsfw, switchState.gifs.not(), switchState.portrait)) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        uncheckedTrackColor = Color.Transparent
                    )
                )
            }
        }

        if (server == NORMAL || server == ENHANCED) {
            Row(
                modifier = Modifier.padding(start = Dimens.homeSwitchesPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = if(switchState.nsfw) R.string.nsfw_content else R.string.sfw_content),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Switch(
                    checked = switchState.nsfw,
                    onCheckedChange = { switchStateCallback(SwitchState(switchState.nsfw.not(), switchState.gifs, switchState.portrait)) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        uncheckedTrackColor = Color.Transparent
                    )
                )
            }
        }

        if (server == NORMAL) {
            Row(
                modifier = Modifier.padding(start = Dimens.homeSwitchesPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = if(switchState.portrait) R.string.landscape else R.string.portrait_default),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Switch(
                    checked = switchState.portrait,
                    onCheckedChange = { switchStateCallback(SwitchState(switchState.nsfw, switchState.gifs, switchState.portrait.not())) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        uncheckedTrackColor = Color.Transparent
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewSwitches() {
    WaifuViewerTheme(darkTheme = false) {
        SelectorSwitches()
    }
}