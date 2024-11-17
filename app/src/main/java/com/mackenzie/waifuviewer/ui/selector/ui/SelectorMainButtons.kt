package com.mackenzie.waifuviewer.ui.selector.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.ui.theme.Dimens

@Composable
fun SelectorMainButtons(
    categorias: Pair< Array<String>, Array<String>> = Pair(arrayOf(), arrayOf()),
    // onServerButtonClicked: (String) -> Unit = {},
    onWaifuButtonClicked: (String) -> Unit = {},
    switchState: Triple<Boolean, Boolean, Boolean> = Triple(false, false, false),
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
                onClick = {  },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.transparent),
                    // contentColor = colorResource(id = R.color.purple_200)
                ),
            ) {
                Text(
                    text = stringResource(id = R.string.server_normal),
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
                        if(switchState.first || switchState.second) categorias.second[indiceSeleccionado]
                        else categorias.first[indiceSeleccionado]
                    }
                )
            }
            DropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                if (switchState.first || switchState.second) {
                    categorias.second.forEachIndexed { indice, categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria) },
                            onClick = {
                                indiceSeleccionado = indice
                                expandido = false
                            }
                        )
                    }
                } else {
                    categorias.first.forEachIndexed { indice, categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria) },
                            onClick = {
                                indiceSeleccionado = indice
                                expandido = false
                            }
                        )
                    }
                }

            }

            Button(
                onClick = {
                    if(switchState.first || switchState.second) onWaifuButtonClicked( categorias.second[indiceSeleccionado] )
                    else onWaifuButtonClicked( categorias.first[indiceSeleccionado] )
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