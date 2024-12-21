package com.mackenzie.waifuviewer.ui.common

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.net.toFile
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
        bitmapObject: Bitmap,
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
            val uri2: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.normalizeScheme()


            Log.e("SaveImage", "URI: $uri, uri2: $uri2")
            Log.e("SaveImage", "values: $values")
            imageOutStream = context.contentResolver.openOutputStream(uri!!)

            // imageOutStream = context.contentResolver.openOutputStream(uri2)

        } else {
            // Este codigo no funciona
            val directory =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + File.separator + "Waifus")

            if (!directory.exists())
                directory.mkdirs()

            val image = File(directory, imageTitle)
            imageOutStream = withContext(IO) {
                FileOutputStream(image)
            }
        }

        try {
            if (mimeType == "image/png") {
                // bitmapObject.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream!!)
                val image = retrieveImageFromUrl(url)
                withContext(IO) {
                    imageOutStream?.write(image)
                }
            } else if (mimeType == "image/gif") {

                val gifImage = retrieveGifFromUrl(url)
                withContext(IO) {
                    imageOutStream?.write(gifImage)
                }
            } else {
                // bitmapObject.compress(Bitmap.CompressFormat.JPEG, 100, imageOutStream!!)
                val image = retrieveImageFromUrl(url)
                withContext(IO) {
                    imageOutStream?.write(image)
                }
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

    @Throws(IOException::class)
    suspend fun saveImageToStorage2(
        bitmapObject: Bitmap,
        imageTitle: String,
        mimeType: String,
        url: String
    ) {
        var imageOutStream: OutputStream?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val directory = Environment.DIRECTORY_PICTURES + File.separator + "Waifus"

            val values = ContentValues()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, imageTitle)
            values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            values.put(MediaStore.Images.Media.RELATIVE_PATH, directory)
            // val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            val uri = Uri.Builder()
                .scheme(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.scheme)
                .path(values.getAsString(MediaStore.Images.Media.RELATIVE_PATH))
                // .appendPath(imageTitle)
                // .appendPath(imageTitle)
                .build()
            // scheme = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.scheme,
            // authority = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.authority,
            // path = directory + File.separator + imageTitle,
            // query = values.toString()


            // val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Waifus/$imageTitle")
            // val file = File(directory, imageTitle)
            Log.e("SaveImage", "URI: $uri, values: $values")
            // Log.e("SaveImage", "URI2: $uri2")
            uri?.let {
                imageOutStream = withContext(IO) {
                    FileOutputStream(uri.toFile())
                }
            } ?: run {
                imageOutStream = null
            }
            // imageOutStream = context.contentResolver.openOutputStream(uri)
            /*imageOutStream = withContext(IO) {
                FileOutputStream(uri?.toFile())
            }*/

        } else {
            val directory =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + File.separator + "Waifus")

            if (!directory.exists())
                directory.mkdirs()

            val image = File(directory, imageTitle)
            imageOutStream = withContext(IO) {
                FileOutputStream(image)
            }
        }

        try {
            if (mimeType == "image/png") {
                // bitmapObject.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream!!)
                val image = retrieveImageFromUrl(url)
                withContext(IO) {
                    imageOutStream?.write(image)
                }
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
                // bitmapObject.compress(Bitmap.CompressFormat.JPEG, 100, imageOutStream!!)
            }


        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            withContext(IO) {
                imageOutStream?.close()
                Log.e("SaveImage", "Imagen Guardada")

                // Toast.makeText(context, "Imagen Guardada", Toast.LENGTH_SHORT).show()
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

    suspend fun retrieveImageFromUrl2(url: String):Bit {
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