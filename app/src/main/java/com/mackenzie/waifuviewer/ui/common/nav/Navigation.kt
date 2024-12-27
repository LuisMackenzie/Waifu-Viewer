package com.mackenzie.waifuviewer.ui.common.nav

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mackenzie.waifuviewer.ui.common.tagFilter
import com.mackenzie.waifuviewer.ui.main.ui.WaifuScreenContentRoute
import com.mackenzie.waifuviewer.ui.selector.ui.SelectorScreenContentRoute
import com.mackenzie.waifuviewer.ui.splash.SplashScreenRoute

@Composable
fun Navigation() {

    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = NavItem.SplashScreen.route
    ) {
        composable(NavItem.SplashScreen.route) {
            SplashScreenRoute {
                navController.navigate(route = NavItem.SelectorScreen.route) {
                    popUpTo(route =
                    NavItem.SplashScreen.route) {
                        inclusive = true
                    }
                }
            }
        }
        composable(NavItem.SelectorScreen.route) {
            // TODO
            SelectorScreenContentRoute(
                onWaifuButtonClicked = { waifuTag ->
                    Log.e("Navigation", "waifuTag=$waifuTag")
                    navController.navigate(route = NavItem.WaifuScreen.createRoute(waifuTag)) {
                        popUpTo(route =
                        NavItem.SelectorScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            route = NavItem.WaifuScreen.route,
            arguments = NavItem.WaifuScreen.args
            // route = "waifu_screen/{waifuTag}",
            // arguments = listOf(navArgument("waifuTag") { type = NavType.StringType })
        ) { backsStackEntry ->
            val tag = backsStackEntry.arguments?.getString(NavArg.WaifuTag.key)
            /*WaifuScreenContentRoute() {
                navController.navigate(route = NavItem.WaifuDetail.createRoute()) {
                    popUpTo(route =
                    NavItem.WaifuScreen.route) {
                        inclusive = true
                    }
                }
            }*/

            Text("WaifuScreen $tag")

            // Esto es lo que se le passa a la pantalla para que pueda navegar
            // navController.navigate(NavItem.WaifuDetail.createRoute(waifu.id))
        }
        composable(
            route = NavItem.WaifuDetail.route,
            arguments = NavItem.WaifuDetail.args
            // route = NavItem.WaifuDetail.baseRoute,
            // arguments = listOf(navArgument("waifuId") { type = NavType.IntType })
        ) { backStackEntry ->
            val waifuId = backStackEntry.arguments?.getInt(NavArg.ItemId.key)
            // requireNotNull(waifuId)
            // DetailScreenRoute(waifuId = waifuId)
        }
        composable(NavItem.FavoriteScreen.baseRoute) {
            // FavoriteScreenRoute()
        }
        composable(NavItem.WaifuGptScreen.baseRoute) {
            // WaifuGptScreenContent()
        }
        composable(NavItem.WaifuGeminiScreen.baseRoute) {
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