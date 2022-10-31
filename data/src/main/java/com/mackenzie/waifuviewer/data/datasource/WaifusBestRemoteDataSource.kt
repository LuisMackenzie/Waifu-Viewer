package com.mackenzie.waifuviewer.data.datasource

import arrow.core.Either
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuBestItemGif
import com.mackenzie.waifuviewer.domain.WaifuBestItemPng

interface WaifusBestRemoteDataSource {

    suspend fun getRandomWaifusBestPng(
        tag: String
    ): Either<Error, List<WaifuBestItemPng>>

    suspend fun getOnlyWaifuBestPng(): WaifuBestItemPng?

    suspend fun getRandomWaifusBestGif(
        tag: String
    ): Either<Error, List<WaifuBestItemGif>>

    suspend fun getOnlyWaifuBestGif(): WaifuBestItemGif?
}