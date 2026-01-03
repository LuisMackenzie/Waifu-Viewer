package com.mackenzie.waifuviewer.ui.gpt.ui

import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import com.google.android.material.snackbar.Snackbar
import com.mackenzie.waifuviewer.domain.getNameById


@Composable
fun VideoListScreenContent(
    // vm: VideoHubViewModel = hiltViewModel(),
    serverId: Int,
    onNavigate: (Int) -> Unit = {}
) {

    // En base al server, PEdir la lista de videos


    Scaffold(
        topBar = { MainAppBar() }
    ) { padding ->

        VideoHubList(
            titleServer = getNameById(serverId),
            padding= padding,
            onItemClick = { item ->
                Log.e( "VideoHubScreenContent", "ID=${item.id}, Clicked item: ${item.title}")
                // onNavigate(item.id)
            }
        )

        Snackbar.make(LocalView.current, "Under Development!", Snackbar.LENGTH_SHORT).show()
    }
}