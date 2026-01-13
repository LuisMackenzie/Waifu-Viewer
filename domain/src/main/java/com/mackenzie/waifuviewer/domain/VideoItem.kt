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

fun getHentaiServers() = (51..65).map {
    VideoItem(
        it,
        getAnimeServerNameById(it),
        getImageFromServerId(it).ifBlank { "https://loremflickr.com/400/400/girl?lock=$it" },
        getServerUrlById(it),
        Type.SERVER,
        "Generic Description $it"
    )
}

fun getLiveCamsServers() = (66..73).map {
    VideoItem(
        it,
        getLiveCamNameById(it),
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
        else -> "Server Name Unknown"
    }
}

fun getAnimeServerNameById(id: Int): String {
    return when (id) {
        51 -> "Hanime.tv"
        52 -> "HentaiCloud"
        53 -> "HentaiGasm"
        54 -> "HentaiMama"
        55 -> "HentaiTube"
        56 -> "HentaiPlay"
        57 -> "MuchoHentai"
        58 -> "Naughty Machinima"
        59 -> "OHentai"
        60 -> "PorCore"
        61 -> "xAnimePorn"
        62 -> "AniPorn"
        63 -> "ZZCartoon"
        64 -> "PornHub"
        65 -> "HentaiHeaven"
        else -> "Server Name Unknown"
    }
}

fun getLiveCamNameById(id: Int): String {
    return when (id) {
        66 -> "Chaturbate"
        67 -> "Amateur.tv"
        68 -> "BongaCams"
        69 -> "Cam4"
        70 -> "Camsoda"
        71 -> "CamWhoresBay"
        72 -> "StripChat"
        73 -> "Streamate"
        // 9 -> "MyFreeCams"
        else -> "Server Name Unknown"
    }
}

fun getServerUrlById(id: Int): String {
    return when (id) {
        1 -> "https://es.pornhub.com/video/"  // PornHub
        2 -> "https://es.redtube.com/newest" // RedTube
        3 -> "https://beeg.com/" // Beeg
        4 -> "https://www.eporner.com/" // Eporner
        5 -> "https://www.tube8.com/" // Tube8
        6 -> "https://xhamster.com/" // XHamster
        else -> ""
    }
}

fun getImageFromServerId(id: Int): String {
    return when (id) {
        1 -> "https://img.icons8.com/color/512/pornhub.png"  // PornHub
        2 -> "https://avatars.githubusercontent.com/u/46095600?s=200&v=4" // RedTube
        3 -> "https://lh3.googleusercontent.com/YrLQ2iF13cX-RBTf-0iM5gBcDm3woauAzoT-AmMXGhRq-R48iBALY5lSDSy8ciMaoGN9" // Beeg
        4 -> "https://www.blackhatworld.com/data/avatars/o/1814/1814745.jpg?1695663155" // Eporner
        5 -> "https://firebounty.com/image/912-tube8" // Tube8
        6 -> "https://stripcash.com/blog/content/images/size/w2000/2024/12/2024-12-18_17-29-35.jpg" // XHamster
        7 -> "https://1000logos.net/wp-content/uploads/2025/08/YouJizz-logo-500x281.jpg" // YouJizz
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

        51 -> "https://ih1.redbubble.net/image.2357425361.6067/raf,360x360,075,t,fafafa:ca443f4786.u2.jpg"  // Hanime.tv
        52 -> "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTrLP_RgKcH7Ws83XP9us0Tjdv0cewIFyk0ag&s" // HentaiCloud
        53 -> "https://hentaigasm.tv/wp-content/uploads/2024/12/HG.png" // HentaiGasm
        54 -> "https://hentaimama.tv/wp-content/uploads/2022/08/1-favicon_PNG.png" // HentaiMama
        55 -> "https://ih1.redbubble.net/image.1118561511.1643/bg,f8f8f8-flat,750x,075,f-pad,750x1000,f8f8f8.jpg" // HentaiTube
        56 -> "https://www.soviet-power.com/image/cache/data/2021/w165%20%D0%B0-550x550.jpg" // HentaiPlay
        57 -> "https://cdn2.steamgriddb.com/logo_thumb/3bbc8b8ac2f0e75cafa24ec9b9530352.png" // MuchoHentai
        58 -> "https://static.wikia.nocookie.net/logopedia/images/1/1c/Machinima.svg/revision/latest/scale-to-width-down/200?cb=20161120075354" // Naughty Machinima
        59 -> "https://pandatools.org/wp-content/uploads/2024/06/image-54.png" // OHentai
        60 -> "https://thumbs.dreamstime.com/b/hentai-rosette-stamp-imitation-grunge-style-designed-round-ribbon-small-crowns-blue-vector-rubber-print-text-texture-136328212.jpg" // PorCore
        61 -> "https://assets.thepornmap.com/wp-content/uploads/20251104195912/xanimeporn.png" // xAnimePorn
        62 -> "https://ei.rdtcdn.com/m=eOhlbe/media/pics/sites/006/590/561/cover1687211108/1687211108.jpg" // AniPorn
        63 -> "https://cdn.displate.com/artwork/380x270/2023-01-11/258b544708e5360b2a577e1311c67a40_ebb0719b47c2dcc503bbd57cb226caa8.jpg" // ZZCartoon
        64 -> "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVC6zfQDXZ3GLXR2HebVdgj-n7vAdPB0jxbQ&s" // PornHub
        65 -> "https://preview.redd.it/bi0o1d5qw7y21.jpg?auto=webp&s=55351ad97b3ae9503562da00d2d96d62f858899f" // HentaiHeaven

        66 -> "https://logowik.com/content/uploads/images/chaturbate1720166505.logowik.com.webp" // Chaturbate
        67-> "https://www.kamastudioagencia.com/wp-content/uploads/2024/10/amateur-scaled.jpg"  // Amateur.tv
        68 -> "https://juanbustos.com/wp-content/uploads/2019/02/BongaCams_01-copia.jpg"  // BongaCams
        69 -> "https://webcamstartup.com/wp-content/uploads/2024/07/CAM4_Site_Logo.png"  // Cam4
        70 -> "https://play-lh.googleusercontent.com/BByJrJkoUsr1zl4-B16qjyfIlSZxvbiqaga27HCF_EebNkkQfIf2QgX4bXnWhBRMpF4"  // Camsoda
        71 -> "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTwCS5SYQKu0_tGhtO6d4sJuajxhBtobyCLgw&s" // CamWhoresBay
        72 -> "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQtMQA8Eky7VirE7acAwsscAqsOm3MrxSNvXg&s" // StripChat
        73 -> "https://media.licdn.com/dms/image/v2/C560BAQF0a5MYbE0T2g/company-logo_200_200/company-logo_200_200/0/1630645346067?e=2147483647&v=beta&t=cImXQFmlhNwHsGbfz9Hx80D4jcN9q-BpWm3XKdZ_3Is" // Streamate
        else -> "https://loremflickr.com/400/400/girl?lock=$id"
    }
}