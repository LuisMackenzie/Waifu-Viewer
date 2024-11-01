package com.mackenzie.waifuviewer.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.databinding.ActivityNavHostBinding
import com.mackenzie.waifuviewer.ui.common.isNightModeActive
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

        // Navigation Compose
        /*setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "selector") {
                composable("selector") {
                    // SelectorScreen()
                }
                composable("waifu_screen") {
                    // WaifuScreen()
                }
                composable(
                    route = "detail"
                    // arguments = listOf(navArgument("waifuId") { type = NavType.IntType })
                ) { backStackEntry ->
                    // val waifuId = backStackEntry.arguments?.getInt("waifuId")
                    // requireNotNull(waifuId)
                    // DetailScreen(waifuId = waifuId)
                }
                composable("favorite") {
                    // FavoriteScreen()
                }
                composable("waifu_gpt") {
                    WaifuGptScreenContent()
                }
            }
        }*/

        // val isNavbarVisible = isNavigationActive()

        // val isNavbarVisible2 = isSystemNavBarVisible()

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