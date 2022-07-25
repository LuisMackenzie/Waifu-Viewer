package com.mackenzie.waifuviewer.ui.common

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext
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
        mimeType: String
    ) {
        val imageOutStream: OutputStream?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val directory = Environment.DIRECTORY_PICTURES + File.separator + "Waifus"

            val values = ContentValues()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, imageTitle)
            values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            values.put(MediaStore.Images.Media.RELATIVE_PATH, directory)
            val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            imageOutStream = context.contentResolver.openOutputStream(uri!!)

        } else {
            val directory =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + File.separator + "Waifus")

            if (!directory.exists())
                directory.mkdirs()

            val image = File(directory, imageTitle)
            imageOutStream = FileOutputStream(image)

            /*val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                setDataAndType(Uri.fromFile(directory), mimeType)
                // putExtra(Intent.EXTRA_TITLE, "Waifu.jpg")
                putExtra("mimeType", mimeType)
                // putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
            }*/
            // context.startActivity(Intent.createChooser(intent, "Set as:"))
        }

        try {
            if (mimeType == "image/png") {
                bitmapObject.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream)
            } else if (mimeType == "image/gif") {
                // var imageGif: Bitmap = bitmapObject.copy(Bitmap.Config.ARGB_8888, true)
                // imageGif = Glide.with(context).asGif().load(dirImagen).into(image)
                bitmapObject.compress(Bitmap.CompressFormat.JPEG, 100, imageOutStream)
            } else {
                bitmapObject.compress(Bitmap.CompressFormat.JPEG, 100, imageOutStream)
            }


        } finally {
            // imageOutStream?.flush()
            imageOutStream?.close()
            withContext(Main) {
                Toast.makeText(context, "Imagen Guardada", Toast.LENGTH_SHORT).show()
            }
        }

        /*private boolean isGif(String imagen) {
            String extension = "";
            int i = imagen.lastIndexOf('.');
            int p = Math.max(imagen.lastIndexOf('/'), imagen.lastIndexOf('\\'));
            if (i > p) {
                extension = imagen.substring(i+1);
            }
            return extension.trim().equalsIgnoreCase("gif");
        }*/
    }
}