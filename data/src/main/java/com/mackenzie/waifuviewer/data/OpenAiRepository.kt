package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.datasource.OpenAiRemoteDataSource
import com.mackenzie.waifuviewer.domain.ImageRequestBody
import com.mackenzie.waifuviewer.domain.TextRequestBody
import javax.inject.Inject

class OpenAiRepository @Inject constructor(val remoteDataSource: OpenAiRemoteDataSource) {

    suspend fun textCompletion(
        apiKey: String,
        requestBody: TextRequestBody
    ) = remoteDataSource.textCompletion("Bearer $apiKey", requestBody)

    suspend fun imageGeneration(
        apiKey: String,
        requestBody: ImageRequestBody
    ) = remoteDataSource.imageGeneration("Bearer $apiKey", requestBody)

}