package com.mackenzie.waifuviewer.domain

import com.mackenzie.waifuviewer.domain.VideoItem.Type
import com.mackenzie.waifuviewer.domain.video.StarInfoItem
import com.mackenzie.waifuviewer.domain.video.StarItem
import com.mackenzie.waifuviewer.domain.video.TagDomainInfo
import com.mackenzie.waifuviewer.domain.video.ThumbItem
import com.mackenzie.waifuviewer.domain.video.VideoDomainItem
import com.mackenzie.waifuviewer.domain.video.VideoItemDetails

data class VideoItem(
    val id: Int,
    val title: String,
    val thumb: String,
    val url: String,
    val type: Type,
    val description: String
) {
    enum class Type { PHOTO, VIDEO, AUDIO, SERVER }
}

fun getMedia() = (1..20).map {
    VideoDomainItem(
        video = VideoItemDetails(
            videoId = it.toString(),
            title = "Title $it",
            thumb = "https://loremflickr.com/400/400/girl?lock=$it",
            url = "https://loremflickr.com/400/400/girl?lock=$it",
            embedUrl = "",
            publishDate = "",
            rating = "4.5",
            ratings = "4.5",
            views = (1000 * it).toString(),
            duration = "5:00",
            defaultThumb = "https://loremflickr.com/400/400/girl?lock=$it",
            type = getType(it).name,
            thumbs = listOf(ThumbItem(
                size = "small",
                width = "200",
                height = "200",
                src = "https://loremflickr.com/200/200/girl?lock=$it"
            )),
            tags = listOf(TagDomainInfo("tag2"), TagDomainInfo("tag2")),

            stars = listOf(StarItem(
                star = StarInfoItem(
                    starName = "Star 01 $it",
                    starThumb = "https://loremflickr.com/400/400/cat?lock=$it"
                )
            ), StarItem(
                star = StarInfoItem(
                    starName = "Star 02 $it",
                    starThumb = "https://loremflickr.com/400/400/cat?lock=$it"
                )
            ))

        )
    )
}

fun getMedia2() = (1..20).map {
    VideoItem(
        it,
        "Title $it",
        "https://loremflickr.com/400/400/cat?lock=1",
        "https://loremflickr.com/400/400/girl?lock=$it",
        getType(it),
        "Generic Description $it"
    )
}

fun getHentaiServers() = (50..69).map {
    VideoItem(
        it,
        getNameById(it),
        getImageFromServerId(it).ifBlank { "https://loremflickr.com/400/400/girl?lock=$it" },
        getServerUrlById(it),
        Type.SERVER,
        "Generic Description $it"
    )
}

fun getLiveCamsServers() = (80..87).map {
    VideoItem(
        it,
        getNameById(it),
        getImageFromServerId(it).ifBlank { "https://loremflickr.com/400/400/girl?lock=$it" },
        getServerUrlById(it),
        Type.SERVER,
        "Generic Description $it"
    )
}

fun getVideoServers() = (1..22).map {
    VideoItem(
        it,
        getNameById(it),
        getImageFromServerId(it).ifBlank { "https://loremflickr.com/400/400/girl?lock=$it" },
        getServerUrlById(it),
        Type.SERVER,
        "Generic Description of ${getNameById(it)}"
    )
}

fun getType(id: Int): Type {
    return when (id % 5) {
        0 -> Type.VIDEO
        1 -> Type.AUDIO
        else -> Type.PHOTO
    }
}

