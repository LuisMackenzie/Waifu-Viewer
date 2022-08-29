package com.mackenzie.waifuviewer

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.mackenzie.waifuviewer.data.db.WaifuPicDao
import com.mackenzie.waifuviewer.data.server.MockWebServerRule
import com.mackenzie.waifuviewer.data.server.ServerImDataSource
import com.mackenzie.waifuviewer.data.server.ServerPicDataSource
import com.mackenzie.waifuviewer.data.server.fromJson
import com.mackenzie.waifuviewer.ui.NavHostActivity
import com.mackenzie.waifuviewer.ui.buildPicDatabaseWaifus
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class WaifuPicsInstrumentationTests {

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
    lateinit var picDao: WaifuPicDao

    @Inject
    lateinit var picDataSource: ServerPicDataSource

    @Before
    fun setUp() {
        mockWebServerRule.server.enqueue(MockResponse().fromJson("response_pics.json"))
        hiltRule.inject()
    }

    @Test
    fun check_PICS_mock_server_is_working() = runTest {
        val imWaifus = picDataSource.getRandomWaifusPics("sfw", "waifu")
        imWaifus.fold({ throw Exception(it.toString()) }) {
            Assert.assertEquals("https://i.waifu.pics/~oi3Eln.png", it[1].url)
        }
    }

    @Test
    fun check_PICS_mock_server_is_working_2() = runTest {
        val imWaifus = picDataSource.getRandomWaifusPics("sfw", "waifu")
        imWaifus.fold({ throw Exception(it.toString()) }) {
            Assert.assertEquals("https://i.waifu.pics/wkNZidT.png", it[3].url)
        }
    }

    @Test
    fun check_4_PICS_items_db() = runTest {
        picDao.insertAllWaifuPics(buildPicDatabaseWaifus(1, 2, 3, 4))
        Assert.assertEquals(4, picDao.waifuPicsCount())
    }

    @Test
    fun check_6_PICS_items_db()  = runTest {
        picDao.insertAllWaifuPics(buildPicDatabaseWaifus(5, 6, 7, 8, 9, 10))
        Assert.assertEquals(6, picDao.waifuPicsCount())
    }

}