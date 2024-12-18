package com.mackenzie.waifuviewer.data.server

import arrow.core.Either
import com.mackenzie.waifuviewer.data.datasource.OpenAiRemoteDataSource
import com.mackenzie.waifuviewer.data.server.models.ImageGenerateImageData
import com.mackenzie.waifuviewer.data.server.models.ImageGenerationApiRequestBody
import com.mackenzie.waifuviewer.data.server.models.ImageGenerationApiResponse
import com.mackenzie.waifuviewer.data.server.models.RemoteConnect
import com.mackenzie.waifuviewer.data.server.models.TextCompletionApiRequestBody
import com.mackenzie.waifuviewer.data.server.models.TextCompletionApiResponse
import com.mackenzie.waifuviewer.data.server.models.TextCompletionChoice
import com.mackenzie.waifuviewer.data.server.models.TextCompletionError
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.ImageGenerateItemData
import com.mackenzie.waifuviewer.domain.ImageGenerationItemResponse
import com.mackenzie.waifuviewer.domain.ImageRequestBody
import com.mackenzie.waifuviewer.domain.TextCompletionChoiceItem
import com.mackenzie.waifuviewer.domain.TextCompletionErrorItem
import com.mackenzie.waifuviewer.domain.TextCompletionItemResponse
import com.mackenzie.waifuviewer.domain.TextRequestBody
import javax.inject.Inject

class OpenAiDataSource @Inject constructor(private val remoteService: RemoteConnect): OpenAiRemoteDataSource {

    override suspend fun textCompletion(
        apiKey: String,
        requestBody: TextRequestBody
    ): Either<Error, TextCompletionItemResponse?> = tryCall {
        remoteService.serviceOpenAi
            .textCompletion(apiKey, requestBody.fromDomainModel())
            .body()?.toDomainModel()
    }


    override suspend fun imageGeneration(
        apiKey: String,
        requestBody: ImageRequestBody
    ): Either<Error, ImageGenerationItemResponse?> = tryCall {
        remoteService.serviceOpenAi
            .imageGeneration(apiKey, requestBody.fromDomainModel())
            .body()?.toDomainModel()
    }
}

// Text Mappers
private fun TextCompletionApiResponse.toDomainModel(): TextCompletionItemResponse = TextCompletionItemResponse(
    id = id,
    created = created,
    choices = choices?.map { it.toDomainModel() },
    error = error?.toDomainModel()
)

private fun TextCompletionChoice.toDomainModel(): TextCompletionChoiceItem = TextCompletionChoiceItem(
    text = text,
    index = index,
    finish_reason = finish_reason
)

private fun TextCompletionError.toDomainModel(): TextCompletionErrorItem = TextCompletionErrorItem(
    message = message,
    type = type,
    code = code
)

private fun TextRequestBody.fromDomainModel(): TextCompletionApiRequestBody = TextCompletionApiRequestBody(
    prompt = prompt,
    model = model,
    temperature = temperature,
    maxTokens = maxTokens
)

// Image Mappers

private fun ImageGenerationApiResponse.toDomainModel(): ImageGenerationItemResponse = ImageGenerationItemResponse(
    created = created,
    data = data?.map { it.toDomainModel() },
    error = error?.toDomainModel()
)

private fun ImageGenerateImageData.toDomainModel(): ImageGenerateItemData = ImageGenerateItemData(
    url = url
)

private fun ImageRequestBody.fromDomainModel(): ImageGenerationApiRequestBody = ImageGenerationApiRequestBody(
    prompt = prompt,
    model = model,
    size = size,
    generateCount = generateCount,
    responseFormat = responseFormat
)