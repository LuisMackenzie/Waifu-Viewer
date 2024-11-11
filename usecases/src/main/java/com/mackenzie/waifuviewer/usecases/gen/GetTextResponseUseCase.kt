package com.mackenzie.waifuviewer.usecases.gen

import com.mackenzie.waifuviewer.data.OpenAiRepository
import com.mackenzie.waifuviewer.domain.TextRequestBody
import javax.inject.Inject

class GetTextResponseUseCase @Inject constructor(private val repo: OpenAiRepository) {

    suspend operator fun invoke(apiKey: String, body: TextRequestBody) = repo.textCompletion(apiKey, body)

}