package com.mackenzie.waifuviewer.models.datasource

import android.util.Log
import com.mackenzie.waifuviewer.App
import com.mackenzie.waifuviewer.models.RemoteConnection.servicePics
import com.mackenzie.waifuviewer.models.Waifu
import com.mackenzie.waifuviewer.models.datasource.WaifusImLocalDataSource
import com.mackenzie.waifuviewer.models.datasource.WaifusPicLocalDataSource
import com.mackenzie.waifuviewer.models.datasource.WaifusRemoteDataSource
import com.mackenzie.waifuviewer.models.db.WaifuImItem
import com.mackenzie.waifuviewer.models.db.WaifuPicItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WaifusRepository(application: App) {

    private val localPicDataSource = WaifusPicLocalDataSource(application.db.waifuPicDao())
    private val localImDataSource = WaifusImLocalDataSource(application.db.waifuImDao())
    private val remoteDataSource = WaifusRemoteDataSource()

    val savedWaifusPic = localPicDataSource.waifusPic
    val savedWaifusIm = localImDataSource.waifusIm

    fun findPicsById(id: Int) = localPicDataSource.findPicById(id)
    fun findImById(id: Int) = localImDataSource.findImById(id)


    suspend fun requestWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: Boolean) {
        if(localImDataSource.isImEmpty()) {
            val waifusIm = remoteDataSource.getRandomWaifusIm(isNsfw, tag, isGif, getOrientation(orientation))
            localImDataSource.saveIm(waifusIm.waifus.toLocalModelIm())
        } else {
            Log.e("Waifus Repository", "LocalDataSource IM IS NOT EMPTY")
        }
    }

    suspend fun requestNewWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: Boolean) {
        val waifusIm = remoteDataSource.getRandomWaifusIm(isNsfw, tag, isGif, getOrientation(orientation))
        localImDataSource.saveIm(waifusIm.waifus.toLocalModelIm())
    }

    suspend fun requestWaifusPics(isNsfw: String, tag: String) {
        if(localPicDataSource.isPicsEmpty()) {
            val waifusPics = remoteDataSource.getRandomWaifusPics(isNsfw, tag)
            localPicDataSource.savePics(waifusPics.toLocalModelPics())
        } else {
            Log.e("Waifus Repository", "LocalDataSource PICS IS NOT EMPTY")
        }
    }

    suspend fun requestNewWaifusPics(isNsfw: String, tag: String) {
        val waifusPics = remoteDataSource.getRandomWaifusPics(isNsfw, tag)
        localPicDataSource.savePics(waifusPics.toLocalModelPics())
    }

    suspend fun requestOnlyWaifuPic() = servicePics.getOnlyWaifuPic()

    private fun getOrientation(ori: Boolean): String {
        if (ori) {
            return "LANDSCAPE"
        } else {
            return "PORTRAIT"
        }
    }
}

private fun List<String>.toLocalModelPics() : List<WaifuPicItem> = map { it.toLocalModelPics() }

private fun String.toLocalModelPics(): WaifuPicItem = WaifuPicItem(url = this)

private fun List<Waifu>.toLocalModelIm() : List<WaifuImItem>  = map { it.toLocalModelIm() }

private fun Waifu.toLocalModelIm(): WaifuImItem = WaifuImItem(
    file = file,
    extension = extension,
    imageId = imageId,
    isNsfw = isNsfw,
    previewUrl = previewUrl,
    source = source,
    uploadedAt = uploadedAt,
    dominant_color = dominant_color,
    url = url,
    width = width,
    height = height,
    favourites = favourites
)