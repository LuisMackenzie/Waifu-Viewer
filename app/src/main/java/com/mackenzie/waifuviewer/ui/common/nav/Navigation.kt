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
import androidx.core.os.bundleOf
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.main.ui.WaifuScreenContentRoute
import com.mackenzie.waifuviewer.ui.selector.ui.SelectorScreenContentRoute
import com.mackenzie.waifuviewer.ui.splash.SplashScreenRoute

@Composable
fun Navigation() {

    val navController = rememberNavController()
    val context = LocalContext.current
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
                onWaifuButtonClicked = { waifuTag, bundle ->
                    bun = bundle
                    navController.navigate(route = NavItem.WaifuScreen.createRoute(waifuTag))
                    Log.e("WaifuTag", "waifuTag= $waifuTag")
                    Log.e("WaifuTag", "bundle Sended= $bundle")
                }
            )
        }
        composable(NavItem.WaifuScreen) { backsStackEntry ->
            val tag: String = backsStackEntry.findArg(NavArg.WaifuTag)
            // val bun: String = backsStackEntry.findArg(NavArg.WaifuBundle)
            Log.e("WaifuTag", "waifuTag= $tag")
            Log.e("WaifuTag", "bundle received= $bun")
            WaifuScreenContentRoute(bundle = bun) {
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
            // FavoriteScreenRoute()
        }
        composable(NavItem.WaifuGptScreen) {
            // WaifuGptScreenContent()
        }
        composable(NavItem.WaifuGeminiScreen) {
            // WaifuGptScreenContent()
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