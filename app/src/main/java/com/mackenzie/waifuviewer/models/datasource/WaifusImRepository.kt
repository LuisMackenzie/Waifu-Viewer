package com.mackenzie.waifuviewer.models.datasource

import android.util.Log
import com.mackenzie.waifuviewer.App
import com.mackenzie.waifuviewer.models.Error
import com.mackenzie.waifuviewer.models.Waifu
import com.mackenzie.waifuviewer.models.db.WaifuImItem
import com.mackenzie.waifuviewer.models.tryCall

class WaifusImRepository(application: App) {

    private val localImDataSource = WaifusImLocalDataSource(application.db.waifuImDao())
    private val remoteImDataSource = WaifusImRemoteDataSource()
    val savedWaifusIm = localImDataSource.waifusIm

    fun findImById(id: Int) = localImDataSource.findImById(id)

    suspend fun requestWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: Boolean): Error? = tryCall {
        if(localImDataSource.isImEmpty()) {
            val waifusIm = remoteImDataSource.getRandomWaifusIm(isNsfw, tag, isGif, getOrientation(orientation))
            localImDataSource.saveIm(waifusIm.waifus.toLocalModelIm())
        } else {
            Log.e("Waifus Repository", "LocalDataSource IM IS NOT EMPTY")
        }
    }

    suspend fun requestNewWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: Boolean): Error? = tryCall {
        val waifusIm = remoteImDataSource.getRandomWaifusIm(isNsfw, tag, isGif, getOrientation(orientation))
        localImDataSource.saveIm(waifusIm.waifus.toLocalModelIm())
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

private fun List<Waifu>.toLocalModelIm() : List<WaifuImItem>  = map { it.toLocalModelIm() }

private fun Waifu.toLocalModelIm(): WaifuImItem = WaifuImItem(
    file = file,
    imageId = imageId,
    isNsfw = isNsfw,
    dominant_color = dominant_color,
    url = url,
    width = width,
    height = height,
    isFavorite = false
)