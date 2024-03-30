package com.mackenzie.waifuviewer.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mackenzie.waifuviewer.BuildConfig
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.databinding.ActivityNavHostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavHostActivity : AppCompatActivity(R.layout.activity_nav_host) {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var binding: ActivityNavHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        binding = ActivityNavHostBinding.inflate(layoutInflater)
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

    private fun inmersiveMode() {
        val windowInsetsController = WindowCompat.getInsetsController(window, binding.root)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())  // FUNC
//        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
//        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())  // FUNC
//        windowInsetsController.hide(WindowInsetsCompat.Type.mandatorySystemGestures())
//        windowInsetsController.hide(WindowInsetsCompat.Type.tappableElement())
//        windowInsetsController.hide(WindowInsetsCompat.Type.systemGestures())

    }

    private fun inmersiveMode2() {
        WindowCompat.getInsetsController(window, binding.root)
            .hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun hideBottomSysBar() {
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

}