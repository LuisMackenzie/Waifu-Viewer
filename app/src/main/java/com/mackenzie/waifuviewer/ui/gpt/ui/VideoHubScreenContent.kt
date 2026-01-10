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

@Composable
fun VideoHubScreenContent(
    vm: VideoHubViewModel = hiltViewModel(),
    onNavigate: (Int) -> Unit = {}
) {

    val videoHubState by vm.state.collectAsStateWithLifecycle()
    LaunchedEffect(true) {
        // vm.getServers()
    }

    Scaffold(
        topBar = { MainAppBar() }
    ) { padding ->

        val localview = LocalView.current

        ServerList(
            padding= padding,
            onFavoriteClick = {
                Snackbar.make(localview, "Favorite Feature Under Development!", Snackbar.LENGTH_SHORT).show()
            }
        ) { item ->
            Log.e( "VideoHubScreenContent", "Server ID=${item.id}, Clicked Server: ${item.title}")
            onNavigate(item.id)
        }

        Snackbar.make(LocalView.current, "Under Development!", Snackbar.LENGTH_SHORT).show()
    }
}
