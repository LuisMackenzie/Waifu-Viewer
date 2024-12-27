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
import com.mackenzie.waifuviewer.ui.common.saveServerMode
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

}