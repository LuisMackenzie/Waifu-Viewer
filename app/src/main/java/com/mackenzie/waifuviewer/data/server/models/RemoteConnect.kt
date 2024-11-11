package com.mackenzie.waifuviewer.data.server.models

import com.mackenzie.waifuviewer.data.server.OpenAIService
import com.mackenzie.waifuviewer.data.server.WaifuBestService
import com.mackenzie.waifuviewer.data.server.WaifuImService
import com.mackenzie.waifuviewer.data.server.WaifuPicService
import com.mackenzie.waifuviewer.data.server.WaifuTraceMoeService

data class RemoteConnect(
    val serviceIm: WaifuImService,
    val servicePic: WaifuPicService,
    val serviceBest: WaifuBestService,
    val serviceMoe: WaifuTraceMoeService,
    val serviceOpenAi: OpenAIService
    )