fun getNameById(id: Int): String {
    return when (id) {
        1 -> "PornHub"
        2 -> "RedTube"
        3 -> "Beeg"
        4 -> "Eporner"
        5 -> "Tube8"
        6 -> "XHamster"
        7 -> "YouJizz"
        8 -> "YouPorn"
        9 -> "XVideos"
        10 -> "XNXX"
        11 -> "TNAFLix"
        12 -> "Blowjobs"
        13 -> "SpangBang"
        14 -> "PornTrex"
        15 -> "HQPorner"
        16 -> "Analdin"
        17 -> "XXXFiles"
        18 -> "PornSlash"
        19 -> "WatchPorn"
        20 -> "WhoresHub"
        21 -> "Porn.com"
        22 -> "Porn HD"

        50 -> "Hanime.tv"
        51 -> "HentaiCloud"
        52 -> "HentaiGasm"
        53 -> "HentaiMama 1"
        54 -> "HentaiMama 2"
        55 -> "HentaiMama 3"
        56 -> "HentaiTube 1"
        57 -> "HentaiTube 2"
        58 -> "HentaiPlay"
        59 -> "MuchoHentai"
        60 -> "Naughty Machinima"
        61 -> "OHentai"
        62 -> "PorCore"
        63 -> "xAnimePorn"
        64 -> "AniPorn"
        65 -> "ZZCartoon"
        66 -> "PornHub Hentai"
        67 -> "HentaiHeaven 1"
        68 -> "HentaiHeaven 2"
        69 -> "HentaiHeaven 3"

        80 -> "Chaturbate"
        81 -> "Amateur.tv"
        82 -> "BongaCams"
        83 -> "Cam4"
        84 -> "Camsoda"
        85 -> "CamWhoresBay"
        86 -> "StripChat"
        87 -> "Streamate"
        else -> "Server Name Unknown"
    }
}

/*fun getAnimeServerNameById(id: Int): String {
    return when (id) {
        50 -> "Hanime.tv"
        51 -> "HentaiCloud"
        52 -> "HentaiGasm"
        53 -> "HentaiMama 1"
        54 -> "HentaiMama 2"
        55 -> "HentaiMama 3"
        56 -> "HentaiTube 1"
        57 -> "HentaiTube 2"
        58 -> "HentaiPlay"
        59 -> "MuchoHentai"
        60 -> "Naughty Machinima"
        61 -> "OHentai"
        62 -> "PorCore"
        63 -> "xAnimePorn"
        64 -> "AniPorn"
        65 -> "ZZCartoon"
        66 -> "PornHub"
        67 -> "HentaiHeaven 1"
        68 -> "HentaiHeaven 2"
        69 -> "HentaiHeaven 3"
        else -> "Server Name Unknown"
    }
}*/

/*fun getLiveCamNameById(id: Int): String {
    return when (id) {
        80 -> "Chaturbate"
        81 -> "Amateur.tv"
        82 -> "BongaCams"
        83 -> "Cam4"
        84 -> "Camsoda"
        85 -> "CamWhoresBay"
        86 -> "StripChat"
        87 -> "Streamate"
        // 88 -> "MyFreeCams"
        else -> "Server Name Unknown"
    }
}*/

