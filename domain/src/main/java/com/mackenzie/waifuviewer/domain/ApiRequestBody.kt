package com.mackenzie.waifuviewer.domain

data class TextRequestBody (
    val prompt: String,
    val model: String = "gpt-4o-mini",
    val temperature: Float = 0.7f,
    val maxTokens: Int = 500
)

data class ImageRequestBody (
    val prompt: String,
    val model: String = "dall-e-3",
    val size: String = "1024x1024",
    val generateCount: Int = 1,
    val responseFormat: String = "url"
)
