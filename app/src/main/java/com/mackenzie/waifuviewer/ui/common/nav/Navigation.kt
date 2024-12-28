package com.mackenzie.waifuviewer.ui.common.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mackenzie.waifuviewer.domain.selector.SwitchState
import com.mackenzie.waifuviewer.ui.detail.DetailScreenContentRoute
import com.mackenzie.waifuviewer.ui.favs.ui.FavoriteScreenContentRoute
import com.mackenzie.waifuviewer.ui.gemini.menu.WaifuGeminiScreenMenuRoute
import com.mackenzie.waifuviewer.ui.gpt.ui.WaifuGptScreenContent
import com.mackenzie.waifuviewer.ui.main.WaifuScreenContentRoute
import com.mackenzie.waifuviewer.ui.selector.ui.SelectorScreenContentRoute
import com.mackenzie.waifuviewer.ui.splash.SplashScreenRoute

@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavItem.SplashScreen.route
    ) {
        composable(NavItem.SplashScreen) {
            SplashScreenRoute {
                navController.navigate(route = NavItem.SelectorScreen.route) {
                    popUpTo(route = NavItem.SplashScreen.route) { inclusive = true }
                }
            }
        }
        composable(NavItem.SelectorScreen) {
            SelectorScreenContentRoute(
                onWaifuButtonClicked = { server, tag, nsfw, gif, lands ->
                    navController.navigate(route = NavItem.WaifuScreen.createRoute(server, tag, nsfw, gif, lands))
                },
                onGptButtonClicked = { gptType ->
                    if (gptType) navController.navigate(route = NavItem.WaifuGptScreen.route)
                    else navController.navigate(route = NavItem.WaifuGeminiScreen.route)
                },
                onFavoriteButtonClicked = { navController.navigate(route = NavItem.FavoriteScreen.route) }
            )
        }
        composable(NavItem.WaifuScreen) { backsStackEntry ->
            WaifuScreenContentRoute(
                server = backsStackEntry.findArg(NavArg.WaifuServer),
                tag = backsStackEntry.findArg(NavArg.WaifuTag),
                switchState = SwitchState(backsStackEntry.findArg(NavArg.NsfwState), backsStackEntry.findArg(NavArg.GifState), backsStackEntry.findArg(NavArg.LandsState))
            ) { waifu -> navController.navigate(route = NavItem.WaifuDetail.createRoute(waifu, false)) }
        }
        composable(NavItem.WaifuDetail) { backStackEntry ->
            DetailScreenContentRoute(backStackEntry.findArg(NavArg.WaifuId), backStackEntry.findArg(NavArg.WaifuFavorite))
        }
        composable(NavItem.FavoriteScreen) {
            FavoriteScreenContentRoute() { waifu -> navController.navigate(route = NavItem.WaifuDetail.createRoute(waifu, true)) }
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