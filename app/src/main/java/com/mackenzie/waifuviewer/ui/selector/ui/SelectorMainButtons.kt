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
    categorias: Array<String> = arrayOf(),
    onWaifuButtonClicked: (String) -> Unit = {}
) {

    var expandido by remember { mutableStateOf(false) }

    var indiceSeleccionado by remember { mutableStateOf(-1) }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Button(
                modifier = Modifier
                    // .align(Alignment.BottomCenter)
                    .padding(Dimens.homeButtonFabPadding),
                onClick = { expandido = true }
            ) {
                Text(
                    if (indiceSeleccionado == -1)
                        "Selecciona una categoría"
                    else
                        categorias[indiceSeleccionado]
                )
            }
            DropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                categorias.forEachIndexed { indice, categoria ->
                    DropdownMenuItem(
                        text = { Text(categoria) },
                        onClick = {
                            indiceSeleccionado = indice
                            expandido = false
                        }
                    )
                }
            }

            Button(
                onClick = { onWaifuButtonClicked( categorias[indiceSeleccionado] ) },
                modifier = Modifier
                    // .align(Alignment.BottomCenter)
                    .padding(bottom = Dimens.homeButtonPadding),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.purple_200),
                    // contentColor = colorResource(id = R.color.purple_200)
                ),
            ) {
                Text(
                    text = stringResource(id = R.string.give_me_waifus)
                )
            }
        }

    }
}