fun getServerUrlById(id: Int): String {
    return when (id) {
        1 -> "https://es.pornhub.com"
        2 -> "https://es.redtube.com"
        3 -> "https://beeg.com"
        4 -> "https://www.eporner.com"
        5 -> "https://www.tube8.com"
        6 -> "https://www.xhamster.com"
        7 -> "https://www.youjizz.com"
        8 -> "https://www.youporn.com"
        9 -> "https://www.xvideos.com"
        10 -> "https://www.xnxx.com"
        11 -> "https://www.tnaflix.com"
        12 -> "https://blowjobs.pro"
        13 -> "https://es.spankbang.com"
        14 -> "https://www.porntrex.com"
        15 -> "https://hqporner.com"
        16 -> "https://www.analdin.com"
        17 -> "https://www.xxxfiles.com"
        18 -> "https://www.pornslash.com"
        19 -> "https://watchporn.to"
        20 -> "https://www.whoreshub.com"
        21 -> "https://www.porn.com"
        22 -> "https://www.pornhd.com"

        /*1 -> "https://es.pornhub.com/video/"  // PornHub
        2 -> "https://es.redtube.com/newest/" // RedTube
        3 -> "https://www.beeg.com/" // Beeg No funciona
        4 -> "https://www.eporner.com/" // Eporner Funciona Bien ***
        5 -> "https://tube8.com/" // Tube8 No funciona
        6 -> "https://www.xhamster.com/" // XHamster Funciona sin thumbs
        7 -> "https://www.youjizz.com/" // YouJizz Funciona sin thumbs
        8 -> "https://www.youporn.com/" // YouPorn No funciona
        9 -> "https://www.xvideos.com/" // XVideos Funciona sin thumbs
        10 -> "https://www.xnxx.es/"  // XNXX No funciona
        11 -> "https://www.tnaflix.com/"  // TNAFLix No funciona
        12 -> "https://blowjobs.pro/" // Blowjobs.pro No funciona
        13 -> "https://es.spankbang.com/" // SpangBang No funciona
        14 -> "https://www.porntrex.com/" // PornTrex Funciona Bien ***
        15 -> "https://hqporner.com/" // HQPorner No funciona
        16 -> "https://www.analdin.com/" // Analdin No funciona
        17 -> "https://www.xxxfiles.com/" // XXXFiles No funciona
        18 -> "https://www.pornslash.com/" // PornSlash Funciona Bien ***
        19 -> "https://watchporn.to/" // WatchPorn No funciona
        20 -> "https://www.whoreshub.com/" // WhoresHub No funciona
        21 -> "https://www.porn.com/" // Porn.com No funciona
        22 -> "https://www.pornhd.com/" // Porn HD No funciona*/

        50 -> "https://hanime.tv/" // Hanime.tv
        51 -> "https://www.hentaicloud.com/" // HentaiCloud
        52 -> "https://hentaigasm.com/" // HentaiGasm
        53 -> "https://hentaimama.io/" // HentaiMama
        54 -> "https://hentaimama.tv" // HentaiMama
        55 -> "https://hentaimama.xxx" // HentaiMama
        56 -> "https://www.hentaitube.online/" // HentaiTube
        57 -> "https://hentaitube.icu/" // HentaiTube
        58 -> "https://hentaiplay.net/" // HentaiPlay
        59 -> "https://muchohentai.com/home" // MuchoHentai
        60 -> "https://www.naughtymachinima.com/" // Naughty Machinima
        61 -> "https://ohentai.org" // OHentai
        62 -> "https://porcore.com" // PorCore
        63 -> "https://xanimeporn.com/" // xAnimePorn
        64 -> "https://aniporn.com/most-popular/" // AniPorn
        65 -> "https://www.zzcartoon.com" // ZZCartoon
        66 -> "https://es.pornhub.com/categories/hentai" // PornHub Hentai
        67 -> "https://hentaiheaven.com" // HentaiHeaven
        68 -> "https://hentaiheaven.xxx" // HentaiHeaven
        69 -> "https://hentaiheaven.icu" // HentaiHeaven
        else -> ""
    }
}

fun getEmbedUrl(serverId: Int, videoId: String): String {
    if (videoId.isEmpty()) return ""
    return when (serverId) {
        1 -> "https://es.pornhub.com/embed/$videoId"
        2 -> "https://es.redtube.com/embed/$videoId"
        3 -> "https://beeg.com/embed/$videoId"
        4 -> "https://www.eporner.com/embed/$videoId/"
        5 -> "https://www.tube8.com/embed/$videoId"
        6 -> "https://www.xhamster.com/embed/$videoId"
        7 -> "https://www.youjizz.com/embed/$videoId"
        8 -> "https://www.youporn.com/embed/$videoId"
        9 -> "https://www.xvideos.com/embedframe/$videoId"
        10 -> "https://www.xnxx.com/embedframe/$videoId"
        11 -> "https://www.tnaflix.com/embed/$videoId"
        13 -> "https://spankbang.com/$videoId/embed/"
        14 -> "https://www.porntrex.com/embed/$videoId"
        15 -> "https://hqporner.com/embed/$videoId"
        18 -> "https://www.pornslash.com/embed/$videoId"
        20 -> "https://www.whoreshub.com/embed/$videoId"
        21 -> "https://www.porn.com/videos/embed/$videoId"
        22 -> "https://www.pornhd.com/embed/$videoId"
        else -> ""
    }
}

