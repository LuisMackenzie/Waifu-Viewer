package com.mackenzie.waifuviewer.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.mackenzie.waifuviewer.data.datasource.FavoriteLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusImLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusImRemoteDataSource
import com.mackenzie.waifuviewer.domain.WaifuImItem
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

    suspend fun requestOnlyWaifuIm(): WaifuImItem? {
        val waifuIm = remoteImDataSource.getOnlyWaifuIm()
        if (waifuIm != null) return waifuIm else return null
        // localImDataSource.saveOnlyIm(waifuIm)
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
        favDataSource.saveIm(updatedWaifu)
        return localImDataSource.saveOnlyIm(updatedWaifu)
    }
}
