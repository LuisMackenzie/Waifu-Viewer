package com.mackenzie.waifuviewer.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.databinding.ActivityNavHostBinding
import com.mackenzie.waifuviewer.domain.RemoteConfigValues
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.common.nav.Navigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavHostActivity : AppCompatActivity(R.layout.activity_nav_host) {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var binding: ActivityNavHostBinding
    private var remoteValues : RemoteConfigValues = RemoteConfigValues()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavHostBinding.inflate(layoutInflater)
        firebaseAnalytics = Firebase.analytics
        enableEdgeToEdge()

        // Navigation Compose
        // TODO doing
        setContent {
            Navigation()
        }
    }

    private fun getRemoteConfig() {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = Constants.RELEASEINTERVALINSECONDS
            // minimumFetchIntervalInSeconds = Constants.DEBUGINTERVALINSECONDS
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                remoteValues = RemoteConfigValues(
                    remoteConfig.getBoolean("nsfw_mode"),
                    remoteConfig.getBoolean("waifu_gpt_service"),
                    remoteConfig.getBoolean("waifu_gemini_service"),
                    remoteConfig.getBoolean("automatic_server"),
                    false,
                    remoteConfig.getLong("server_mode").toInt(),
                    getServerMode(),
                )
                remoteValues.apply {
                    setNsfwMode(nsfwIsActive, gptIsActive, geminiIsActive)
                    setAutoMode(AutoModeIsEnabled)
                }
            } else {
                Log.e("getRemoteConfig", "Hubo un Error al recuperar de remote config: ${task.exception}")
            }
        }
    }

    private fun getServerMode(): ServerType {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val mode = sharedPref?.getString(Constants.SERVER_MODE, ServerType.NORMAL.value)
        Log.v("GetMode", "SERVER_MODE=${mode}")
        return when (mode) {
            ServerType.NORMAL.value -> ServerType.NORMAL
            ServerType.ENHANCED.value -> ServerType.ENHANCED
            ServerType.NEKOS.value -> ServerType.NEKOS
            else -> ServerType.NORMAL
        }
    }

    private fun setAutoMode(isAutomatic: Boolean) {
        if (isAutomatic) {
            // TODO
            // Toast.makeText(requireContext(), "Automatic Mode Selected", Toast.LENGTH_SHORT).show()
        } else {
            // TODO
            // Toast.makeText(requireContext(), "Manual Mode Selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setNsfwMode(nsfw: Boolean, hasGpt: Boolean, hasGemini: Boolean) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        requireNotNull(sharedPref)
        with (sharedPref.edit()) {
            putBoolean(Constants.WORK_MODE, nsfw)
            putBoolean(Constants.IS_WAIFU_GPT, hasGpt)
            putBoolean(Constants.IS_WAIFU_GEMINI, hasGemini)
            apply()
        }
    }
}