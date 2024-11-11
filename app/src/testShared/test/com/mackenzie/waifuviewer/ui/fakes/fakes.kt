package com.mackenzie.waifuviewer.ui

import com.mackenzie.testshared.sampleFavWaifu
import com.mackenzie.testshared.sampleImWaifu
import com.mackenzie.testshared.samplePicWaifu
import com.mackenzie.waifuviewer.data.PermissionChecker
import com.mackenzie.waifuviewer.data.datasource.LocationDataSource
import com.mackenzie.waifuviewer.data.server.OpenAIService
import com.mackenzie.waifuviewer.data.server.WaifuTraceMoeService
import com.mackenzie.waifuviewer.data.server.models.AnimeResult
import com.mackenzie.waifuviewer.data.server.models.ImageGenerationApiRequestBody
import com.mackenzie.waifuviewer.data.server.models.ImageGenerationApiResponse
import com.mackenzie.waifuviewer.data.server.models.TextCompletionApiRequestBody
import com.mackenzie.waifuviewer.data.server.models.TextCompletionApiResponse
import com.mackenzie.waifuviewer.data.server.models.TraceMoeResult
import retrofit2.Response

val defaultFakeImWaifus = listOf(
    sampleImWaifu.copy(1),
    sampleImWaifu.copy(2),
    sampleImWaifu.copy(3),
    sampleImWaifu.copy(4)
)

val defaultFakePicWaifus = listOf(
    samplePicWaifu.copy(1),
    samplePicWaifu.copy(2),
    samplePicWaifu.copy(3),
    samplePicWaifu.copy(4)
)

val defaultFakeFavWaifus = listOf(
    sampleFavWaifu.copy(1),
    sampleFavWaifu.copy(2),
    sampleFavWaifu.copy(3),
    sampleFavWaifu.copy(4)
)

class FakeLocationDataSource : LocationDataSource {
    var location = "US"

    override suspend fun findLastRegion(): String = location
}

class FakePermissionChecker : PermissionChecker {
    var permissionGranted = true

    override fun check(permission: PermissionChecker.Permission) = permissionGranted
}

class FakeRemoteOpenAiService(private val waifus: List<AnimeResult> = emptyList()) : OpenAIService {

    override suspend fun textCompletion(
        authorization: String,
        requestBody: TextCompletionApiRequestBody
    ): Response<TextCompletionApiResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun imageGeneration(
        authorization: String,
        requestBody: ImageGenerationApiRequestBody
    ): Response<ImageGenerationApiResponse> {
        TODO("Not yet implemented")
    }
}

class FakeRemoteMoeService(private val waifus: List<AnimeResult> = emptyList()) :
    WaifuTraceMoeService {

    override suspend fun searchAnime(imageUrl: String) = TraceMoeResult(
        frameCount = 0,
        error = null,
        result = waifus
    )

}