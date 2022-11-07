package com.mackenzie.waifuviewer.data.server

data class RemoteConnect(
    val serviceIm: WaifuImService,
    val servicePic: WaifuPicService,
    val serviceBest: WaifuBestService
    )
