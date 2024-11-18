package com.mackenzie.waifuviewer.ui.selector.ui

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
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.*
import com.mackenzie.waifuviewer.ui.theme.Dimens

@Composable
fun SelectorMainButtons(
    tags: Triple<Pair<Array<String>, Array<String>>, Pair<Array<String>, Array<String>>, Pair<Array<String>, Array<String>>> = Triple(Pair(arrayOf(), arrayOf()), Pair(arrayOf(), arrayOf()), Pair(arrayOf(), arrayOf())),
    onServerButtonClicked: () -> Unit = {},
    onWaifuButtonClicked: (String) -> Unit = {},
    switchState: Triple<Boolean, Boolean, Boolean> = Triple(false, false, false),
    server: ServerType = NORMAL,
) {

    var expandido by remember { mutableStateOf(false) }

    var indiceSeleccionado by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Button(
                onClick = { onServerButtonClicked() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.transparent),
                    // contentColor = colorResource(id = R.color.purple_200)
                ),
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
                    // .align(Alignment.BottomCenter)
                    .padding(Dimens.homeButtonFabPadding),
                onClick = { expandido = true }
            ) {
                Text(
                    if (indiceSeleccionado == 0)
                        "Selecciona una categoría"
                    else {
                        when (server) {
                            NEKOS -> {
                                if (switchState.second) tags.third.second[indiceSeleccionado]
                                else tags.third.first[indiceSeleccionado]
                            }
                            ENHANCED -> {
                                if (switchState.first) tags.second.second[indiceSeleccionado]
                                else tags.second.first[indiceSeleccionado]
                            }
                            else -> {
                                if (switchState.first) tags.first.second[indiceSeleccionado]
                                else tags.first.first[indiceSeleccionado]
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
                        if (switchState.second) {
                            tags.third.second.forEachIndexed { indice, categoria ->
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
                            tags.third.first.forEachIndexed { indice, categoria ->
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
                        if (switchState.first) {
                            tags.second.second.forEachIndexed { indice, categoria ->
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
                            tags.second.first.forEachIndexed { indice, categoria ->
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
                        if (switchState.first) {
                            tags.first.second.forEachIndexed { indice, categoria ->
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
                            tags.first.first.forEachIndexed { indice, categoria ->
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
                            if(switchState.second) onWaifuButtonClicked( tags.third.second[indiceSeleccionado] )
                            else onWaifuButtonClicked( tags.third.first[indiceSeleccionado] )
                        }
                        ENHANCED -> {
                            if (switchState.first) onWaifuButtonClicked(tags.second.second[indiceSeleccionado])
                            else onWaifuButtonClicked(tags.second.first[indiceSeleccionado])
                        }
                        else -> {
                            if(switchState.first) onWaifuButtonClicked( tags.first.second[indiceSeleccionado] )
                            else onWaifuButtonClicked( tags.first.first[indiceSeleccionado] )
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