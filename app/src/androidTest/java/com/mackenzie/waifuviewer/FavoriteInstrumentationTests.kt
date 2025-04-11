package com.mackenzie.waifuviewer

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.mackenzie.testshared.sampleImWaifu
import com.mackenzie.waifuviewer.data.db.dao.FavoriteDao
import com.mackenzie.waifuviewer.ui.NavHostActivity
import com.mackenzie.waifuviewer.ui.buildFavDatabaseWaifus
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class FavoriteInstrumentationTests {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val locationPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        "android.permission.ACCESS_COARSE_LOCATION"
    )

    @get:Rule(order = 2)
    val activityRule = ActivityScenarioRule(NavHostActivity::class.java)

    @Inject
    lateinit var favoriteDao: FavoriteDao


    @Before
    fun setUp() {

        hiltRule.inject()
    }

    @Test
    fun check_4_FAV_items_db() = runTest {
        favoriteDao.insertAllFavorite(buildFavDatabaseWaifus(1, 2, 3, 4))
        Assert.assertEquals(4, favoriteDao.favoriteCount())
        val newWaifu = sampleImWaifu
    }

    @Test
    fun check_6_FAV_items_db()  = runTest {
        favoriteDao.insertAllFavorite(buildFavDatabaseWaifus(5, 6, 7, 8, 9, 10))
        Assert.assertEquals(6, favoriteDao.favoriteCount())
    }
}