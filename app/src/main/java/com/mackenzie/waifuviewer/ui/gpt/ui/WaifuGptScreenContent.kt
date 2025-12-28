package com.mackenzie.waifuviewer.ui.gpt.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.android.material.snackbar.Snackbar
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.ui.common.ui.isNavigationBarVisible

@Composable
fun WaifuGptScreenContent() {

    var chatState by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { MainAppBar() }
    ) { padding ->

        Box(
            modifier = if (isNavigationBarVisible()) {
                Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .background(MaterialTheme.colorScheme.background)
            } else {
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            }
        ) {
            MediaList(modifier = Modifier.padding(padding))


            Snackbar.make(LocalView.current, "Under Development!", Snackbar.LENGTH_SHORT).show()
        }
    }
}