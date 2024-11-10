package com.mackenzie.waifuviewer.ui.gemini

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mackenzie.waifuviewer.ui.gemini.chat.WaifuChatRoute
import com.mackenzie.waifuviewer.ui.gemini.info.WaifuInfoRoute
import com.mackenzie.waifuviewer.ui.gemini.resume.SummarizeWaifuRoute
import com.mackenzie.waifuviewer.ui.main.ui.MainTheme

class WaifuGeminiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MainTheme {
                    WaifuGeminiScreen()
                }
            }
        }
    }

    @Composable
    private fun WaifuGeminiScreen() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "gemini_menu") {
                composable("gemini_menu") {
                    WaifuGeminiScreenMenuContent(onItemClicked = { routeId ->
                        navController.navigate(routeId)
                    })
                }
                composable("waifu_summarize") {
                    SummarizeWaifuRoute()
                }
                composable("waifu_info") {
                    WaifuInfoRoute()
                }
                composable("waifu_chat") {
                    WaifuChatRoute()
                }
            }
        }
    }
}