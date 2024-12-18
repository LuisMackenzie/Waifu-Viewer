package com.mackenzie.waifuviewer.data.server

import com.mackenzie.waifuviewer.data.server.models.ImageGenerationApiRequestBody
import com.mackenzie.waifuviewer.data.server.models.ImageGenerationApiResponse
import com.mackenzie.waifuviewer.data.server.models.TextCompletionApiRequestBody
import com.mackenzie.waifuviewer.data.server.models.TextCompletionApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIService {

    @Headers("content-type: application/json")
    @POST("chat/completions")
    suspend fun textCompletion(
        @Header("Authorization") authorization: String,
        @Body requestBody: TextCompletionApiRequestBody
    ): Response<TextCompletionApiResponse>

    @Headers("content-type: application/json")
    @POST("images/generations")
    suspend fun imageGeneration(
        @Header("Authorization") authorization: String,
        @Body requestBody: ImageGenerationApiRequestBody
    ): Response<ImageGenerationApiResponse>
}