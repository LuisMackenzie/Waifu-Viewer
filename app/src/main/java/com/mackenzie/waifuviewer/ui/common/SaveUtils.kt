package com.mackenzie.waifuviewer.ui.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class SaveUtils {

    suspend fun downloadAndSaveImage(
        context: Context,
        imageUrl: String,
        // folderName: String,
        fileName: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Descargar la imagen como InputStream
                val url = URL(imageUrl)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.connect()
                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    connection.disconnect()
                    return@withContext false
                }

                // 2. Convertir InputStream a Bitmap
                val inputStream: InputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                connection.disconnect()

                // 3. Guardar el Bitmap en la carpeta especificada
                val directory = Environment.DIRECTORY_PICTURES + File.separator + "Waifus"
                saveBitmapToFolder(context, bitmap, directory, fileName)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    private fun saveBitmapToFolder(
        context: Context,
        bitmap: Bitmap,
        folderName: String,
        fileName: String
    ): Boolean {
        return try {
            // 1. Obtener el directorio de destino. Podría ser:
            //    - context.getExternalFilesDir(null) -> /storage/emulated/0/Android/data/<app>/files
            //    - Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            //    - O un directorio interno con context.filesDir
            val directory = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                folderName
            )
            if (!directory.exists()) {
                directory.mkdirs()
            }

            // 2. Crear el archivo de destino
            val file = File(directory, fileName)
            FileOutputStream(file).use { fos ->
                // Ajusta el formato y la compresión como desees (ej: PNG, JPEG, etc.)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}