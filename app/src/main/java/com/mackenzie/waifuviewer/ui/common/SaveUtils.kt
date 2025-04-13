package com.mackenzie.waifuviewer.ui.common

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.mackenzie.waifuviewer.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import androidx.core.net.toUri
import com.mackenzie.waifuviewer.BuildConfig

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


    fun downloadAndInstallUpdate(context: Context, latestVer: String): Boolean {
        val flavorLink = latestVer.getFlavorLink(context)
        val updateNameFile = "waifu-${BuildConfig.BUILD_TYPE}-${latestVer}-${BuildConfig.VERSION_CODE}.apk"

        // Verificar si el permiso de almacenamiento está concedido
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && !context.hasWriteExternalStoragePermission()) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                100
            )
            return false
        } else {
            val request = DownloadManager.Request(flavorLink.toUri())
                .setTitle(context.getString(R.string.app_name))
                .setDescription(context.getString(R.string.dialog_update_accept))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    updateNameFile
                )
                .setMimeType("application/vnd.android.package-archive")

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadId = downloadManager.enqueue(request)

            // Registrar un receptor para detectar cuando finaliza la descarga
            val onComplete = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    if (id == downloadId) {
                        // Iniciar la instalación
                        val downloadedUri = downloadManager.getUriForDownloadedFile(downloadId)
                        downloadedUri?.let {
                            installApk(context, it)
                        }
                        /*val query = DownloadManager.Query().setFilterById(downloadId)
                        val cursor = downloadManager.query(query)

                        if (cursor.moveToFirst()) {
                            val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                            val status = cursor.getInt(statusIndex)

                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                val localUriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                                val localUri = cursor.getString(localUriIndex).toUri()
                                val apkFile = File(localUri.path?.replace("file://", "") ?: "")

                                installApk(context, apkFile)
                            }
                        }
                        cursor.close()*/
                        context.unregisterReceiver(this)
                    }
                }
            }

            ContextCompat.registerReceiver(
                context,
                onComplete,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                ContextCompat.RECEIVER_EXPORTED
            )
            return true
        }
    }

    private fun String.getFlavorLink(context: Context): String {
        val baseLink = context.getString(R.string.dialog_update_download_base_link)
        val debugFile = context.getString(R.string.dialog_update_download_debug_file_name)
        val enhancedFile = context.getString(R.string.dialog_update_download_enhanced_file_name)
        val releaseFile = context.getString(R.string.dialog_update_download_release_file_name)
        return when (BuildConfig.VERSION_NAME.substringAfterLast("-")) {
            "DEBUG" -> { baseLink + this + debugFile }
            "PRIME" -> { baseLink + this + enhancedFile }
            else -> { baseLink + this + releaseFile }
        }
    }

    fun installApk(context: Context, apkUri: Uri) {
        /*val apkUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                apkFile
            )
        } else {
            Uri.fromFile(apkFile)
        }*/
        Log.e( "TAG", "APK URI: $apkUri")

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(apkUri, "application/vnd.android.package-archive")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(intent)
    }
}