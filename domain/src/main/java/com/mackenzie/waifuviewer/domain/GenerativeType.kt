package com.mackenzie.waifuviewer.domain

data class GenerativeType(
    val gm15ProLatest : String = "gemini-1.5-pro-latest",
    val gm15pro : String = "gemini-1.5-pro",
    val gm10ProLatest : String = "gemini-1.0-pro-latest",
    val gm10pro : String = "gemini-1.0-pro",
    val gm10 : String = "gemini-1.0",
    val gm10ProVisionLatest : String = "gemini-1.0-pro-vision-latest",
    val gm10ProVision : String = "gemini-1.0-pro-vision",
)
