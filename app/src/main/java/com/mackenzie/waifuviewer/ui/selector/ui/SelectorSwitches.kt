package com.mackenzie.waifuviewer.ui.selector.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.*
import com.mackenzie.waifuviewer.ui.theme.Dimens

@Composable
fun SelectorSwitches(
    modifier: Modifier = Modifier,
    switchStateCallback: (Triple<Boolean, Boolean, Boolean>) -> Unit = {},
    server: ServerType = NORMAL,
) {

    var nsfwSwitch by remember { mutableStateOf(false) }
    var gifSwitch by remember { mutableStateOf(false) }
    var portraitSwitch by remember { mutableStateOf(false) }

    Row(modifier = modifier) {

        if (server == NORMAL || server == NEKOS) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Switch(
                    checked = gifSwitch,
                    onCheckedChange = { gifSwitch = !gifSwitch; switchStateCallback(Triple(nsfwSwitch, gifSwitch, portraitSwitch)) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                Text(
                    text = stringResource(id = R.string.gif_content),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        if (server == NORMAL || server == ENHANCED) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Switch(
                    checked = nsfwSwitch,
                    onCheckedChange = { nsfwSwitch = !nsfwSwitch; switchStateCallback(Triple(nsfwSwitch, gifSwitch, portraitSwitch)) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                Text(
                    text = stringResource(id = if(nsfwSwitch) R.string.nsfw_content else R.string.sfw_content),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        if (server == NORMAL) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Switch(
                    checked = portraitSwitch,
                    onCheckedChange = { portraitSwitch = !portraitSwitch; switchStateCallback(Triple(nsfwSwitch, gifSwitch, portraitSwitch)) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                Text(
                    text = stringResource(id = if(portraitSwitch) R.string.landscape else R.string.portrait_default),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}