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
        analytics.logEvent(FirebaseAnalytics.Event.PURCHASE, null)
        crashlytics.recordException(SSLHandshakeException("Error de certificado de fin de semana"))




        enableEdgeToEdge()
        setContent {
            MainTheme {
                Navigation()
            }
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                // requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

}