package com.mackenzie.waifuviewer.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.mackenzie.waifuviewer.BuildConfig
import com.mackenzie.waifuviewer.ui.gemini.chat.WaifuChatViewModel
import com.mackenzie.waifuviewer.ui.gemini.info.WaifuInfoViewModel
import com.mackenzie.waifuviewer.ui.gemini.resume.SummarizeWaifuViewModel

val GenerativeViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        viewModelClass: Class<T>,
        extras: CreationExtras
    ): T {
        val config = generationConfig {
            temperature = 0.7f
        }

        return with(viewModelClass) {
            when {
                isAssignableFrom(SummarizeWaifuViewModel::class.java) -> {
                    // Initialize a GenerativeModel with the `gemini-flash` AI model
                    // for text generation
                    val generativeModel = GenerativeModel(
                        modelName = "gemini-1.5-flash-latest",
                        apiKey = BuildConfig.apikey,
                        generationConfig = config
                    )
                    SummarizeWaifuViewModel(generativeModel)
                }

                isAssignableFrom(WaifuInfoViewModel::class.java) -> {
                    // Initialize a GenerativeModel with the `gemini-flash` AI model
                    // for multimodal text generation
                    val generativeModel = GenerativeModel(
                        modelName = "gemini-1.5-flash-latest",
                        apiKey = BuildConfig.apikey,
                        generationConfig = config
                    )
                    WaifuInfoViewModel(generativeModel)
                }

                isAssignableFrom(WaifuChatViewModel::class.java) -> {
                    // Initialize a GenerativeModel with the `gemini-flash` AI model for chat
                    val generativeModel = GenerativeModel(
                        modelName = "gemini-1.5-flash-latest",
                        apiKey = BuildConfig.apikey,
                        generationConfig = config
                    )
                    WaifuChatViewModel(generativeModel)
                }

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${viewModelClass.name}")
            }
        } as T
    }
}
