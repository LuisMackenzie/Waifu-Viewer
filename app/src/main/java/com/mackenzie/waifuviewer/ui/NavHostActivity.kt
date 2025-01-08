package com.mackenzie.waifuviewer.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.mackenzie.waifuviewer.ui.common.getFirebaseInstance
import com.mackenzie.waifuviewer.ui.common.nav.Navigation
import com.mackenzie.waifuviewer.ui.main.ui.MainTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavHostActivity : AppCompatActivity() {

    // private lateinit var firebaseAnalytics: FirebaseAnalytics

    // open val firebaseAnalytics = getFirebaseInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Firebase.initialize(context = this)
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )

        val crashlytics = Firebase.crashlytics
        val analytics = Firebase.analytics
        // analytics.setAnalyticsCollectionEnabled(true)
        // crashlytics.setCrashlyticsCollectionEnabled(true)
        // crashlytics.isCrashlyticsCollectionEnabled = true
        // analytics.logEvent(FirebaseAnalytics.Event.PURCHASE, null)
        // crashlytics.log("Activity created in develop Mode")
        enableEdgeToEdge()
        setContent {
            MainTheme {
                Navigation()
            }
        }
    }

}