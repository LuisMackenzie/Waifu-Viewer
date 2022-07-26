package com.mackenzie.waifuviewer.data.datasource

import android.util.Log
import com.mackenzie.waifuviewer.App
import com.mackenzie.waifuviewer.data.Error
import com.mackenzie.waifuviewer.data.RemoteConnection
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.framework.datasource.RoomPicDataSource
import com.mackenzie.waifuviewer.framework.datasource.ServerPicDataSource

class WaifusPicRepository(
    private val localPicDataSource: WaifusPicLocalDataSource,
    private val remotePicDataSource: WaifusPicRemoteDataSource
    ) {

    // private val localPicDataSource: WaifusPicLocalDataSource = RoomPicDataSource(application.db.waifuPicDao())
    // private val remotePicDataSource: WaifusPicRemoteDataSource = ServerPicDataSource()
    val savedWaifusPic = localPicDataSource.waifusPic

    fun findPicsById(id: Int) = localPicDataSource.findPicById(id)

    suspend fun requestWaifusPics(isNsfw: String, tag: String): Error? = tryCall {
        if(localPicDataSource.isPicsEmpty()) {
            val waifusPics = remotePicDataSource.getRandomWaifusPics(isNsfw, tag)
            localPicDataSource.savePics(waifusPics)
        } else {
            Log.e("Waifus Repository", "LocalDataSource PICS IS NOT EMPTY")
        }
    }

    suspend fun requestNewWaifusPics(isNsfw: String, tag: String): Error? = tryCall {
        val waifusPics = remotePicDataSource.getRandomWaifusPics(isNsfw, tag)
        localPicDataSource.savePics(waifusPics)
    }

    suspend fun requestOnlyWaifuPic() = RemoteConnection.servicePics.getOnlyWaifuPic()

    suspend fun requestOnlyWaifuPicFix(): Error? = tryCall { RemoteConnection.servicePics.getOnlyWaifuPic() }

    suspend fun switchPicsFavorite(picsItem: WaifuPicItem) = tryCall {
        val updatedWaifu = picsItem.copy(isFavorite = !picsItem.isFavorite)
        localPicDataSource.savePics(listOf(updatedWaifu))
    }
}