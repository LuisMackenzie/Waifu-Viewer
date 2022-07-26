package com.mackenzie.waifuviewer.data.datasource

import android.location.Location

interface LocationDataSource {
    suspend fun findLastLocation(): Location?
}
