package com.mackenzie.waifuviewer.data


import com.mackenzie.waifuviewer.data.datasource.WaifusMoeRemoteDataSource
import com.mackenzie.waifuviewer.domain.AnimeSearchItem
import javax.inject.Inject

class TraceMoeRepository @Inject constructor(val remoteDataSource: WaifusMoeRemoteDataSource) {

    suspend fun getMoeWaifuSearch(imageUrl: String) = remoteDataSource.getMoeWaifuSearch(imageUrl)

    suspend fun getMoeWaifuSearch2(imageUrl: String): List<AnimeSearchItem>? {
        val searchResult = remoteDataSource.getMoeWaifuSearch(imageUrl)
        return searchResult.fold(ifLeft = {null}) {
            it
        }
    }
}