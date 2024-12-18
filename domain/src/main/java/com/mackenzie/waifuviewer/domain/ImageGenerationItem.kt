package com.mackenzie.waifuviewer.domain

data class ImageGenerationItem(
    val urls: List<String>? = null,
    val errorMessage: String? = null,
    val apiResponseBody: ImageGenerationItemResponse?
)

data class ImageGenerationItemResponse(
    val created: Long? = null,
    val data: List<ImageGenerateItemData>? = null,
    var error: TextCompletionErrorItem? = null,
)

data class ImageGenerateItemData(
    val url: String
)