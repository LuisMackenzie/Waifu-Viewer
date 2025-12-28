package com.mackenzie.waifuviewer.ui.gpt.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import com.google.android.material.snackbar.Snackbar

@Composable
fun VideoHubScreenContent() {

    // var chatState by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { MainAppBar() }
    ) { padding ->

        MediaList(padding= padding)

        Snackbar.make(LocalView.current, "Under Development!", Snackbar.LENGTH_SHORT).show()
    }
}
