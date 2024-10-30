package com.mackenzie.waifuviewer.data.datasource

import arrow.core.Either
import com.mackenzie.waifuviewer.domain.AnimeSearchItem
import com.mackenzie.waifuviewer.domain.Error

interface WaifusMoeRemoteDataSource {

    suspend fun getMoeWaifuSearch(
        imageUrl: String
    ): Either<Error, List<AnimeSearchItem>>

}