package com.mackenzie.waifuviewer.ui

import com.mackenzie.waifuviewer.data.PermissionChecker
import com.mackenzie.waifuviewer.data.datasource.LocationDataSource

class FakeLocationDataSource : LocationDataSource {
    var location = "US"

    override suspend fun findLastRegion(): String = location
}

class FakePermissionChecker : PermissionChecker {
    var permissionGranted = true

    override fun check(permission: PermissionChecker.Permission) = permissionGranted
}