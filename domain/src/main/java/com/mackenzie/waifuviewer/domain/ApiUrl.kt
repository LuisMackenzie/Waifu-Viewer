package com.mackenzie.waifuviewer.domain

data class ApiUrl (
    val imBaseUrl: String = "https://api.waifu.im/",
    val picsBaseUrl: String = "https://api.waifu.pics/",
    val nekosBaseUrl: String = "https://nekos.best/api/v2/",
    val traceMoeBaseUrl: String = "https://api.trace.moe/"
        )