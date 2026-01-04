package com.mackenzie.waifuviewer.domain

data class VideoItem(
    val id: Int,
    val title: String,
    val thumb: String,
    val url: String,
    val type: Type,
    val description: String
) {
    enum class Type { PHOTO, VIDEO, AUDIO }
}

fun getMedia() = (1..20).map {
    VideoItem(
        it,
        "Title $it",
        "https://loremflickr.com/400/400/cat?lock=1",
        "https://loremflickr.com/400/400/girl?lock=$it",
        getType(it),
        "Generic Description $it"
    )
}

fun getHentaiServers() = (51..64).map {
    VideoItem(
        it,
        getAnimeServerNameById(it),
        "https://loremflickr.com/400/400/cat?lock=1",
        getImageFromServerId(it).ifBlank { "https://loremflickr.com/400/400/girl?lock=$it" },
        getType(it),
        "Generic Description $it"
    )
}

fun getLiveCamsServers() = (65..72).map {
    VideoItem(
        it,
        getLiveCamNameById(it),
        "https://loremflickr.com/400/400/cat?lock=1",
        getImageFromServerId(it).ifBlank { "https://loremflickr.com/400/400/girl?lock=$it" },
        getType(it),
        "Generic Description $it"
    )
}

fun getVideoServers() = (1..22).map {
    VideoItem(
        it,
        getNameById(it),
        "https://loremflickr.com/400/400/cat?lock=1",
        getImageFromServerId(it).ifBlank { "https://loremflickr.com/400/400/girl?lock=$it" },
        getType(it),
        "Generic Description of ${getNameById(it)}"
    )
}

fun getType(id: Int): VideoItem.Type {
    return when (id % 5) {
        0 -> VideoItem.Type.VIDEO
        1 -> VideoItem.Type.AUDIO
        else -> VideoItem.Type.PHOTO
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
        else -> "Server Name Unknown"
    }
}

fun getLiveCamNameById(id: Int): String {
    return when (id) {
        65 -> "Chaturbate"
        66-> "Amateur.tv"
        67 -> "BongaCams"
        68 -> "Cam4"
        69 -> "Camsoda"
        70 -> "CamWhoresBay"
        71 -> "StripChat"
        72 -> "Streamate"
        // 9 -> "MyFreeCams"
        else -> "Server Name Unknown"
    }
}

fun getImageFromServerId(id: Int): String {
    return when (id) {
        1 -> "https://img.icons8.com/color/512/pornhub.png"
        2 -> "https://avatars.githubusercontent.com/u/46095600?s=200&v=4"
        3 -> "https://lh3.googleusercontent.com/YrLQ2iF13cX-RBTf-0iM5gBcDm3woauAzoT-AmMXGhRq-R48iBALY5lSDSy8ciMaoGN9"
        4 -> "https://www.blackhatworld.com/data/avatars/o/1814/1814745.jpg?1695663155"
        5 -> "https://firebounty.com/image/912-tube8"
        6 -> "https://stripcash.com/blog/content/images/size/w2000/2024/12/2024-12-18_17-29-35.jpg"
        7 -> "https://1000logos.net/wp-content/uploads/2025/08/YouJizz-logo-500x281.jpg"
        8 -> "https://static0.polygonimages.com/wordpress/wp-content/uploads/chorus/uploads/chorus_asset/file/15030072/youporn-logo.0.0.1485620315.jpg?q=50&fit=crop&w=608&h=342&dpr=1.5"
        9 -> "https://thumbs.dreamstime.com/b/xvideos-pornographic-video-sharing-viewing-website-december-most-visited-pornographic-website-according-142175224.jpg"
        10 -> "https://i.redd.it/83qah8xpljee1.jpg"
        11 -> "https://static.semrush.com/power-pages/media/favicons/tnaflix-com-favicon-dab11f7b.png"
        12 -> "https://ih1.redbubble.net/image.5390711201.6861/bg,f8f8f8-flat,750x,075,f-pad,750x1000,f8f8f8.jpg"
        13 -> "https://spankbangs.co.uk/wp-content/uploads/2024/06/spankbang-com-favicon-17110d01.png"
        14 -> "https://ptx.cdntrex.com/contents/videos_screenshots/2848000/2848250/preview.jpg"
        15 -> "https://tse1.mm.bing.net/th?q=hqporner+com"
        16 -> "https://s3.eu-central-1.amazonaws.com/asg-mediakit-logos-prod/uploads/trafokit_website/logo/1/analdin__1_.jpg"
        17 -> "https://cdn2.f-cdn.com/contestentries/1673227/33004112/5dda66473abbd_thumbCard.jpg"
        18 -> "https://logoeps.com/wp-content/uploads/2013/04/porn-star-vector-logo.png"
        19 -> "https://cbx-prod.b-cdn.net/COLOURBOX62623450.jpg?width=800&height=800&quality=70"
        20 -> "https://wh.cdntrex.com/contents/videos_screenshots/439000/439461/preview.mp4.jpg"
        21 -> "https://logos-world.net/wp-content/uploads/2023/01/Porn.-com-Logo-500x281.png"
        22 -> "https://logos-world.net/wp-content/uploads/2023/01/PornHD-Logo-500x281.png"

        /*51 -> "Hanime.tv"
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
        64 -> "PornHub"*/

        /*65 -> "Chaturbate"
        66-> "Amateur.tv"
        67 -> "BongaCams"
        68 -> "Cam4"
        69 -> "Camsoda"
        70 -> "CamWhoresBay"
        71 -> "StripChat"
        72 -> "Streamate"*/

        else -> "https://loremflickr.com/400/400/girl?lock=$id"
    }
}