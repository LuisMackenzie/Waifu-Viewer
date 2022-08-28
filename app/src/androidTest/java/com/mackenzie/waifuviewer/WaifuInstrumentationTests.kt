package com.mackenzie.waifuviewer

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.mackenzie.waifuviewer.ui.NavHostActivity
import com.mackenzie.waifuviewer.ui.defaultFakeImWaifus
import org.junit.Rule
import org.junit.Test

class WaifuInstrumentationTests {

    @get:Rule
    val locationPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        "android.permission.ACCESS_COARSE_LOCATION"
    )

    @get:Rule
    val activityRule = ActivityScenarioRule(NavHostActivity::class.java)

    @Test
    fun test_it_works() {

        defaultFakeImWaifus

    }

}