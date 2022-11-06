package com.mackenzie.waifuviewer.data.server

import arrow.core.Either
import com.mackenzie.waifuviewer.data.datasource.WaifusBestRemoteDataSource
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuBestItem
import javax.inject.Inject

class ServerBestDataSource @Inject constructor(private val remoteService: RemoteConnect): WaifusBestRemoteDataSource {

    override suspend fun getRandomWaifusBestPng(tag: String): Either<Error, List<WaifuBestItem>> = tryCall {
        remoteService.serviceBest
            .getRandomWaifuBestPng(tag)
            .waifus
            .toDomainModelPng()
    }

    override suspend fun getRandomWaifusBestGif(tag: String): Either<Error, List<WaifuBestItem>> = tryCall {
        remoteService.serviceBest
            .getRandomWaifuBestGif(tag)
            .waifus
            .toDomainModelGif()
    }

    override suspend fun getOnlyWaifuBestPng(): WaifuBestItem? {
        val waifu: WaifuBestItem?
        try {
            waifu = remoteService.serviceBest
                .getOnlyRandomWaifuBestPng()
                .waifus.first()
                .toDomainModel()
        } catch (e: Exception) {
            return null
        }
        return waifu
    }

    override suspend fun getOnlyWaifuBestGif(): WaifuBestItem? {
        val waifu: WaifuBestItem?
        try {
            waifu = remoteService.serviceBest
                .getOnlyRandomWaifuBestGif()
                .waifus.first()
                .toDomainModel()
        } catch (e: Exception) {
            return null
        }
        return waifu
    }
}

private fun List<WaifuBestPng>.toDomainModelPng(): List<WaifuBestItem> = map { it.toDomainModel() }

private fun WaifuBestPng.toDomainModel(): WaifuBestItem =
    WaifuBestItem(
        0,
        artistHref,
        artistName,
        "",
        sourceUrl,
        url,
        false
    )

private fun List<WaifuBestGif>.toDomainModelGif(): List<WaifuBestItem> = map { it.toDomainModel() }

private fun WaifuBestGif.toDomainModel(): WaifuBestItem =
    WaifuBestItem(
        0,
        "",
        "",
        animeName,
        "",
        url,
        false
    )