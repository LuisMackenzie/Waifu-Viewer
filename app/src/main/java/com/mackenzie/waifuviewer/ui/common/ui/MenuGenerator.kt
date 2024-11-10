package com.mackenzie.waifuviewer.ui.common.ui

import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.GeminiMenuItem

fun menuGenerator(): List<GeminiMenuItem> {
    return listOf(
        GeminiMenuItem("waifu_summarize", R.string.menu_summarize_title, R.string.menu_summarize_description),
        GeminiMenuItem("waifu_info", R.string.menu_reason_title, R.string.menu_reason_description),
        GeminiMenuItem("waifu_chat", R.string.menu_chat_title, R.string.menu_chat_description)
    )
}