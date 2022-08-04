package com.mackenzie.waifuviewer.data.datasource

import arrow.core.Either
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuPicItem

interface WaifusPicRemoteDataSource {
    suspend fun getRandomWaifusPics(
        isNsfw: String,
        tag: String
    ): Either<Error, List<WaifuPicItem>>

    suspend fun getOnlyWaifuPics(): WaifuPicItem
}