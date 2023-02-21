package com.mackenzie.waifuviewer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.mackenzie.waifuviewer.data.db.WaifuImDao
import com.mackenzie.waifuviewer.data.server.MockWebServerRule
import com.mackenzie.waifuviewer.data.server.OkHttp3IdlingResource
import com.mackenzie.waifuviewer.data.server.ServerImDataSource
import com.mackenzie.waifuviewer.data.server.fromJson
import com.mackenzie.waifuviewer.ui.NavHostActivity
import com.mackenzie.waifuviewer.ui.buildImDatabaseWaifus
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class WaifuImInstrumentationTests {

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
    lateinit var imDao: WaifuImDao

    @Inject
    lateinit var imDataSource: ServerImDataSource

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Before
    fun setUp() {
        mockWebServerRule.server.enqueue(MockResponse().fromJson("response_im.json"))
        hiltRule.inject()
        val resource = OkHttp3IdlingResource.create("okHttp", okHttpClient)
        IdlingRegistry.getInstance().register(resource)
    }

    @Test
    fun check_button_navigates_to_waifus() = runTest {
        onView(withId(R.id.btn_waifu))
            .perform(click())

        // Thread.sleep(3000)
        /// onView(withId(R.id.btn_waifu)).check(matches())
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

    /*@Test
    fun check_IM_waifu_navigates_to_detail() = runTest {
        onView(withId(R.id.recycler))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        onView(withId(R.id.tv_detail)).check(matches(hasDescendant(withText("633"))))
    }*/


    /*@Test
    fun check_IM_mock_server_is_working() = runTest {
        val imWaifus = imDataSource.getRandomWaifusIm(false, "waifu", false, "PORTRAIT")
        imWaifus.fold({ throw Exception(it.toString()) }) {
            assertEquals(4039, it[0].imageId)
        }
    }

    @Test
    fun check_IM_mock_server_is_working_2() = runTest {
        val imWaifus = imDataSource.getRandomWaifusIm(false, "waifu", false, "PORTRAIT")
        imWaifus.fold({ throw Exception(it.toString()) }) {
            assertEquals(633, it[1].imageId)
        }
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
    }*/

}