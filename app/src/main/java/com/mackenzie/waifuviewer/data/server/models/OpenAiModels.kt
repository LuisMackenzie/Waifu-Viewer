package com.mackenzie.waifuviewer.data.server.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

// TEXT COMPLETION REQUEST BODY
@JsonClass(generateAdapter = true)
data class TextCompletionApiRequestBody (
    @Json(name = "prompt") val prompt: String,
    @Json(name = "model")  val model: String,
    @Json(name = "temperature")  val temperature: Float,
    @Json(name = "max_tokens") val maxTokens: Int,
) : OpenAIRequestBody

// TEXT COMPLETION RESPONSE BODY
@JsonClass(generateAdapter = true)
data class TextCompletionApiResponse(
    @Json(name = "id") var id: String? = null,
    @Json(name = "created") var created: String? = null,
    @Json(name = "chioces") var choices: List<TextCompletionChoice>? = null,
    @Json(name = "error") var error: TextCompletionError? = null,
)

@Parcelize
@JsonClass(generateAdapter = true)
data class TextCompletionChoice(
    @Json(name = "text") val text: String?,
    @Json(name = "index") val index: Int?,
    @Json(name = "finish_reason") val finish_reason: String?,
) : Parcelable

@JsonClass(generateAdapter = true)
data class TextCompletionError(
    @Json(name = "message") val message: String?,
    @Json(name = "type") var type: String? = null,
    @Json(name = "code") var code: String? = null,
)

// IMAGE GENERATION REQUEST BODY

@JsonClass(generateAdapter = true)
data class ImageGenerationApiRequestBody (
    @Json(name = "prompt") val prompt: String,
    @Json(name = "size") val size: String,
    @Json(name = "n") val generateCount: Int,
    @Json(name = "response_format") val responseFormat: String,
) : OpenAIRequestBody

// IMAGE GENERATION RESPONSE BODY
@JsonClass(generateAdapter = true)
data class ImageGenerationApiResponse(
    @Json(name = "created") val created: Long? = null,
    @Json(name = "data") val data: List<ImageGenerateImageData>? = null,
    @Json(name = "error") var error: TextCompletionError? = null,
)

@Parcelize
@JsonClass(generateAdapter = true)
data class ImageGenerateImageData(
    @Json(name = "url") val url: String
) : Parcelable