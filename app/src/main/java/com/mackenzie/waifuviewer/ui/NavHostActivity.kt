package com.mackenzie.waifuviewer.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.databinding.ActivityNavHostBinding
import com.mackenzie.waifuviewer.ui.common.nav.NavItem
import com.mackenzie.waifuviewer.ui.common.nav.Navigation
import com.mackenzie.waifuviewer.ui.splash.SplashScreenRoute
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
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = NavItem.SplashScreen.baseRoute) {
                composable(NavItem.SplashScreen.baseRoute) {
                    SplashScreenRoute(navController)
                    // Text("SplashScreen")
                }
                composable(NavItem.SelectorScreen.baseRoute) {
                    // TODO
                    // SelectorScreenContentRoute(navController)
                    Text("SelectorScreen")

                    // TODO navegar a WaifuScreen
                    // navController.navigate(NavItem.WaifuScreen.baseRoute)

                }
            }
            /*Surface(color = MaterialTheme.colorScheme.background) {

            }*/
        }
    }
}