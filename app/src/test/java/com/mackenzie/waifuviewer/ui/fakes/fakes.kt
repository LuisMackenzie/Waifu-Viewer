package com.mackenzie.waifuviewer.ui

import com.mackenzie.testshared.sampleFavWaifu
import com.mackenzie.testshared.sampleImWaifu
import com.mackenzie.testshared.samplePicWaifu
import com.mackenzie.waifuviewer.data.PermissionChecker
import com.mackenzie.waifuviewer.data.datasource.LocationDataSource
import com.mackenzie.waifuviewer.data.db.WaifuImDao
import com.mackenzie.waifuviewer.data.db.WaifuImDbItem
import com.mackenzie.waifuviewer.data.server.Waifu
import com.mackenzie.waifuviewer.data.server.WaifuResult
import com.mackenzie.waifuviewer.data.server.WaifuImService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

val defaultFakeImWaifus = listOf(
    sampleImWaifu.copy(1),
    sampleImWaifu.copy(2),
    sampleImWaifu.copy(3),
    sampleImWaifu.copy(4)
)

val defaultFakePicWaifus = listOf(
    samplePicWaifu.copy(1),
    samplePicWaifu.copy(2),
    samplePicWaifu.copy(3),
    samplePicWaifu.copy(4)
)

val defaultFakeFavWaifus = listOf(
    sampleFavWaifu.copy(1),
    sampleFavWaifu.copy(2),
    sampleFavWaifu.copy(3),
    sampleFavWaifu.copy(4)
)

class FakeLocationDataSource : LocationDataSource {
    var location = "US"

    override suspend fun findLastRegion(): String = location
}

class FakePermissionChecker : PermissionChecker {
    var permissionGranted = true

    override fun check(permission: PermissionChecker.Permission) = permissionGranted
}