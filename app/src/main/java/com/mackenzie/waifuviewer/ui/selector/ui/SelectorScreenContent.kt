package com.mackenzie.waifuviewer.ui.selector.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.android.material.color.MaterialColors
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.common.isLandscape
import com.mackenzie.waifuviewer.ui.theme.Dimens
import com.mackenzie.waifuviewer.ui.theme.WaifuViewerTheme

@Composable
fun SelectorScreenContent(
    onWaifuButtonClicked: (String) -> Unit = {}
) {

    var nsfwSwitch by remember { mutableStateOf(false) }
    var gifSwitch by remember { mutableStateOf(false) }
    var portraitSwitch by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        val background = if (LocalContext.current.isLandscape()) "https://cdn.waifu.im/808.png" else "https://nekos.best/api/v2/neko/f09f1d72-4d7d-43ac-9aec-79f0544b95c3.png"
        AsyncImage(
            model= ImageRequest.Builder(LocalContext.current)
                .data(background)
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.ic_error_grey),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(Dimens.homeTitlePadding),
            text = stringResource(id = R.string.waifu_viewer),
            color = Color.White,
            fontSize = Dimens.homeTitleFontSize,
            style = MaterialTheme.typography.bodyMedium
        )
        SelectorMainButtons(
            categorias = Constants.NORMALSFW,
            onWaifuButtonClicked = onWaifuButtonClicked
        )

        SelectorFabFavorites()

        Row(modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(all = Dimens.homeSwitchesPadding)
            .padding(end = Dimens.homeSwitchesPaddingEnd)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Switch(
                    checked = gifSwitch,
                    onCheckedChange = { gifSwitch = !gifSwitch },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                Text(
                    text = stringResource(id = R.string.gif_content),
                    // color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Switch(
                    checked = nsfwSwitch,
                    onCheckedChange = { nsfwSwitch = !nsfwSwitch },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                Text(
                    text = stringResource(id = if(nsfwSwitch) R.string.nsfw_content else R.string.sfw_content),
                    // color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Switch(
                    checked = portraitSwitch,
                    onCheckedChange = { portraitSwitch = !portraitSwitch },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                Text(
                    text = stringResource(id = if(portraitSwitch) R.string.landscape else R.string.portrait_default),
                    // color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }









        /*ChipGroup(
            modifier = Modifier.constrainAs(chipGroup) {
                top.linkTo(titleText.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            Chip(
                onClick = { *//* TODO *//* },
                colors = ChipDefaults.chipColors(backgroundColor = Color.Gray)
            ) {
                Text(text = stringResource(id = R.string.server_normal))
            }
            Chip(
                onClick = { *//* TODO *//* },
                colors = ChipDefaults.chipColors(backgroundColor = Color.Gray)
            ) {
                Text(text = stringResource(id = R.string.server_enhanced))
            }
            Chip(
                onClick = { *//* TODO *//* },
                colors = ChipDefaults.chipColors(backgroundColor = Color.Gray)
            ) {
                Text(text = stringResource(id = R.string.server_best))
            }
        }*/

        /*Row(
            modifier = Modifier
                .constrainAs(switchGroup) {
                    bottom.linkTo(parent.bottom, margin = 4.dp)
                    start.linkTo(favorites.end, margin = 12.dp)
                    end.linkTo(parent.end, margin = 12.dp)
                }
        ) {
            Switch(
                checked = false,
                onCheckedChange = { *//* TODO *//* },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White)
            )
            Text(text = stringResource(id = R.string.gif_content), color = Color.White)

            Switch(
                checked = false,
                onCheckedChange = { *//* TODO *//* },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White)
            )
            Text(text = stringResource(id = R.string.sfw_content), color = Color.White)

            Switch(
                checked = false,
                onCheckedChange = { *//* TODO *//* },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White)
            )
            Text(text = stringResource(id = R.string.portrait_default), color = Color.White)
        }*/
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSelector() {
    WaifuViewerTheme(darkTheme = false) {
        SelectorScreenContent()
    }
}