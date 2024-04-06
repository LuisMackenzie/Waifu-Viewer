package com.mackenzie.waifuviewer.ui.common

import com.google.ai.client.generativeai.type.generationConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.mackenzie.waifuviewer.domain.GenerativeType

open class GenerativeIAModels(key: String) {

    val config = generationConfig {
        temperature = 0.7f
    }

    val gm15ProLatest = GenerativeModel(
        modelName = GenerativeType().gm15ProLatest,
        apiKey = key,
        generationConfig = config
    )

    val gm15pro = GenerativeModel(
        modelName = GenerativeType().gm15pro,
        apiKey = key,
        generationConfig = config
    )

    val gm10ProLatest = GenerativeModel(
        modelName = GenerativeType().gm10ProLatest,
        apiKey = key,
        generationConfig = config
    )

    val gm10Pro = GenerativeModel(
        modelName = GenerativeType().gm10pro,
        apiKey = key,
        generationConfig = config
    )

    val gm10ProGeneral = GenerativeModel(
        modelName = GenerativeType().gm10,
        apiKey = key,
        generationConfig = config
    )

    val gm10ProVisionLatest = GenerativeModel(
        modelName = GenerativeType().gm10ProVisionLatest,
        apiKey = key,
        generationConfig = config
    )

    val gm10ProVision = GenerativeModel(
        modelName = GenerativeType().gm10ProVision,
        apiKey = key,
        generationConfig = config
    )
}