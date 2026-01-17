package com.mackenzie.waifuviewer.ui.gpt.ui

import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import com.google.android.material.snackbar.Snackbar

@Composable
fun VideoHubScreenContent(
    onNavigate: (Int, String) -> Unit = { _, _ -> }
) {

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
            onNavigate(item.id, item.url)
        }

        Snackbar.make(LocalView.current, "Under Development!", Snackbar.LENGTH_SHORT).show()
    }
}
