package com.mackenzie.waifuviewer.data.server

import arrow.core.Either
import com.mackenzie.waifuviewer.data.datasource.WaifusBestRemoteDataSource
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuBestItemGif
import com.mackenzie.waifuviewer.domain.WaifuBestItemPng
import javax.inject.Inject

class ServerBestDataSource @Inject constructor(private val remoteService: RemoteConnect): WaifusBestRemoteDataSource {

    override suspend fun getRandomWaifusBestPng(tag: String): Either<Error, List<WaifuBestItemPng>> = tryCall {
        remoteService.serviceBest
            .getRandomWaifuBestPng(tag)
            .waifus
            .toDomainModelPng()
    }

    override suspend fun getOnlyWaifuBestPng(): WaifuBestItemPng? {
        val waifu: WaifuBestItemPng?
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

    override suspend fun getRandomWaifusBestGif(tag: String): Either<Error, List<WaifuBestItemGif>> = tryCall {
        remoteService.serviceBest
            .getRandomWaifuBestGif(tag)
            .waifus
            .toDomainModelGif()
    }

    override suspend fun getOnlyWaifuBestGif(): WaifuBestItemGif? {
        val waifu: WaifuBestItemGif?
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

private fun List<WaifuBestPng>.toDomainModelPng(): List<WaifuBestItemPng> = map { it.toDomainModel() }

private fun WaifuBestPng.toDomainModel(): WaifuBestItemPng =
    WaifuBestItemPng(
        0,
        artistHref,
        artistName,
        sourceUrl,
        url,
        false
    )

private fun List<WaifuBestGif>.toDomainModelGif(): List<WaifuBestItemGif> = map { it.toDomainModel() }

private fun WaifuBestGif.toDomainModel(): WaifuBestItemGif =
    WaifuBestItemGif(
        0,
        animeName,
        url,
        false
    )