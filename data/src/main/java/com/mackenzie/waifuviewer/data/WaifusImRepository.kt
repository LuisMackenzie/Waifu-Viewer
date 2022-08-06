package com.mackenzie.waifuviewer.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
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

    suspend fun requestWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: Boolean): Either<Error?, List<WaifuImItem>> =
        remoteImDataSource.getRandomWaifusIm(isNsfw, tag, isGif, getOrientation(orientation))
            .fold({ return it.left() }) {
                if(localImDataSource.isImEmpty()) {
                    localImDataSource.saveIm(it)
                }
                return it.right()
            }

    suspend fun requestNewWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: Boolean): Either<Error?, List<WaifuImItem>> =
        remoteImDataSource.getRandomWaifusIm(isNsfw, tag, isGif, getOrientation(orientation))
            .fold(ifLeft = { return it.left() }) {
                localImDataSource.saveIm(it)
                return it.right()
            }

    suspend fun requestOnlyWaifuIm(): WaifuImItem {
        val waifuIm = remoteImDataSource.getOnlyWaifuIm()
        // localImDataSource.saveOnlyIm(waifuIm)
        return waifuIm
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
        return localImDataSource.saveIm(listOf(updatedWaifu))
    }
}
