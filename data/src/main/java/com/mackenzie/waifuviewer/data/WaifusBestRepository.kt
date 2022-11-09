package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.datasource.FavoriteLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusBestLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusBestRemoteDataSource
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuBestItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WaifusBestRepository @Inject constructor(
    private val localDataSource: WaifusBestLocalDataSource,
    private val favDataSource: FavoriteLocalDataSource,
    private val remoteDataSource: WaifusBestRemoteDataSource
) {

    val savedWaifus = localDataSource.waifus

    fun findById(id: Int): Flow<WaifuBestItem> = localDataSource.findById(id)

    suspend fun requestWaifus(tag: String): Error? {
        if(localDataSource.isEmpty()) {
            when(tag) {
                "neko","husbando","kitsune","waifu" -> {
                    val waifus = remoteDataSource.getRandomWaifusBestPng(tag)
                    waifus.fold(ifLeft = {return it}) {
                        localDataSource.save(it)
                    }
                }
                else -> {
                    val waifus = remoteDataSource.getRandomWaifusBestGif(tag)
                    waifus.fold(ifLeft = {return it}) {
                        localDataSource.save(it)
                    }
                }
            }
        }
        return null

    }

    suspend fun requestNewWaifus(tag: String): Error? {
        when(tag) {
            "neko","husbando","kitsune","waifu" -> {
                val waifus = remoteDataSource.getRandomWaifusBestPng(tag)
                waifus.fold(ifLeft = {return it}) {
                    localDataSource.save(it)
                }
            }
            else -> {
                val waifus = remoteDataSource.getRandomWaifusBestGif(tag)
                waifus.fold(ifLeft = {return it}) {
                    localDataSource.save(it)
                }
            }
        }
        return null
    }

    suspend fun requestOnlyWaifuPng(): WaifuBestItem? {
        val waifuPic = remoteDataSource.getOnlyWaifuBestPng()
        if (waifuPic != null) return waifuPic else return null
    }

    suspend fun requestOnlyWaifuGif(): WaifuBestItem? {
        val waifuPic = remoteDataSource.getOnlyWaifuBestGif()
        if (waifuPic != null) return waifuPic else return null
    }

    suspend fun requestClearWaifusBest(): Error? {
        val error = localDataSource.deleteAll()
        if (error != null) return error else return null
    }

    suspend fun switchFavorite(item: WaifuBestItem) :Error? {
        val updatedWaifu = item.copy(isFavorite = !item.isFavorite)
        if (updatedWaifu.isFavorite) {
            val error = favDataSource.saveBest(updatedWaifu)
            if (error != null) return error
        }
        return localDataSource.saveOnly(updatedWaifu)
    }

}