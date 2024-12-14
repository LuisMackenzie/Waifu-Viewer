package com.mackenzie.waifuviewer.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.databinding.ActivityNavHostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavHostActivity : AppCompatActivity(R.layout.activity_nav_host) {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var binding: ActivityNavHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavHostBinding.inflate(layoutInflater)
        firebaseAnalytics = Firebase.analytics
        enableEdgeToEdge()

        // Navigation Compose
        // TODO doing
    }
}