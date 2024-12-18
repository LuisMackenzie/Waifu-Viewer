package com.mackenzie.waifuviewer.usecases.gen

import com.mackenzie.waifuviewer.data.OpenAiRepository
import com.mackenzie.waifuviewer.domain.ImageRequestBody
import javax.inject.Inject

class GetImageResponseUseCase @Inject constructor(private val repo: OpenAiRepository) {

    suspend operator fun invoke(apiKey: String, body: ImageRequestBody) = repo.imageGeneration(apiKey, body)

}