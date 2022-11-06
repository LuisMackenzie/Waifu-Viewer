package com.mackenzie.waifuviewer.data.datasource

import arrow.core.Either
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuBestItem

interface WaifusBestRemoteDataSource {

    suspend fun getRandomWaifusBestPng(
        tag: String
    ): Either<Error, List<WaifuBestItem>>

    suspend fun getOnlyWaifuBestPng(): WaifuBestItem?

    suspend fun getRandomWaifusBestGif(
        tag: String
    ): Either<Error, List<WaifuBestItem>>

    suspend fun getOnlyWaifuBestGif(): WaifuBestItem?
}