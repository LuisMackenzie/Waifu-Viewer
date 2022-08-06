package com.mackenzie.waifuviewer.data.datasource

import arrow.core.Either
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.domain.WaifuPicItem

interface WaifusImRemoteDataSource {
    suspend fun getRandomWaifusIm(
        isNsfw: Boolean,
        tag: String,
        isGif: Boolean,
        orientation: String
    ): Either<Error? , List<WaifuImItem>>

    suspend fun getOnlyWaifuIm(): WaifuImItem
}
