package com.mackenzie.waifuviewer.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mackenzie.waifuviewer.BuildConfig
import com.mackenzie.waifuviewer.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavHostActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nav_host)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        firebaseAnalytics = Firebase.analytics

        val config = generationConfig {
            temperature = 0.7f
        }

        val gm15ProLatest = GenerativeModel(
            modelName = "gemini-1.5-pro-latest",
            apiKey = BuildConfig.apikey,
            generationConfig = config
        )

        val gm15pro = GenerativeModel(
            modelName = "gemini-1.5-pro",
            apiKey = BuildConfig.apikey,
            generationConfig = config
        )

        val gm10ProLatest = GenerativeModel(
            modelName = "gemini-1.0-pro-latest",
            apiKey = BuildConfig.apikey,
            generationConfig = config
        )

        val gm10Pro = GenerativeModel(
            modelName = "gemini-1.0-pro",
            apiKey = BuildConfig.apikey,
            generationConfig = config
        )

        val gm10ProGeneral = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = BuildConfig.apikey,
            generationConfig = config
        )

        val gm10ProVisionLatest = GenerativeModel(
            modelName = "gemini-1.0-pro-vision-latest",
            apiKey = BuildConfig.apikey,
            generationConfig = config
        )

        val gm10ProVision = GenerativeModel(
            modelName = "gemini-1.0-pro-vision",
            apiKey = BuildConfig.apikey,
            generationConfig = config
        )

    }

    /*private fun inmersiveMode() {
        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView)
        windowInsetsController?.hide(WindowInsetsCompat.Type.systemBars())
    }*/


}