package com.mackenzie.waifuviewer.domain.video.embed

data class ServerSpec(
    val id: Int,
    val name: String,
    val domains: List<String>,
) {

    companion object {
        fun getSupportedServers(): List<ServerSpec> {
            return listOf(
                ServerSpec(1, "PornHub", listOf("pornhub.com", "pornhub.es")),
                ServerSpec(2, "RedTube", listOf("redtube.com")),
                ServerSpec(3, "Beeg", listOf("beeg.com")),
                ServerSpec(4, "Eporner", listOf("eporner.com")),
                ServerSpec(5, "Tube8", listOf("tube8.com")),
                ServerSpec(6, "XHamster", listOf("xhamster.com")),
                ServerSpec(7, "YouJizz", listOf("youjizz.com")),
                ServerSpec(8, "YouPorn", listOf("youporn.com")),
                ServerSpec(9, "XVideos", listOf("xvideos.com", "xvideos.es")),
                ServerSpec(10, "XNXX", listOf("xnxx.com", "xnxx.es")),
                ServerSpec(11, "TNAFLix", listOf("tnaflix.com")),
                ServerSpec(12, "Blowjobs", listOf("blowjobs.pro")),
                ServerSpec(13, "SpangBang", listOf("spankbang.com")),
                ServerSpec(14, "PornTrex", listOf("porntrex.com")),
                ServerSpec(15, "HQPorner", listOf("hqporner.com")),
                ServerSpec(16, "Analdin", listOf("analdin.com")),
                ServerSpec(17, "XXXFiles", listOf("xxxfiles.com")),
                ServerSpec(18, "PornSlash", listOf("pornslash.com")),
                ServerSpec(19, "WatchPorn", listOf("watchporn.to")),
                ServerSpec(20, "WhoresHub", listOf("whoreshub.com")),
                ServerSpec(21, "Porn.com", listOf("porn.com")),
                ServerSpec(22, "Porn HD", listOf("pornhd.com")),

                ServerSpec(50, "", listOf("hanime.tv")),
                ServerSpec(51, "", listOf("hentaicloud.com")),
                ServerSpec(52, "", listOf("hentaigasm.com")),
                ServerSpec(53, "", listOf("hentaimama.io")),
                /*ServerSpec(54, "", listOf("")),
                ServerSpec(55, "", listOf("")),
                ServerSpec(56, "", listOf("")),
                ServerSpec(57, "", listOf("")),
                ServerSpec(58, "", listOf("")),
                ServerSpec(59, "", listOf("")),
                ServerSpec(60, "", listOf("")),
                ServerSpec(61, "", listOf("")),*/
                ServerSpec(62, "PorCore", listOf("porcore.com")),
                /*ServerSpec(63, "", listOf("")),
                ServerSpec(64, "", listOf("")),
                ServerSpec(65, "", listOf("")),
                ServerSpec(66, "", listOf("")),
                ServerSpec(67, "", listOf("")),

                ServerSpec(80, "", listOf("")),
                ServerSpec(81, "", listOf("")),
                ServerSpec(82, "", listOf("")),
                ServerSpec(83, "", listOf("")),
                ServerSpec(84, "", listOf("")),
                ServerSpec(85, "", listOf("")),
                ServerSpec(86, "", listOf("")),
                ServerSpec(87, "", listOf(""))*/
            )
        }
    }
}
