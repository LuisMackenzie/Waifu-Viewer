package com.mackenzie.waifuviewer.ui.common.nav

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.os.bundleOf
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.material.snackbar.Snackbar
import com.mackenzie.waifuviewer.domain.selector.SwitchState
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.favs.ui.FavoriteScreenContentRoute
import com.mackenzie.waifuviewer.ui.gemini.menu.WaifuGeminiScreenMenuRoute
import com.mackenzie.waifuviewer.ui.gpt.ui.WaifuGptScreenContent
import com.mackenzie.waifuviewer.ui.main.ui.WaifuScreenContentRoute
import com.mackenzie.waifuviewer.ui.selector.ui.SelectorScreenContentRoute
import com.mackenzie.waifuviewer.ui.splash.SplashScreenRoute

@Composable
fun Navigation() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val view = LocalView.current
    var bun by remember { mutableStateOf(bundleOf()) }

    NavHost(
        navController = navController,
        startDestination = NavItem.SplashScreen.route
    ) {
        composable(NavItem.SplashScreen) {
            SplashScreenRoute {
                navController.navigate(route = NavItem.SelectorScreen.route) {
                    popUpTo(route =
                    NavItem.SplashScreen.route) {
                        inclusive = true
                    }
                }
            }
        }
        composable(NavItem.SelectorScreen) {
            // TODO
            SelectorScreenContentRoute(
                onWaifuButtonClicked = { server, tag, nsfw, gif, lands ->
                    navController.navigate(route = NavItem.WaifuScreen.createRoute(server, tag, nsfw, gif, lands))
                },
                onGptButtonClicked = { gptType ->
                    if (gptType) {
                        navController.navigate(route = NavItem.WaifuGptScreen.route)
                    } else {
                        navController.navigate(route = NavItem.WaifuGeminiScreen.route)
                    }
                },
                onFavoriteButtonClicked = {
                    navController.navigate(route = NavItem.FavoriteScreen.route)
                }
            )
        }
        composable(NavItem.WaifuScreen) { backsStackEntry ->
            WaifuScreenContentRoute(
                server = backsStackEntry.findArg(NavArg.WaifuServer),
                tag = backsStackEntry.findArg(NavArg.WaifuTag),
                switchState = SwitchState(
                    backsStackEntry.findArg(NavArg.NsfwState),
                    backsStackEntry.findArg(NavArg.GifState),
                    backsStackEntry.findArg(NavArg.LandsState))
            ) {
                /*navController.navigate(route = NavItem.WaifuDetail.createRoute()) {
                    popUpTo(route =
                    NavItem.WaifuScreen.route) {
                        inclusive = true
                    }
                }*/
            }
            // Esto es lo que se le passa a la pantalla para que pueda navegar
            // navController.navigate(NavItem.WaifuDetail.createRoute(waifu.id))
        }
        composable(NavItem.WaifuDetail) { backStackEntry ->
            val waifuId = backStackEntry.arguments?.getInt(NavArg.ItemId.key)
            // requireNotNull(waifuId)
            // DetailScreenRoute(waifuId = waifuId)
        }
        composable(NavItem.FavoriteScreen) {
            FavoriteScreenContentRoute()
        }
        composable(NavItem.WaifuGptScreen) {
            WaifuGptScreenContent()
        }
        composable(NavItem.WaifuGeminiScreen) {
            WaifuGeminiScreenMenuRoute()
        }
    }

}

private fun NavGraphBuilder.composable(
    navItem: NavItem,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(route = navItem.route, arguments = navItem.args) { backStackEntry ->
        content(backStackEntry)
    }

}

private inline fun <reified T> NavBackStackEntry.findArg(arg: NavArg): T {
    val value = arguments?.get(arg.key)
    requireNotNull(value) { "Argument ${arg.key} not found" }
    return value as T
}

private inline fun <reified T> NavBackStackEntry.findArgByKey(key: String): T {
    val value = arguments?.get(key)
    requireNotNull(value) { "Argument ${key} not found" }
    return value as T
}