package com.mackenzie.waifuviewer

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.mackenzie.waifuviewer.data.db.dao.WaifuBestDao
import com.mackenzie.waifuviewer.data.server.MockWebServerRule
import com.mackenzie.waifuviewer.data.server.ServerBestDataSource
import com.mackenzie.waifuviewer.ui.NavHostActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import org.junit.Rule
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class WaifuBestInstrumentationTests {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val mockWebServerRule = MockWebServerRule()

    @get:Rule(order = 2)
    val locationPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        "android.permission.ACCESS_COARSE_LOCATION"
    )

    @get:Rule(order = 3)
    val activityRule = ActivityScenarioRule(NavHostActivity::class.java)

    @Inject
    lateinit var bestDao: WaifuBestDao

    @Inject
    lateinit var bestDataSource: ServerBestDataSource

    @Inject
    lateinit var okHttpClient: OkHttpClient

}