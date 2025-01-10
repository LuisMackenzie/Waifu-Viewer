package com.mackenzie.waifuviewer.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.launch
import javax.net.ssl.SSLHandshakeException

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
        // crashlytics.isCrashlyticsCollectionEnabled = true
        // analytics.logEvent(FirebaseAnalytics.Event.PURCHASE, null)
        // crashlytics.recordException(SSLHandshakeException("Error de certificado de fin de semana"))

        enableEdgeToEdge()
        setContent {
            MainTheme {
                Navigation()
            }
        }
    }
}