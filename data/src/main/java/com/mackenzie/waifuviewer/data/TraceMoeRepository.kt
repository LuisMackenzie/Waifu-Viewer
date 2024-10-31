package com.mackenzie.waifuviewer.data

import arrow.core.Either
import com.mackenzie.waifuviewer.data.datasource.WaifusMoeRemoteDataSource
import com.mackenzie.waifuviewer.domain.AnimeSearchItem
import javax.inject.Inject

class TraceMoeRepository @Inject constructor(val remoteDataSource: WaifusMoeRemoteDataSource) {

    suspend fun getMoeWaifuSearch(imageUrl: String) = remoteDataSource.getMoeWaifuSearch(imageUrl)

    suspend fun getMoeWaifuSearch2(imageUrl: String): List<AnimeSearchItem>? {
        val searchResult = remoteDataSource.getMoeWaifuSearch(imageUrl)
        searchResult.fold(ifLeft = {return null}) {
            return it
        }
    }
}