package com.mackenzie.waifuviewer

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.mackenzie.waifuviewer.data.db.FavoriteDao
import com.mackenzie.waifuviewer.data.db.WaifuImDao
import com.mackenzie.waifuviewer.data.db.WaifuPicDao
import com.mackenzie.waifuviewer.di.AppModule
import com.mackenzie.waifuviewer.ui.NavHostActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class WaifuInstrumentationTests {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val locationPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        "android.permission.ACCESS_COARSE_LOCATION"
    )

    @get:Rule(order = 2)
    val activityRule = ActivityScenarioRule(NavHostActivity::class.java)


    @Inject
    lateinit var waifuPicDao: WaifuPicDao

    @Inject
    lateinit var waifuImDao: WaifuImDao

    @Inject
    lateinit var favoriteDao: FavoriteDao

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_it_works() {
        Thread.sleep(3000)
    }

}