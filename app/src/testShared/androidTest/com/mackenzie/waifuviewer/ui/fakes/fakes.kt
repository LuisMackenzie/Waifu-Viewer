package com.mackenzie.waifuviewer.ui

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