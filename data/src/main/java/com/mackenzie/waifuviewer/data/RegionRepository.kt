package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.datasource.LocationDataSource
import com.mackenzie.waifuviewer.data.PermissionChecker.Permission.COARSE_LOCATION
import javax.inject.Inject

class RegionRepository @Inject constructor(
    private val locationDataSource: LocationDataSource,
    private val permissionChecker: PermissionChecker
    ) {

    companion object {
        const val DEFAULT_REGION = "US"
    }

    suspend fun findLastRegion(): String {
        return if (permissionChecker.check(COARSE_LOCATION)) {
            locationDataSource.findLastRegion() ?: DEFAULT_REGION
        } else {
            DEFAULT_REGION
        }
    }
}