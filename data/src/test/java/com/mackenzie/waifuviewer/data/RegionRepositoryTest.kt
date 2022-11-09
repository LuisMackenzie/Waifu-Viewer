package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.PermissionChecker.Permission.COARSE_LOCATION
import com.mackenzie.waifuviewer.data.datasource.LocationDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.mock

class RegionRepositoryTest {

    @Test
    fun `Returns default region when coarse permission is not granted`(): Unit = runBlocking {
        val sut = buildRegionRepository(
            permissionChecker = mock{ on { check(COARSE_LOCATION) }.thenReturn(false) }
        )
        val region = sut.findLastRegion()
        assertEquals(RegionRepository.DEFAULT_REGION, region)
    }

    @Test
    fun `return default region when coarse permission is granted`(): Unit = runBlocking {
        val sut = buildRegionRepository(
            locationDataSource = mock{ onBlocking { findLastRegion() }.thenReturn("ES")},
            permissionChecker = mock{ on { check(COARSE_LOCATION) }.thenReturn(true) })
        val region = sut.findLastRegion()
        assertEquals("ES", region)
    }

}

private fun buildRegionRepository(
    locationDataSource: LocationDataSource = mock(),
    permissionChecker: PermissionChecker = mock()
) = RegionRepository(locationDataSource, permissionChecker)