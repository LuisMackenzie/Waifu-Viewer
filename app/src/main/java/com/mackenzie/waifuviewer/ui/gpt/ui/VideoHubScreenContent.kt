package com.mackenzie.waifuviewer.ui.gpt.ui

import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.material.snackbar.Snackbar
import com.mackenzie.waifuviewer.ui.gpt.VideoHubViewModel
import com.mackenzie.waifuviewer.ui.selector.SelectorViewModel

@Composable
fun VideoHubScreenContent(vm: VideoHubViewModel = hiltViewModel()) {

    val videoHubState by vm.state.collectAsStateWithLifecycle()
    LaunchedEffect(true) {
        // vm.getServers()
    }

    Scaffold(
        topBar = { MainAppBar() }
    ) { padding ->

        MediaList(padding= padding)

        Snackbar.make(LocalView.current, "Under Development!", Snackbar.LENGTH_SHORT).show()
    }
}
