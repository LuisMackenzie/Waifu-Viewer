package com.mackenzie.waifuviewer.models.datasource

import android.util.Log
import com.mackenzie.waifuviewer.App
import com.mackenzie.waifuviewer.models.Error
import com.mackenzie.waifuviewer.models.RemoteConnection
import com.mackenzie.waifuviewer.models.db.WaifuPicItem
import com.mackenzie.waifuviewer.models.tryCall

class WaifusPicRepository(application: App) {

    private val localPicDataSource = WaifusPicLocalDataSource(application.db.waifuPicDao())
    private val remotePicDataSource = WaifusPicRemoteDataSource()
    val savedWaifusPic = localPicDataSource.waifusPic

    fun findPicsById(id: Int) = localPicDataSource.findPicById(id)

    suspend fun requestWaifusPics(isNsfw: String, tag: String): Error? = tryCall {
        if(localPicDataSource.isPicsEmpty()) {
            val waifusPics = remotePicDataSource.getRandomWaifusPics(isNsfw, tag)
            localPicDataSource.savePics(waifusPics.toLocalModelPics())
        } else {
            Log.e("Waifus Repository", "LocalDataSource PICS IS NOT EMPTY")
        }
    }

    suspend fun requestNewWaifusPics(isNsfw: String, tag: String): Error? = tryCall {
        val waifusPics = remotePicDataSource.getRandomWaifusPics(isNsfw, tag)
        localPicDataSource.savePics(waifusPics.toLocalModelPics())
    }

    suspend fun requestOnlyWaifuPic() = RemoteConnection.servicePics.getOnlyWaifuPic()

    suspend fun requestOnlyWaifuPicFix(): Error? = tryCall { RemoteConnection.servicePics.getOnlyWaifuPic() }

    suspend fun switchPicsFavorite(picsItem: WaifuPicItem) = tryCall {
        val updatedWaifu = picsItem.copy(isFavorite = !picsItem.isFavorite)
        localPicDataSource.savePics(listOf(updatedWaifu))
    }

}

private fun List<String>.toLocalModelPics() : List<WaifuPicItem> = map { it.toLocalModelPics() }

private fun String.toLocalModelPics(): WaifuPicItem = WaifuPicItem(url = this, isFavorite = false)