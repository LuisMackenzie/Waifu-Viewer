package com.mackenzie.waifuviewer.domain

data class TextRequestBody (
    val prompt: String,
    val model: String,
    val temperature: Float,
    val maxTokens: Int
)

data class ImageRequestBody (
    val prompt: String,
    val size: String,
    val generateCount: Int,
    val responseFormat: String
)
