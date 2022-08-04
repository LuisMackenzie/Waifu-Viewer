package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.datasource.WaifusPicLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusPicRemoteDataSource
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.domain.Error
import kotlinx.coroutines.flow.Flow

class WaifusPicRepository(
    private val localPicDataSource: WaifusPicLocalDataSource,
    private val remotePicDataSource: WaifusPicRemoteDataSource
    ) {

    val savedWaifusPic = localPicDataSource.waifusPic

    fun findPicsById(id: Int): Flow<WaifuPicItem> = localPicDataSource.findPicById(id)

    suspend fun requestWaifusPics(isNsfw: String, tag: String): Error? {
        if(localPicDataSource.isPicsEmpty()) {
            val waifusPics = remotePicDataSource.getRandomWaifusPics(isNsfw, tag)
            waifusPics.fold(ifLeft = { return it }) {
                localPicDataSource.savePics(it)
            }
        }
        return null
    }

    suspend fun requestNewWaifusPics(isNsfw: String, tag: String): Error? {
        val waifusPics = remotePicDataSource.getRandomWaifusPics(isNsfw, tag)
        waifusPics.fold(ifLeft = { return it }) {
            localPicDataSource.savePics(it)
        }
        return null
    }

    suspend fun requestOnlyWaifuPic(): WaifuPicItem {
        val waifuPic = remotePicDataSource.getOnlyWaifuPics()
        localPicDataSource.saveOnlyPics(waifuPic)
        return waifuPic
    }

    suspend fun switchPicsFavorite(picsItem: WaifuPicItem) :Error? {
        val updatedWaifu = picsItem.copy(isFavorite = !picsItem.isFavorite)
        return localPicDataSource.savePics(listOf(updatedWaifu))
    }
}