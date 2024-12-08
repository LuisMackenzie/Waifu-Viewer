package com.mackenzie.waifuviewer.domain.selector

data class ServerTags(
    val enhancedSfw: List<String> = listOf("All Categories", "waifu", "neko", "shinobu", "megumin", "bully", "cuddle", "cry", "hug", "awoo", "kiss", "lick", "pat", "smug", "bonk", "yeet", "blush", "smile", "wave", "highfive", "handhold", "nom", "bite", "glomp", "slap", "kill", "kick", "happy", "wink", "poke", "dance", "cringe"),
    val enhancedNsfw: List<String>  = listOf("All Categories", "waifu", "neko", "trap", "blowjob"),
    val normalSfw: List<String>  = listOf("All Categories", "uniform", "maid", "waifu", "marin-kitagawa", "mori-calliope", "raiden-shogun", "kamisato-ayaka", "oppai", "selfies"),
    val normalNsfw: List<String>  = listOf("All Categories", "ass", "hentai", "milf", "oral", "paizuri", "ecchi", "ero"),
    val nekosGif: List<String> = listOf("All Categories", "highfive", "happy", "sleep", "handhold", "laugh", "bite", "poke", "tickle", "kiss", "wave", "thumbsup", "stare", "cuddle", "smile", "baka", "blush", "think", "pout", "facepalm", "wink", "shoot", "smug", "cry", "pat", "punch", "dance", "feed", "shrug", "bored", "kick", "hug", "yeet", "slap"),
    val nekosPng: List<String> = listOf("All Categories", "neko", "husbando", "kitsune", "waifu"),
    val nekosAll :List<String> = listOf("All Categories", "neko", "husbando", "kitsune", "waifu", "highfive", "happy", "sleep", "handhold", "laugh", "bite", "poke", "tickle", "kiss", "wave", "thumbsup", "stare", "cuddle", "smile", "baka", "blush", "think", "pout", "facepalm", "wink", "shoot", "smug", "cry", "pat", "punch", "dance", "feed", "shrug", "bored", "kick", "hug", "yeet", "slap" )
)