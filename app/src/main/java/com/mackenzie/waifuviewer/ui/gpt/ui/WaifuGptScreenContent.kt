package com.mackenzie.waifuviewer.ui.gpt.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.android.material.snackbar.Snackbar
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.getMedia
import com.mackenzie.waifuviewer.domain.getMedia2
import com.mackenzie.waifuviewer.ui.common.ui.isNavigationBarVisible

@Composable
fun WaifuGptScreenContent() {

    var chatState by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { MainAppBar() }
    ) { padding ->

        val media1 = getMedia()
        val media2 = getMedia2()

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            // columns = GridCells.Adaptive(150.dp), // Esto mostrará 2-3 items por fila dependiendo del ancho de pantalla
            modifier = if (isNavigationBarVisible()) {
                Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding)
            } else {
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding)
            }
        ) {
            // Primera sección - Header
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Seccion numero 01",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Primera sección - Items
            items(media1) { item ->
                RenderItem(item, modifier = Modifier.padding(4.dp))
            }

            // Segunda sección - Header
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Seccion numero 02",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Segunda sección - Items
            items(media2) { item ->
                RenderItem(item, modifier = Modifier.padding(4.dp))
            }
        }

        Snackbar.make(LocalView.current, "Under Development!", Snackbar.LENGTH_SHORT).show()
    }
}
