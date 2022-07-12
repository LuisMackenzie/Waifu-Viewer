package com.mackenzie.waifuviewer.data

import android.app.Activity
import com.mackenzie.waifuviewer.models.RemoteConnection

class WaifusRepository(activity: Activity) {

    // private val apiKey = activity.getString(R.string.api_key)
    // private val regionRepository = RegionRepository(activity)

    suspend fun getRandomWaifus() = RemoteConnection.serviceIm.getRandomWaifu()

    suspend fun getRandomWaifusGif() = RemoteConnection.serviceIm.getRandomWaifu(isGif = true)

    suspend fun getWaifuOnly() = RemoteConnection.serviceIm.getRandomWaifu(manyWaifus = false)

    suspend fun getWaifuOnlyNsfw() = RemoteConnection.serviceIm.getRandomWaifu(isNsfw = true, manyWaifus = false)

    suspend fun getCustomRandomWaifus(isNsfw: Boolean,
                                isGif: Boolean,
                                orientation: String) = RemoteConnection.serviceIm.getRandomWaifu(isNsfw, isGif, orientation)

    suspend fun getCustomTagWaifus(isNsfw: Boolean,
                                      isGif: Boolean, tag: String,
                                      orientation: String) = RemoteConnection.serviceIm.getRandomWaifu(isNsfw, tag, isGif, orientation)

    suspend fun getWaifuInfo(waifuId: String) = RemoteConnection.serviceIm.getWaifu(waifuId)

    suspend fun getEspecialWaifu(isNsfw: Boolean,
                                 isGif: Boolean,
                                 tag: String) = RemoteConnection.serviceIm.getEspecialWaifu(isNsfw, tag, isGif)


    suspend fun getCategories(categories: String) = RemoteConnection.serviceIm.getCategories()

    /*suspend fun getWaifuPics(isNsfw: String, tag: String) {
        val apiCall: Call<WaifuPicsResult> = RemoteConnection.servicePics.getWaifuPics(type = isNsfw, category = tag)
        // apiCall.enqueue(this)

    }*/

    suspend fun getOnlyWaifuPic() = RemoteConnection.servicePics.getOnlyWaifuPic()

}