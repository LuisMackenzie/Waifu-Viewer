package com.mackenzie.waifuviewer.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.mackenzie.waifuviewer.data.datasource.WaifusPicLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusPicRemoteDataSource
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuImItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WaifusPicRepository @Inject constructor(
    private val localPicDataSource: WaifusPicLocalDataSource,
    private val remotePicDataSource: WaifusPicRemoteDataSource
    ) {

    val savedWaifusPic = localPicDataSource.waifusPic

    fun findPicsById(id: Int): Flow<WaifuPicItem> = localPicDataSource.findPicById(id)

    suspend fun requestWaifusPics(isNsfw: String, tag: String): Error? {
        if(localPicDataSource.isPicsEmpty()) {
            val waifus = remotePicDataSource.getRandomWaifusPics(isNsfw, tag)
            waifus.fold(ifLeft = {return it}) {
                localPicDataSource.savePics(it)
            }
        }
        return null
    }


    suspend fun requestNewWaifusPics(isNsfw: String, tag: String): Either<Error, List<WaifuPicItem>> {
        remotePicDataSource.getRandomWaifusPics(isNsfw, tag)
            .fold(ifLeft = { return it.left() }) {
                localPicDataSource.savePics(it)
                return it.right()
            }
    }

    suspend fun requestOnlyWaifuPic(): WaifuPicItem {
        val waifuPic = remotePicDataSource.getOnlyWaifuPics()
        // localPicDataSource.saveOnlyPics(waifuPic)
        return waifuPic
    }

    suspend fun switchPicsFavorite(picsItem: WaifuPicItem) :Error? {
        val updatedWaifu = picsItem.copy(isFavorite = !picsItem.isFavorite)
        return localPicDataSource.saveOnlyPics(updatedWaifu)
    }
}