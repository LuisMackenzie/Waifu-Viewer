package com.mackenzie.waifuviewer.ui.common.nav

import androidx.navigation.NavType
import androidx.navigation.navArgument
import java.io.File

sealed class NavItem(
    internal val baseRoute: String,
    private val navArgs: List<NavArg> = emptyList()
) {

    object SplashScreen : NavItem("splash_screen")

    object SelectorScreen : NavItem("selector_screen")

    object WaifuScreen : NavItem("waifu_screen", listOf(NavArg.WaifuTag)) {
        fun createRoute(waifuTag: String) = "$baseRoute/$waifuTag"
    }

    object FavoriteScreen : NavItem("favorite_screen")

    object WaifuDetail : NavItem("detail_screen", listOf(NavArg.ItemId)) {
        fun createRoute(itemId: Int) = "$baseRoute/$itemId"
    }

    object WaifuGptScreen : NavItem("gpt_screen")

    object WaifuGeminiScreen : NavItem("gemini_screen")

    val route = run {
        val argValues = navArgs.map { "{${it.key}}" }
        listOf(baseRoute)
            .plus(argValues)
            .joinToString("/")
    }

    val args = navArgs.map {
        navArgument(it.key) { type = it.navType }
    }
}

enum class NavArg(val key: String, val navType: NavType<*>) {
    ItemId("itemId", NavType.IntType),
    WaifuTag("waifuTag", NavType.StringType)
}