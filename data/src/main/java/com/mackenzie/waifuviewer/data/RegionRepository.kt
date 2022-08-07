package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.datasource.LocationDataSource
import com.mackenzie.waifuviewer.data.PermissionChecker.Permission.COARSE_LOCATION
import javax.inject.Inject

class RegionRepository  @Inject constructor(
    private val locationDataSource: LocationDataSource,
    private val permissionChecker: PermissionChecker
    ) {

    companion object {
        private const val DEFAULT_REGION = "US"
    }

    suspend fun findLastRegion(): String {
        return if (permissionChecker.check(COARSE_LOCATION)) {
            locationDataSource.findLastRegion() ?: DEFAULT_REGION
        } else {
            DEFAULT_REGION
        }
    }

    // private val locationDataSource: LocationDataSource = PlayServicesLocationDataSource(application)
    /*private val coarsePermissionChecker = PermissionChecker(
        application,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )*/
    // private val geocoder = Geocoder(application)

    // suspend fun findLastRegion(): String = findLastLocation().toRegion()

    /*private suspend fun findLastLocation(): Location? {
        val success = coarsePermissionChecker.check()
        return if (success) locationDataSource.findLastLocation() else null
    }*/

    /*private fun Location?.toRegion(): String {
        val addresses = this?.let {
            geocoder.getFromLocation(latitude, longitude, 1)
        }
        return addresses?.firstOrNull()?.countryCode ?: com.mackenzie.waifuviewer.data.RegionRepository.DEFAULT_REGION
    }*/
}