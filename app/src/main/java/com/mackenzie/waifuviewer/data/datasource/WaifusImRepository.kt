package com.mackenzie.waifuviewer.data.datasource

import android.util.Log
import com.mackenzie.waifuviewer.App
import com.mackenzie.waifuviewer.data.Error
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.framework.datasource.RoomImDataSource
import com.mackenzie.waifuviewer.framework.datasource.ServerImDataSource
import kotlinx.coroutines.flow.Flow

class WaifusImRepository(
    private val localImDataSource: WaifusImLocalDataSource,
    private val remoteImDataSource: WaifusImRemoteDataSource
) {

    // private val localImDataSource: WaifusImLocalDataSource = RoomImDataSource(application.db.waifuImDao())
    // private val remoteImDataSource: WaifusImRemoteDataSource = ServerImDataSource()
    val savedWaifusIm = localImDataSource.waifusIm

    fun findImById(id: Int): Flow<WaifuImItem> = localImDataSource.findImById(id)

    suspend fun requestWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: Boolean): Error? = tryCall {
        if(localImDataSource.isImEmpty()) {
            val waifusIm = remoteImDataSource.getRandomWaifusIm(isNsfw, tag, isGif, getOrientation(orientation))
            localImDataSource.saveIm(waifusIm)
        } else {
            Log.e("Waifus Repository", "LocalDataSource IM IS NOT EMPTY")
        }
    }

    suspend fun requestNewWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: Boolean): Error? = tryCall {
        val waifusIm = remoteImDataSource.getRandomWaifusIm(isNsfw, tag, isGif, getOrientation(orientation))
        localImDataSource.saveIm(waifusIm)
    }

    private fun getOrientation(ori: Boolean): String {
        if (ori) {
            return "LANDSCAPE"
        } else {
            return "PORTRAIT"
        }
    }

    suspend fun switchImFavorite(imItem: WaifuImItem) = tryCall {
        val updatedWaifu = imItem.copy(isFavorite = !imItem.isFavorite)
        localImDataSource.saveIm(listOf(updatedWaifu))
    }
}
