package com.mackenzie.waifuviewer

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.mackenzie.waifuviewer.data.db.FavoriteDao
import com.mackenzie.waifuviewer.data.db.WaifuImDao
import com.mackenzie.waifuviewer.data.db.WaifuPicDao
import com.mackenzie.waifuviewer.di.AppModule
import com.mackenzie.waifuviewer.ui.NavHostActivity
import com.mackenzie.waifuviewer.ui.buildFavDatabaseWaifus
import com.mackenzie.waifuviewer.ui.buildImDatabaseWaifus
import com.mackenzie.waifuviewer.ui.buildPicDatabaseWaifus
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
    lateinit var picDao: WaifuPicDao

    @Inject
    lateinit var imDao: WaifuImDao

    @Inject
    lateinit var favoriteDao: FavoriteDao

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_4_IM_items_db() = runTest {
        imDao.insertAllWaifuIm(buildImDatabaseWaifus(1, 2, 3, 4))
        assertEquals(4, imDao.waifuImCount())
    }

    @Test
    fun check_6_IM_items_db()  = runTest {
        imDao.insertAllWaifuIm(buildImDatabaseWaifus(5, 6, 7, 8, 9, 10))
        assertEquals(6, imDao.waifuImCount())
    }

    @Test
    fun check_4_PICS_items_db() = runTest {
        picDao.insertAllWaifuPics(buildPicDatabaseWaifus(1, 2, 3, 4))
        assertEquals(4, picDao.waifuPicsCount())
    }

    @Test
    fun check_6_PICS_items_db()  = runTest {
        picDao.insertAllWaifuPics(buildPicDatabaseWaifus(5, 6, 7, 8, 9, 10))
        assertEquals(6, picDao.waifuPicsCount())
    }

    @Test
    fun check_4_FAV_items_db() = runTest {
        favoriteDao.insertAllFavorite(buildFavDatabaseWaifus(1, 2, 3, 4))
        assertEquals(4, favoriteDao.favoriteCount())
    }

    @Test
    fun check_6_FAV_items_db()  = runTest {
        favoriteDao.insertAllFavorite(buildFavDatabaseWaifus(5, 6, 7, 8, 9, 10))
        assertEquals(6, favoriteDao.favoriteCount())
    }

}