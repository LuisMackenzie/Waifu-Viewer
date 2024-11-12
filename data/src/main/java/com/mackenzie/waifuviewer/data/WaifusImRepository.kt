package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.datasource.FavoriteLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusImLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusImRemoteDataSource
import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import com.mackenzie.waifuviewer.domain.Error
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WaifusImRepository @Inject constructor(
    private val localImDataSource: WaifusImLocalDataSource,
    private val favDataSource: FavoriteLocalDataSource,
    private val remoteImDataSource: WaifusImRemoteDataSource
) {

    val savedWaifusIm = localImDataSource.waifusIm

    fun findImById(id: Int): Flow<WaifuImItem> = localImDataSource.findImById(id)

    suspend fun requestWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: Boolean): Error? {
        if(localImDataSource.isImEmpty()) {
            val waifus = remoteImDataSource.getRandomWaifusIm(isNsfw, tag, isGif, getOrientation(orientation))
            waifus.fold(ifLeft = {return it}) {
                localImDataSource.saveIm(it)
            }
        }
        return null
    }

    suspend fun requestNewWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: Boolean): Error? {
        val waifus = remoteImDataSource.getRandomWaifusIm(isNsfw, tag, isGif, getOrientation(orientation))
        waifus.fold(ifLeft = {return it}) {
            localImDataSource.saveIm(it)
        }
        return null
    }

    suspend fun requestClearWaifusIm(): Error? {
        val error = localImDataSource.deleteAll()
        if (error != null) return error else return null
    }

    suspend fun requestOnlyWaifuIm(orientation: Boolean): WaifuImItem? {
        val waifuIm = remoteImDataSource.getOnlyWaifuIm(getOrientation(orientation))
        if (waifuIm != null) return waifuIm else return null
    }

    suspend fun requestWaifuImTags(): Error? {
        val waifuImTags = remoteImDataSource.getWaifuImTags()
        if (waifuImTags != null) {
            // val error = localImDataSource.saveWaifuImTags(waifuImTags)
            // if (error != null) return error
        }
        return null
    }

    private fun getOrientation(ori: Boolean): String {
        if (ori) {
            return "LANDSCAPE"
        } else {
            return "PORTRAIT"
        }
    }

    suspend fun switchImFavorite(imItem: WaifuImItem): Error? {
        val updatedWaifu = imItem.copy(isFavorite = !imItem.isFavorite)
        if (updatedWaifu.isFavorite) {
            val error = favDataSource.saveIm(updatedWaifu)
            if (error != null) return error
        }
        return localImDataSource.saveOnlyIm(updatedWaifu)
    }
}
