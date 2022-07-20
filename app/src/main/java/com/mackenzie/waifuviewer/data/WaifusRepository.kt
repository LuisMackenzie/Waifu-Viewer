package com.mackenzie.waifuviewer.data

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.mackenzie.waifuviewer.App
import com.mackenzie.waifuviewer.models.RemoteConnection.servicePics
import com.mackenzie.waifuviewer.models.Waifu
import com.mackenzie.waifuviewer.models.datasource.WaifusLocalDataSource
import com.mackenzie.waifuviewer.models.datasource.WaifusRemoteDataSource
import com.mackenzie.waifuviewer.models.db.WaifuImItem
import com.mackenzie.waifuviewer.models.db.WaifuPicItem
import com.mackenzie.waifuviewer.ui.main.WaifuFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WaifusRepository(application: App) {

    private val localDataSource = WaifusLocalDataSource(application.db.waifuPicDao(), application.db.waifuImDao())
    private val remoteDataSource = WaifusRemoteDataSource()

    val savedWaifusPic = localDataSource.waifusPic
    val savedWaifusIm = localDataSource.waifusIm

    fun findImById(id: Int) = localDataSource.findImById(id)
    fun findPicsById(id: Int) = localDataSource.findPicById(id)

    suspend fun requestWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: Boolean) = withContext(Dispatchers.IO) {
        if(localDataSource.isImEmpty()) {
            val waifus = remoteDataSource.getRandomWaifusIm(isNsfw, tag, isGif, getOrientation(orientation))
            localDataSource.saveIm(waifus.waifus.toLocalModelIm())
        } else {
            Log.e("Waifus Repository", "LocalDataSource IM IS NOT EMPTY")
        }
    }

    suspend fun requestWaifusPics(isNsfw: String, tag: String) = withContext(Dispatchers.IO) {
        if(localDataSource.isPicsEmpty()) {
            val waifusPics = remoteDataSource.getRandomWaifusPics(isNsfw, tag)
            localDataSource.savePics(waifusPics.toLocalModelPics())
        } else {
            Log.e("Waifus Repository", "LocalDataSource PICS IS NOT EMPTY")
        }
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
    favourites = favourites ?: 0
)