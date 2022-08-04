package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.datasource.WaifusImLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusImRemoteDataSource
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.domain.Error
import kotlinx.coroutines.flow.Flow

class WaifusImRepository(
    private val localImDataSource: WaifusImLocalDataSource,
    private val remoteImDataSource: WaifusImRemoteDataSource
) {

    val savedWaifusIm = localImDataSource.waifusIm

    fun findImById(id: Int): Flow<WaifuImItem> = localImDataSource.findImById(id)

    suspend fun requestWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: Boolean): Error? {
        if(localImDataSource.isImEmpty()) {
            val waifusIm = remoteImDataSource.getRandomWaifusIm(isNsfw, tag, isGif, getOrientation(orientation))
            waifusIm.fold(ifLeft = { return it }) {
                localImDataSource.saveIm(it)
            }
        }
        return null
    }

    suspend fun requestNewWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: Boolean): Error? {
        val waifusIm = remoteImDataSource.getRandomWaifusIm(isNsfw, tag, isGif, getOrientation(orientation))
        waifusIm.fold(ifLeft = { return it }) {
            localImDataSource.saveIm(it)
        }
        return null
    }

    suspend fun requestOnlyWaifuIm(): WaifuImItem {
        val waifuIm = remoteImDataSource.getOnlyWaifuIm()

        /*waifuIm.fold(ifLeft = { return it }) {
            localImDataSource.saveOnlyIm(it)
        }*/
        return waifuIm
    }

    // suspend fun requestOnlyWaifuIm() = remoteImDataSource.getOnlyWaifuIm()

    // suspend fun requestOnlyWaifuImFix() = remoteImDataSource.getOnlyWaifuIm()

    private fun getOrientation(ori: Boolean): String {
        if (ori) {
            return "LANDSCAPE"
        } else {
            return "PORTRAIT"
        }
    }



    suspend fun switchImFavorite(imItem: WaifuImItem): Error? {
        val updatedWaifu = imItem.copy(isFavorite = !imItem.isFavorite)
        return localImDataSource.saveIm(listOf(updatedWaifu))
    }
}
