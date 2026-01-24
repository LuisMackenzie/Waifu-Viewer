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

                ServerSpec(50, "Hanime.tv", listOf("hanime.tv")),
                ServerSpec(51, "HentaiCloud", listOf("hentaicloud.com")),
                ServerSpec(52, "HentaiGasm", listOf("hentaigasm.com")),
                ServerSpec(53, "HentaiMama 1", listOf("hentaimama.io")),
                ServerSpec(54, "HentaiMama 2", listOf("hentaimama.tv")),
                ServerSpec(55, "HentaiMama 3", listOf("hentaimama.xxx")),
                ServerSpec(56, "HentaiTube 1", listOf("hentaitube.online")),
                ServerSpec(57, "HentaiTube 2", listOf("hentaitube.icu")),
                ServerSpec(58, "HentaiPlay", listOf("hentaiplay.net")),
                ServerSpec(59, "MuchoHentai", listOf("muchohentai.com")),
                ServerSpec(60, "Naughty Machinima", listOf("naughtymachinima.com")),
                ServerSpec(61, "OHentai", listOf("ohentai.org")),
                ServerSpec(62, "PorCore", listOf("porcore.com")),
                ServerSpec(63, "xAnimePorn", listOf("xanimeporn.com")),
                ServerSpec(64, "AniPorn", listOf("aniporn.com")),
                ServerSpec(65, "ZZCartoon", listOf("zzcartoon.com")),
                ServerSpec(66, "PornHub Hentai", listOf("pornhub.com/categories/hentai")),
                ServerSpec(67, "HentaiHeaven", listOf("hentaihaven.co")),

                /*ServerSpec(80, "Chaturbate", listOf("")),
                ServerSpec(81, "Amateur.tv", listOf("")),
                ServerSpec(82, "BongaCams", listOf("")),
                ServerSpec(83, "Cam4", listOf("")),
                ServerSpec(84, "Camsoda", listOf("")),
                ServerSpec(85, "CamWhoresBay", listOf("")),
                ServerSpec(86, "StripChat", listOf("")),
                ServerSpec(87, "Streamate", listOf(""))*/
            )
        }
    }
}
