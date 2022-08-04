package com.mackenzie.waifuviewer.data.datasource

interface LocationDataSource {
    suspend fun findLastRegion(): String?
}
