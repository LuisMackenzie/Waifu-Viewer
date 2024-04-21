package com.mackenzie.waifuviewer.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import com.mackenzie.waifuviewer.ui.common.isNavigationActive
import com.mackenzie.waifuviewer.ui.common.isNightModeActive
import com.mackenzie.waifuviewer.ui.common.isSystemNavBarVisible
import com.mackenzie.waifuviewer.ui.common.isSystemNavBarVisible2
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavHostActivity : AppCompatActivity(R.layout.activity_nav_host) {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var binding: ActivityNavHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            enableEdgeToEdge()
            val isNightModeVisible = isNightModeActive()
            Log.e("NavHostActivity", "isNightModeVisible=$isNightModeVisible")

        }
        binding = ActivityNavHostBinding.inflate(layoutInflater)
        firebaseAnalytics = Firebase.analytics

        val isNavbarVisible = isNavigationActive()

        val isNavbarVisible2 = isSystemNavBarVisible()

    }


    /*@RequiresApi(Build.VERSION_CODES.R)
    private fun isNavBarVisible():Boolean {
        val windowInsets = WindowInsets.CONSUMED
        val isShow = windowInsets.isVisible(WindowInsets.Type.navigationBars())
        val cutOut = windowInsets.isVisible(WindowInsets.Type.displayCutout())
        val temp1 = windowInsets.isVisible(WindowInsets.Type.systemBars())
        val temp2 = windowInsets.isVisible(WindowInsets.Type.captionBar())
        val isConsumed = windowInsets.isConsumed
        Log.e("NavHostActivity", "isNavigationBarVisible=$isShow, isConsumed=$isConsumed, cutOut=$cutOut, temp1=$temp1, temp2=$temp2")
        return isShow
    }*/

    /*private fun isNavBarVisibleCompat():Boolean {
        val windowInsets = WindowInsetsCompat.CONSUMED
        val isShow = windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
        val cutOut = windowInsets.isVisible(WindowInsetsCompat.Type.displayCutout())
        val temp1 = windowInsets.isVisible(WindowInsetsCompat.Type.systemBars())
        val temp2 = windowInsets.isVisible(WindowInsetsCompat.Type.captionBar())
        val isConsumed = windowInsets.isConsumed
        Log.e("NavHostActivity", "isNavigationBarVisible=$isShow, isConsumed=$isConsumed, cutOut=$cutOut, temp1=$temp1, temp2=$temp2")
        return isShow
    }*/

    private fun hideInmersiveMode() {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.show(WindowInsetsCompat.Type.navigationBars())
    }

    private fun showInmersiveMode() {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
    }

}