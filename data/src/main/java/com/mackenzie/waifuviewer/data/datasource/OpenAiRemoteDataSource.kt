package com.mackenzie.waifuviewer.data.datasource

import arrow.core.Either
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.ImageGenerationItemResponse
import com.mackenzie.waifuviewer.domain.ImageRequestBody
import com.mackenzie.waifuviewer.domain.TextCompletionItemResponse
import com.mackenzie.waifuviewer.domain.TextRequestBody

interface OpenAiRemoteDataSource {

    suspend fun textCompletion(
        apiKey: String,
        requestBody: TextRequestBody,
    ): Either<Error, TextCompletionItemResponse?>

    suspend fun imageGeneration(
        apiKey: String,
        requestBody: ImageRequestBody,
    ): Either<Error, ImageGenerationItemResponse?>
}