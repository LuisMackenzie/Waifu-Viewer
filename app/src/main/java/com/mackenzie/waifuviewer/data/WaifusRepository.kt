package com.mackenzie.waifuviewer.data

import android.app.Activity
import android.app.Application
import androidx.lifecycle.Transformations.map
import com.google.gson.JsonObject
import com.mackenzie.waifuviewer.App
import com.mackenzie.waifuviewer.WaifuViewModel
import com.mackenzie.waifuviewer.models.*
import com.mackenzie.waifuviewer.models.RemoteConnection.serviceIm
import com.mackenzie.waifuviewer.models.RemoteConnection.servicePics
import com.mackenzie.waifuviewer.models.db.WaifuImDao
import com.mackenzie.waifuviewer.models.db.WaifuImItem
import com.mackenzie.waifuviewer.models.db.WaifuPicDao
import com.mackenzie.waifuviewer.models.db.WaifuPicItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class WaifusRepository(application: App) {

    private val localDataSource = WaifusLocalDataSource(application.db.waifuPicDao(), application.db.waifuImDao())
    private val remoteDataSource = WaifusRemoteDataSource()

    val savedWaifusPic = localDataSource.waifusPic
    val savedWaifusIm = localDataSource.waifusIm

    suspend fun requestWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: Boolean): WaifuResult = withContext(Dispatchers.IO) {
        var waifusIm: WaifuResult = WaifuResult(emptyList())
        if(localDataSource.isImEmpty()) {
            val waifus = remoteDataSource.getRandomWaifusIm(isNsfw, tag, isGif, getOrientation(orientation))
            waifusIm = waifus
            localDataSource.saveIm(waifus.waifus.toLocalModelIm())
            return@withContext waifus
        } else {
            waifusIm =  remoteDataSource.getRandomWaifusIm(isNsfw, tag, isGif, getOrientation(orientation))
            return@withContext waifusIm
        }
    }

    suspend fun requestWaifusPics(isNsfw: String, tag: String) {
        if(localDataSource.isPicsEmpty()) {
            val waifus : List<String> = remoteDataSource.getRandomWaifusPics(isNsfw, tag)
            localDataSource.savePics(waifus.toLocalModelPics())
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

    // private val apiKey = activity.getString(R.string.api_key)
    // private val regionRepository = RegionRepository(activity)



    // suspend fun getRandomWaifusGif() = serviceIm.getRandomWaifu(isGif = true)

    // suspend fun getWaifuOnly() = serviceIm.getRandomWaifu(manyWaifus = false)

    // suspend fun getWaifuOnlyNsfw() = serviceIm.getRandomWaifu(isNsfw = true, manyWaifus = false)

    /*suspend fun getCustomRandomWaifus(isNsfw: Boolean,
                                isGif: Boolean,
                                orientation: Boolean) = serviceIm.getRandomWaifu(isNsfw, isGif, orientation)*/

    /*suspend fun getCustomTagWaifus(isNsfw: Boolean,
                                      isGif: Boolean, tag: String,
                                      orientation: String) = serviceIm.getRandomWaifu(isNsfw, tag, isGif, orientation)*/

    // suspend fun getWaifuInfo(waifuId: String) = serviceIm.getWaifu(waifuId)

    /*suspend fun getEspecialWaifu(isNsfw: Boolean,
                                 isGif: Boolean,
                                 tag: String) = serviceIm.getEspecialWaifu(isNsfw, tag, isGif)*/


    // suspend fun getCategories(categories: String) = serviceIm.getCategories()

    /*suspend fun getWaifuPics(isNsfw: String, tag: String) {
        val apiCall: Call<WaifuPicsResult> = RemoteConnection.servicePics.getWaifuPics(type = isNsfw, category = tag)
        // apiCall.enqueue(this)

    }*/


}

class WaifusLocalDataSource(private val PicDao: WaifuPicDao, private val ImDao: WaifuImDao) {

    val waifusPic: Flow<List<WaifuPicItem>> = PicDao.getAllPic()
    val waifusIm: Flow<List<WaifuImItem>> = ImDao.getAllIm()
    fun isPicsEmpty() = PicDao.waifuCount() == 0
    fun isImEmpty() = ImDao.waifuCount() == 0
    fun savePics(waifus: List<WaifuPicItem>) {
        PicDao.insertAllWaifus(waifus)
    }
    fun saveIm(waifus: List<WaifuImItem>) {
        ImDao.insertAllWaifus(waifus)
    }
}

class WaifusRemoteDataSource() {

    suspend fun getRandomWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: String) = serviceIm.getRandomWaifu(isNsfw, tag, isGif,  orientation)

    suspend fun getRandomWaifusPics(isNsfw: String, tag: String) =
        servicePics.getWaifuPics(type = isNsfw, category = tag, body = getJson(isNsfw, tag)).body()!!.images


    private fun getJson(isNsfw: String, tag: String): JsonObject {
        val jsonBody = JsonObject()
        jsonBody.addProperty("classification", isNsfw)
        jsonBody.addProperty("category", tag)
        return jsonBody
    }

    /*suspend fun getRandomWaifusPics(isNsfw: String, tag: String) = WaifuManager().getWaifuPics(isNsfw = isNsfw, tag = tag) { waifuPicsResult ->
        if (waifuPicsResult != null){

        }
    }*/

}

private fun List<String>.toLocalModelPics() : List<WaifuPicItem> = map { it.toLocalModelPics() }

private fun String.toLocalModelPics(): WaifuPicItem = WaifuPicItem(id = 0,url = this)

private fun List<Waifu>.toLocalModelIm() : List<WaifuImItem>  = map { it.toLocalModelIm() }

private fun Waifu.toLocalModelIm(): WaifuImItem = WaifuImItem(
    id = imageId ?: 0,
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