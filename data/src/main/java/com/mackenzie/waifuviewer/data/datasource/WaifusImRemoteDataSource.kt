package com.mackenzie.waifuviewer.data.datasource

import arrow.core.Either
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import com.mackenzie.waifuviewer.domain.im.WaifuImTagList

interface WaifusImRemoteDataSource {
    suspend fun getRandomWaifusIm(
        isNsfw: Boolean,
        tag: String,
        isGif: Boolean,
        orientation: String
    ): Either<Error, List<WaifuImItem>>

    suspend fun getOnlyWaifuIm(orientation: String): WaifuImItem?

    suspend fun getWaifuImTags(): WaifuImTagList?
}
