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

    object WaifuScreen : NavItem("waifu_screen", listOf(NavArg.WaifuServer, NavArg.WaifuTag, NavArg.NsfwState, NavArg.GifState, NavArg.LandsState)) {
        fun createRoute(server: String, waifuTag: String, nsfw: Boolean, gif: Boolean, lands: Boolean) = baseRoute + File.separator + server + File.separator + waifuTag + File.separator + nsfw + File.separator + gif + File.separator + lands
    }

    object FavoriteScreen : NavItem("favorite_screen")

    object WaifuDetail : NavItem("detail_screen", listOf(NavArg.WaifuId, NavArg.WaifuFavorite)) {
        fun createRoute(itemId: Int, isFavorite: Boolean) = baseRoute + File.separator + itemId + File.separator + isFavorite
    }

    object WaifuGptScreen : NavItem("gpt_screen")

    object WaifuGeminiScreen : NavItem("gemini_screen")

    val route = run {
        val argValues = navArgs.map { "{${it.key}}" }
        listOf(baseRoute)
            .plus(argValues)
            .joinToString(File.separator)
    }

    val args = navArgs.map {
        navArgument(it.key) { type = it.navType }
    }
}

enum class NavArg(val key: String, val navType: NavType<*>) {
    WaifuId("itemId", NavType.IntType),
    WaifuServer("waifuServer", NavType.StringType),
    WaifuTag("waifuTag", NavType.StringType),
    WaifuFavorite("waifuFavorite", NavType.BoolType),
    NsfwState("nsfwState", NavType.BoolType),
    GifState("gifState", NavType.BoolType),
    LandsState("landsState", NavType.BoolType),
}