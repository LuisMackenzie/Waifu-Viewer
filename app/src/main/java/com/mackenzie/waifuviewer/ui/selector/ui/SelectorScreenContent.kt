package com.mackenzie.waifuviewer.ui.selector.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.ui.theme.Dimens

@Composable
fun SelectorScreenContent() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        AsyncImage(
            model= ImageRequest.Builder(LocalContext.current)
                .data("https://nekos.best/api/v2/neko/f09f1d72-4d7d-43ac-9aec-79f0544b95c3.png")
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
                .padding(Dimens.detailTitlePadding),
            text = "title",
            fontSize = Dimens.detailTitleFontSize,
            style = MaterialTheme.typography.bodyMedium
        )
        Button(
            onClick = {},
            modifier = Modifier.align(Alignment.BottomCenter),
            // colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFBB86FC))
        ) {
            Text(text = stringResource(id = R.string.give_me_waifus))
        }

        /*Text(
            text = stringResource(id = R.string.waifu_viewer),
            fontSize = 48.sp,
            color = Color.White,
            modifier = Modifier
                .constrainAs(titleText) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )*/

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

        /*Spinner(
            modifier = Modifier.constrainAs(spinner) {
                top.linkTo(chipGroup.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )*/

        /*Button(
            onClick = { *//* TODO *//* },
            modifier = Modifier.constrainAs(waifuButton) {
                bottom.linkTo(parent.bottom, margin = 50.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFBB86FC))
        ) {
            Text(text = stringResource(id = R.string.give_me_waifus))
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

        /*FloatingActionButton(
            onClick = { *//* TODO *//* },
            modifier = Modifier.constrainAs(waifuGpt) {
                bottom.linkTo(waifuGemini.top, margin = 10.dp)
                start.linkTo(parent.start, margin = 10.dp)
            },
            backgroundColor = Color(0xFFBB86FC)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_waifu_gpt), contentDescription = stringResource(id = R.string.download_image))
        }*/

        /*FloatingActionButton(
            onClick = { *//* TODO *//* },
            modifier = Modifier.constrainAs(waifuGemini) {
                bottom.linkTo(reloadBackground.top, margin = 70.dp)
                start.linkTo(parent.start, margin = 10.dp)
            },
            backgroundColor = Color(0xFFBB86FC)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_waifu_gemini), contentDescription = stringResource(id = R.string.download_image))
        }*/

        /*FloatingActionButton(
            onClick = { *//* TODO *//* },
            modifier = Modifier.constrainAs(reloadBackground) {
                bottom.linkTo(favorites.top, margin = 10.dp)
                start.linkTo(parent.start, margin = 10.dp)
            },
            backgroundColor = Color(0xFFBB86FC)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_baseline_update), contentDescription = stringResource(id = R.string.download_image))
        }*/

        /*FloatingActionButton(
            onClick = { *//* TODO *//* },
            modifier = Modifier.constrainAs(favorites) {
                bottom.linkTo(parent.bottom, margin = 10.dp)
                start.linkTo(parent.start, margin = 10.dp)
            },
            backgroundColor = Color(0xFFBB86FC)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_favorite_off), contentDescription = stringResource(id = R.string.download_image))
        }*/
    }
}