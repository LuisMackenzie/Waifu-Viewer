package com.mackenzie.waifuviewer.ui.selector.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.*
import com.mackenzie.waifuviewer.domain.selector.SwitchState
import com.mackenzie.waifuviewer.domain.selector.TagsState
import com.mackenzie.waifuviewer.ui.theme.Dimens
import com.mackenzie.waifuviewer.ui.theme.WaifuViewerTheme

@Composable
fun SelectorMainButtons(
    tags: TagsState = TagsState(),
    onServerButtonClicked: () -> Unit = {},
    onWaifuButtonClicked: (String) -> Unit = {},
    switchState: SwitchState = SwitchState(),
    server: ServerType = NORMAL,
) {

    var expandido by remember { mutableStateOf(false) }

    var indiceSeleccionado by remember { mutableStateOf(0) }

    var switchDefaultState by remember { mutableStateOf(SwitchState()) }

    when (server) {
        NEKOS -> { if (switchState.gifs != switchDefaultState.gifs) { indiceSeleccionado = 0 } }
        ENHANCED -> { if (switchState.nsfw != switchDefaultState.nsfw) { indiceSeleccionado = 0 } }
        else -> { if (switchState.nsfw != switchDefaultState.nsfw) { indiceSeleccionado = 0 } }
    }
    switchDefaultState = switchState

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Button(
                onClick = { onServerButtonClicked(); indiceSeleccionado = 0 },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.transparent)),
            ) {
                Text(
                    text = stringResource(
                        id = when (server) {
                            NEKOS -> R.string.server_best
                            ENHANCED -> R.string.server_enhanced
                            else -> R.string.server_normal
                        }
                    ),
                    color = colorResource(id = R.color.white),
                    fontSize = Dimens.homeButtonServerFontSize
                )
            }

            Button(
                modifier = Modifier
                    .padding(Dimens.homeButtonFabPadding),
                onClick = { expandido = true },
                colors =  ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Text(
                    if (indiceSeleccionado == 0)
                        "Selecciona una categoría"
                    else {
                        when (server) {
                            NEKOS -> {
                                if (switchState.gifs) tags.nekos.gifs[indiceSeleccionado]
                                else tags.nekos.png[indiceSeleccionado]
                            }
                            ENHANCED -> {
                                if (switchState.nsfw) tags.enhanced.nsfw[indiceSeleccionado]
                                else tags.enhanced.sfw[indiceSeleccionado]
                            }
                            else -> {
                                if (switchState.nsfw) tags.normal.nsfw[indiceSeleccionado]
                                else tags.normal.sfw[indiceSeleccionado]
                            }
                        }
                    }
                )
            }
            DropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                when (server) {
                    NEKOS -> {
                        if (switchState.gifs) {
                            tags.nekos.gifs.forEachIndexed { indice, categoria ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = categoria,
                                            fontSize = Dimens.homeTagsItemFontSize,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center,
                                        )
                                    },
                                    onClick = {
                                        indiceSeleccionado = indice
                                        expandido = false
                                    },
                                    contentPadding = PaddingValues(Dimens.homeTagsItemPaddings)
                                )
                            }
                        } else {
                            tags.nekos.png.forEachIndexed { indice, categoria ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = categoria,
                                            fontSize = Dimens.homeTagsItemFontSize,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center
                                        )
                                    },
                                    onClick = {
                                        indiceSeleccionado = indice
                                        expandido = false
                                    },
                                    contentPadding = PaddingValues(Dimens.homeTagsItemPaddings)
                                )
                            }
                        }
                    }
                    ENHANCED -> {
                        if (switchState.nsfw) {
                            tags.enhanced.nsfw.forEachIndexed { indice, categoria ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = categoria,
                                            fontSize = Dimens.homeTagsItemFontSize,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center
                                        )
                                    },
                                    onClick = {
                                        indiceSeleccionado = indice
                                        expandido = false
                                    },
                                    contentPadding = PaddingValues(Dimens.homeTagsItemPaddings)
                                )
                            }
                        } else {
                            tags.enhanced.sfw.forEachIndexed { indice, categoria ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = categoria,
                                            fontSize = Dimens.homeTagsItemFontSize,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center
                                        )
                                    },
                                    onClick = {
                                        indiceSeleccionado = indice
                                        expandido = false
                                    },
                                    contentPadding = PaddingValues(Dimens.homeTagsItemPaddings)
                                )
                            }
                        }
                    }
                    else -> {
                        if (switchState.nsfw) {
                            tags.normal.nsfw.forEachIndexed { indice, categoria ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = categoria,
                                            fontSize = Dimens.homeTagsItemFontSize,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center
                                        )
                                    },
                                    onClick = {
                                        indiceSeleccionado = indice
                                        expandido = false
                                    },
                                    // modifier = Modifier.align(Alignment.CenterHorizontally),
                                    contentPadding = PaddingValues(Dimens.homeTagsItemPaddings)
                                )
                            }
                        } else {
                            tags.normal.sfw.forEachIndexed { indice, categoria ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = categoria,
                                            fontSize = Dimens.homeTagsItemFontSize,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center
                                        )
                                    },
                                    onClick = {
                                        indiceSeleccionado = indice
                                        expandido = false
                                    },
                                    // modifier = Modifier.align(Alignment.CenterHorizontally),
                                    contentPadding = PaddingValues(Dimens.homeTagsItemPaddings)
                                )
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    when (server) {
                        NEKOS -> {
                            if(switchState.gifs) onWaifuButtonClicked( tags.nekos.gifs[indiceSeleccionado] )
                            else onWaifuButtonClicked( tags.nekos.png[indiceSeleccionado] )
                        }
                        ENHANCED -> {
                            if (switchState.nsfw) onWaifuButtonClicked(tags.enhanced.nsfw[indiceSeleccionado])
                            else onWaifuButtonClicked(tags.enhanced.sfw[indiceSeleccionado])
                        }
                        else -> {
                            if(switchState.nsfw) onWaifuButtonClicked( tags.normal.nsfw[indiceSeleccionado] )
                            else onWaifuButtonClicked( tags.normal.sfw[indiceSeleccionado] )
                        }
                    }
                },
                modifier = Modifier
                    // .align(Alignment.BottomCenter)
                    .padding(bottom = Dimens.homeButtonPadding),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_200)),
            ) {
                Text(
                    text = stringResource(id = R.string.give_me_waifus)
                )
            }
        }

    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewButtons() {
    WaifuViewerTheme(darkTheme = false) {
        SelectorMainButtons()
    }
}