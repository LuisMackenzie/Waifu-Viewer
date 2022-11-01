package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.datasource.FavoriteLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusBestGifLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusBestPngLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusBestRemoteDataSource
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuBestItemGif
import com.mackenzie.waifuviewer.domain.WaifuBestItemPng
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WaifusBestRepository @Inject constructor(
    private val localPngDataSource: WaifusBestPngLocalDataSource,
    private val localGifDataSource: WaifusBestGifLocalDataSource,
    private val favDataSource: FavoriteLocalDataSource,
    private val remoteDataSource: WaifusBestRemoteDataSource
) {

    val savedWaifusPng = localPngDataSource.waifusPng
    val savedWaifusGif = localGifDataSource.waifusGif

    fun findPngById(id: Int): Flow<WaifuBestItemPng> = localPngDataSource.findPngById(id)
    fun findGifById(id: Int): Flow<WaifuBestItemGif> = localGifDataSource.findGifById(id)

    suspend fun requestWaifusPng(tag: String): Error? {
        if(localPngDataSource.isPngEmpty()) {
            val waifus = remoteDataSource.getRandomWaifusBestPng(tag)
            waifus.fold(ifLeft = {return it}) {
                localPngDataSource.savePng(it)
            }
        }
        return null
    }

    suspend fun requestWaifusGif(tag: String): Error? {
        if(localGifDataSource.isGifEmpty()) {
            val waifus = remoteDataSource.getRandomWaifusBestGif(tag)
            waifus.fold(ifLeft = {return it}) {
                localGifDataSource.saveGif(it)
            }
        }
        return null
    }

    suspend fun requestNewWaifusPng(tag: String): Error? {
        val waifus = remoteDataSource.getRandomWaifusBestPng(tag)
        waifus.fold(ifLeft = {return it}) {
            localPngDataSource.savePng(it)
        }
        return null
    }

    suspend fun requestNewWaifusGif(tag: String): Error? {
        val waifus = remoteDataSource.getRandomWaifusBestGif(tag)
        waifus.fold(ifLeft = {return it}) {
            localGifDataSource.saveGif(it)
        }
        return null
    }

    suspend fun requestOnlyWaifuPng(): WaifuBestItemPng? {
        val waifuPic = remoteDataSource.getOnlyWaifuBestPng()
        if (waifuPic != null) return waifuPic else return null
    }

    suspend fun requestOnlyWaifuGif(): WaifuBestItemGif? {
        val waifuPic = remoteDataSource.getOnlyWaifuBestGif()
        if (waifuPic != null) return waifuPic else return null
    }

    suspend fun requestClearWaifusPng(): Error? {
        val error = localPngDataSource.deleteAllPng()
        if (error != null) return error else return null
    }

    suspend fun requestClearWaifusGif(): Error? {
        val error = localGifDataSource.deleteAllGif()
        if (error != null) return error else return null
    }

    suspend fun switchPngFavorite(pngItem: WaifuBestItemPng) :Error? {
        val updatedWaifu = pngItem.copy(isFavorite = !pngItem.isFavorite)
        if (updatedWaifu.isFavorite) {
            val error = favDataSource.savePng(updatedWaifu)
            if (error != null) return error
        }
        return localPngDataSource.saveOnlyPng(updatedWaifu)
    }

    suspend fun switchGifFavorite(gifItem: WaifuBestItemGif) :Error? {
        val updatedWaifu = gifItem.copy(isFavorite = !gifItem.isFavorite)
        if (updatedWaifu.isFavorite) {
            val error = favDataSource.saveGif(updatedWaifu)
            if (error != null) return error
        }
        return localGifDataSource.saveOnlyGif(updatedWaifu)
    }

}