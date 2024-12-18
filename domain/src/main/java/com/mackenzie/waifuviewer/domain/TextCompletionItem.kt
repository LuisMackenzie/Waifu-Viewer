package com.mackenzie.waifuviewer.domain

data class TextCompletionItem(
    val text: String? = null,
    val errorMessage: String? = null,
    val apiResponseBody: TextCompletionItemResponse?
)

data class TextCompletionItemResponse(
    var id: String? = null,
    var created: String? = null,
    var choices: List<TextCompletionChoiceItem>? = null,
    var error: TextCompletionErrorItem? = null,
)

data class TextCompletionChoiceItem(
    val text: String?,
    val index: Int?,
    val finish_reason: String?,
)

data class TextCompletionErrorItem(
    val message: String?,
    var type: String? = null,
    var code: String? = null,
)
