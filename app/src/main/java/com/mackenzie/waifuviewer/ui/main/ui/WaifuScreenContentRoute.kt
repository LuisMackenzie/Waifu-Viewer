package com.mackenzie.waifuviewer.ui.main.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.WaifuPicsViewModel
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.ENHANCED
import com.mackenzie.waifuviewer.domain.ServerType.NEKOS
import com.mackenzie.waifuviewer.domain.ServerType.NORMAL
import com.mackenzie.waifuviewer.ui.main.WaifuBestViewModel
import com.mackenzie.waifuviewer.ui.main.WaifuImViewModel

@Composable
fun WaifuScreenContentRoute(
    imViewModel: WaifuImViewModel = hiltViewModel(),
    picsViewModel : WaifuPicsViewModel = hiltViewModel(),
    bestViewModel: WaifuBestViewModel = hiltViewModel(),
    onNavigate: () -> Unit = {}
) {





    WaifuImScreenContent(
        state = imViewModel.state.collectAsStateWithLifecycle().value,
        onWaifuClicked = {}, // { mainState.onWaifuImClicked(it) },
        onRequestMore = {}, // { onLoadMoreWaifusIm() },
        onFabClick = {
            imViewModel.onClearImDatabase()
            // activity?.onBackPressedDispatcher?.onBackPressed()
            // Toast.makeText(requireContext(), getString(R.string.waifus_gone), Toast.LENGTH_SHORT).show()
        }
    )

}

@Composable
private fun LaunchWaifuScreen(mode: ServerType) {
    when (mode) {
        NORMAL -> {} // WaifuImScreen()
        ENHANCED -> {} //  WaifuPicsScreen()
        NEKOS -> {} // WaifuNekosScreen()
        else -> {}
    }
}