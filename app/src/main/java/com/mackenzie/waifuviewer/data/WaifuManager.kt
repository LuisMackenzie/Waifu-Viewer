package com.mackenzie.waifuviewer.data

import android.util.Log
import com.google.gson.JsonObject
import com.mackenzie.waifuviewer.models.RemoteConnection.servicePics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class WaifuManager {

    suspend fun getWaifuPics(isNsfw: String, tag: String, onResult: (List<String>?) -> Unit) {
        // val servicePics = RemoteConnection.buildServicePics(WaifuService::class.java)
        // val jsonBody = JSONObject("""{"classification":"$isNsfw", "category":"$tag"}""")
        val jsonBody = JsonObject()

        jsonBody.addProperty("classification", isNsfw)
        jsonBody.addProperty("category", tag)
        // val parser: Parser = Parser()
        // val stringBuilder: StringBuilder = StringBuilder("{\"classification\":\"$isNsfw\", \"category\":$tag}")
        // val jsonBody: JsonObject = parser.parse(stringBuilder) as JsonObject
        // val waifuPicJson = WaifuPicJson(isNsfw, tag)

        Log.e("WaifuManager", "waifuPicJson: $jsonBody")
        try {
            val waifuPics = servicePics.getWaifuPics(isNsfw, tag, jsonBody).body()
            if (waifuPics != null) {
                onResult(waifuPics.images)
                Log.e("WaifuManager", "Habemus Waifus ${waifuPics.images}")
            } else {
                onResult(null)
                Log.e("WaifuManager", "NO Waifus!!")
            }
        }  catch (e: HttpException) {
            Log.e("WaifuManager", "Error: $e")
        }





            /*servicePics.getWaifuPics(isNsfw, tag, jsonBody).isSuccessful.let {
                if (it) {
                    val waifuPics = servicePics.getWaifuPics(isNsfw, tag, jsonBody).body()
                    onResult(waifuPics)
                    // onResult(waifuPics?.pics)
                } else {
                    onResult(null)
                }
            }*/

            /*servicePics.getWaifuPics(isNsfw, tag, jsonBody.asJsonObject).enqueue(
                object : Callback<List<String>> {
                    override fun onFailure(call: Call<List<String>>, t: Throwable) {
                        onResult(null)
                    }

                    override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                        if (response.isSuccessful) {
                            onResult(response.body())
                        } else {
                            onResult(null)
                        }
                        // val body = response.body()
                        // onResult(body)
                    }
                })*/




        // onResult(servicePics.getWaifuPics(isNsfw, tag).execute().body())




    }

}
