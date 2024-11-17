package com.mackenzie.waifuviewer.ui.gpt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.mackenzie.waifuviewer.ui.gpt.ui.WaifuGptScreenContent
import com.mackenzie.waifuviewer.ui.main.ui.MainTheme


class WaifuGptFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MainTheme {
                    WaifuGptScreen()
                }
            }
        }
    }

    @Composable
    private fun WaifuGptScreen() {
        WaifuGptScreenContent()
        Snackbar.make(requireView(), "Under Development!", Snackbar.LENGTH_SHORT).show()
    }
}