fun getImageFromServerId(id: Int): String {
    return when (id) {
        1 -> "https://img.icons8.com/color/512/pornhub.png"  // PornHub
        2 -> "https://static.wikia.nocookie.net/logopedia/images/a/ad/RedTube_2007_logo.png/revision/latest/scale-to-width-down/284?cb=20230616170943" // RedTube
        3 -> "https://logos-world.net/wp-content/uploads/2023/01/Beeg-Logo.png" // Beeg
        4 -> "https://www.blackhatworld.com/data/avatars/o/1814/1814745.jpg?1695663155" // Eporner
        5 -> "https://firebounty.com/image/912-tube8" // Tube8
        6 -> "https://stripcash.com/blog/content/images/size/w2000/2024/12/2024-12-18_17-29-35.jpg" // XHamster
        7 -> "https://www.dafont.com/forum/attach/orig/8/0/806734.png?1" // YouJizz
        8 -> "https://static0.polygonimages.com/wordpress/wp-content/uploads/chorus/uploads/chorus_asset/file/15030072/youporn-logo.0.0.1485620315.jpg?q=50&fit=crop&w=608&h=342&dpr=1.5" // YouPorn
        9 -> "https://thumbs.dreamstime.com/b/xvideos-pornographic-video-sharing-viewing-website-december-most-visited-pornographic-website-according-142175224.jpg" // XVideos
        10 -> "https://i.redd.it/83qah8xpljee1.jpg"  // XNXX
        11 -> "https://static.semrush.com/power-pages/media/favicons/tnaflix-com-favicon-dab11f7b.png"  // TNAFLix
        12 -> "https://ih1.redbubble.net/image.5390711201.6861/bg,f8f8f8-flat,750x,075,f-pad,750x1000,f8f8f8.jpg" // Blowjobs.pro
        13 -> "https://spankbangs.co.uk/wp-content/uploads/2024/06/spankbang-com-favicon-17110d01.png" // SpangBang
        14 -> "https://ptx.cdntrex.com/contents/videos_screenshots/2848000/2848250/preview.jpg" // PornTrex
        15 -> "https://tse1.mm.bing.net/th?q=hqporner+com" // HQPorner
        16 -> "https://s3.eu-central-1.amazonaws.com/asg-mediakit-logos-prod/uploads/trafokit_website/logo/1/analdin__1_.jpg" // Analdin
        17 -> "https://cdn2.f-cdn.com/contestentries/1673227/33004112/5dda66473abbd_thumbCard.jpg" // XXXFiles
        18 -> "https://logoeps.com/wp-content/uploads/2013/04/porn-star-vector-logo.png" // PornSlash
        19 -> "https://cbx-prod.b-cdn.net/COLOURBOX62623450.jpg?width=800&height=800&quality=70" // WatchPorn
        20 -> "https://wh.cdntrex.com/contents/videos_screenshots/439000/439461/preview.mp4.jpg" // WhoresHub
        21 -> "https://logos-world.net/wp-content/uploads/2023/01/Porn.-com-Logo-500x281.png" // Porn.com
        22 -> "https://logos-world.net/wp-content/uploads/2023/01/PornHD-Logo-500x281.png" // Porn HD

        50 -> "https://ih1.redbubble.net/image.2357425361.6067/raf,360x360,075,t,fafafa:ca443f4786.u2.jpg"  // Hanime.tv
        51 -> "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTrLP_RgKcH7Ws83XP9us0Tjdv0cewIFyk0ag&s" // HentaiCloud
        52 -> "https://hentaigasm.tv/wp-content/uploads/2024/12/HG.png" // HentaiGasm
        53 -> "https://hentaimama.tv/wp-content/uploads/2022/08/1-favicon_PNG.png" // HentaiMama
        54 -> "https://hentaimama.tv/wp-content/uploads/2022/08/1-favicon_PNG.png" // HentaiMama
        55 -> "https://hentaimama.tv/wp-content/uploads/2022/08/1-favicon_PNG.png" // HentaiMama
        56 -> "https://ih1.redbubble.net/image.1118561511.1643/bg,f8f8f8-flat,750x,075,f-pad,750x1000,f8f8f8.jpg" // HentaiTube
        57 -> "https://ih1.redbubble.net/image.1118561511.1643/bg,f8f8f8-flat,750x,075,f-pad,750x1000,f8f8f8.jpg" // HentaiTube
        58 -> "https://www.soviet-power.com/image/cache/data/2021/w165%20%D0%B0-550x550.jpg" // HentaiPlay
        59 -> "https://cdn2.steamgriddb.com/logo_thumb/3bbc8b8ac2f0e75cafa24ec9b9530352.png" // MuchoHentai
        60 -> "https://static.wikia.nocookie.net/logopedia/images/1/1c/Machinima.svg/revision/latest/scale-to-width-down/200?cb=20161120075354" // Naughty Machinima
        61 -> "https://pandatools.org/wp-content/uploads/2024/06/image-54.png" // OHentai
        62 -> "https://thumbs.dreamstime.com/b/hentai-rosette-stamp-imitation-grunge-style-designed-round-ribbon-small-crowns-blue-vector-rubber-print-text-texture-136328212.jpg" // PorCore
        63 -> "https://assets.thepornmap.com/wp-content/uploads/20251104195912/xanimeporn.png" // xAnimePorn
        64 -> "https://ei.rdtcdn.com/m=eOhlbe/media/pics/sites/006/590/561/cover1687211108/1687211108.jpg" // AniPorn
        65 -> "https://cdn.displate.com/artwork/380x270/2023-01-11/258b544708e5360b2a577e1311c67a40_ebb0719b47c2dcc503bbd57cb226caa8.jpg" // ZZCartoon
        66 -> "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVC6zfQDXZ3GLXR2HebVdgj-n7vAdPB0jxbQ&s" // PornHub
        67 -> "https://preview.redd.it/bi0o1d5qw7y21.jpg?auto=webp&s=55351ad97b3ae9503562da00d2d96d62f858899f" // HentaiHeaven
        68 -> "https://preview.redd.it/bi0o1d5qw7y21.jpg?auto=webp&s=55351ad97b3ae9503562da00d2d96d62f858899f" // HentaiHeaven
        69 -> "https://preview.redd.it/bi0o1d5qw7y21.jpg?auto=webp&s=55351ad97b3ae9503562da00d2d96d62f858899f" // HentaiHeaven

        80 -> "https://logowik.com/content/uploads/images/chaturbate1720166505.logowik.com.webp" // Chaturbate
        81-> "https://www.kamastudioagencia.com/wp-content/uploads/2024/10/amateur-scaled.jpg"  // Amateur.tv
        82 -> "https://juanbustos.com/wp-content/uploads/2019/02/BongaCams_01-copia.jpg"  // BongaCams
        83 -> "https://webcamstartup.com/wp-content/uploads/2024/07/CAM4_Site_Logo.png"  // Cam4
        84 -> "https://play-lh.googleusercontent.com/BByJrJkoUsr1zl4-B16qjyfIlSZxvbiqaga27HCF_EebNkkQfIf2QgX4bXnWhBRMpF4"  // Camsoda
        85 -> "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTwCS5SYQKu0_tGhtO6d4sJuajxhBtobyCLgw&s" // CamWhoresBay
        86 -> "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQtMQA8Eky7VirE7acAwsscAqsOm3MrxSNvXg&s" // StripChat
        87 -> "https://media.licdn.com/dms/image/v2/C560BAQF0a5MYbE0T2g/company-logo_200_200/company-logo_200_200/0/1630645346067?e=2147483647&v=beta&t=cImXQFmlhNwHsGbfz9Hx80D4jcN9q-BpWm3XKdZ_3Is" // Streamate
        else -> "https://loremflickr.com/400/400/girl?lock=$id"
    }
}