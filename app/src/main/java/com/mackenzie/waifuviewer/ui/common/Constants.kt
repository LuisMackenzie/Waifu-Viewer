package com.mackenzie.waifuviewer.ui.common

class Constants {
    companion object {
        const val SERVER_MODE = "WaifuFragment:server_mode"
        const val IS_NSFW_WAIFU = "WaifuFragment:nsfw"
        const val IS_GIF_WAIFU = "WaifuFragment:gif"
        const val IS_LANDS_WAIFU = "WaifuFragment:lands"
        const val CATEGORY_TAG_WAIFU = "WaifuFragment:tag"
        const val CATEGORY_TAG_WAIFU_IM_ERROR = "WaifuFragment:Im Error"
        const val CATEGORY_TAG_WAIFU_PICS_ERROR = "WaifuFragment:Pics Error"
        const val CATEGORY_TAG_SELECTOR_IM_ERROR = "SelectorFragment:Im Error"
        const val CATEGORY_TAG_SELECTOR_PICS_ERROR = "SelectorFragment:Pics Error"
        const val CATEGORY_TAG_FAVORITE = "FavoriteFragment:Fav Error"
        const val CATEGORY_TAG_DETAIL = "DetailFragment:Detail Error"

        const val DEBUGINTERVALINSECONDS = 10L
        const val RELEASEINTERVALINSECONDS = 43200L

        val ENHANCEDSFW = arrayOf("all", "waifu", "neko", "shinobu", "megumin", "bully", "cuddle", "cry", "hug", "awoo", "kiss", "lick", "pat", "smug", "bonk", "yeet", "blush", "smile", "wave", "highfive", "handhold", "nom", "bite", "glomp", "slap", "kill", "kick", "happy", "wink", "poke", "dance", "cringe")
        val ENHANCEDNSFW = arrayOf("all", "waifu", "neko", "trap", "blowjob")
        val NORMALSFW = arrayOf("all", "uniform", "maid", "waifu", "marin-kitagawa", "mori-calliope", "raiden-shogun", "oppai", "selfies")
        val NORMALNSFW = arrayOf("all", "ass", "hentai", "milf", "oral", "paizuri", "ecchi", "ero")

    }
}