package com.mackenzie.waifuviewer.ui.common

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class SaveImage {

    @Throws(IOException::class)
    suspend fun saveImageToStorage(
        context: Context,
        imageTitle: String,
        mimeType: String,
        url: String
    ) {
        val imageOutStream: OutputStream?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val directory = Environment.DIRECTORY_PICTURES + File.separator + "Waifus"

            val values = ContentValues()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, imageTitle)
            values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            values.put(MediaStore.Images.Media.RELATIVE_PATH, directory)
            val uri : Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            imageOutStream = context.contentResolver.openOutputStream(uri!!)
        } else {
            val directory =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + File.separator + "Waifus")

            if (!directory.exists())
                directory.mkdirs()

            val image = File(directory, mimeType.decodeMimeTypeForTitle(imageTitle))
            imageOutStream = withContext(IO) {
                FileOutputStream(image)
            }
        }

        try {
            if (mimeType == "image/png") {
                val image = retrieveImageFromUrl(url)
                withContext(IO) {
                    imageOutStream?.write(image)
                }
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val image = retrieveImageFromUrl(url)
                    withContext(IO) {
                        imageOutStream?.write(image)
                    }
                } else {
                    withContext(IO) {
                        val bitmapObject: Bitmap = BitmapFactory.decodeStream(urlLink.openConnection().getInputStream())
                        bitmapObject.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream!!)
                    }
                }*/
            } else if (mimeType == "image/gif") {
                val gifImage = retrieveGifFromUrl(url)
                withContext(IO) {
                    imageOutStream?.write(gifImage)
                }
            } else {
                val image = retrieveImageFromUrl(url)
                withContext(IO) {
                    imageOutStream?.write(image)
                }
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val image = retrieveImageFromUrl(url)
                    withContext(IO) {
                        imageOutStream?.write(image)
                    }
                } else {
                    withContext(IO) {
                        val bitmapObject: Bitmap = BitmapFactory.decodeStream(urlLink.openConnection().getInputStream())
                        bitmapObject.compress(Bitmap.CompressFormat.JPEG, 100, imageOutStream!!)
                    }
                }*/
            }


        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            withContext(IO) {
                imageOutStream?.close()
            }
            withContext(Main) {
                Toast.makeText(context, "Imagen Guardada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend private fun retrieveGifFromUrl(url: String):ByteArray {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val deferred =  CompletableDeferred<ByteArray>()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { body ->
                    val bytes = body.bytes()
                    deferred.complete(bytes)
                }
            }
        })
        return deferred.await()
    }

    suspend fun retrieveImageFromUrl(url: String):ByteArray {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val deferred =  CompletableDeferred<ByteArray>()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { body ->
                    val bytes = body.bytes()
                    deferred.complete(bytes)
                }
            }
        })
        return deferred.await()
    }
}