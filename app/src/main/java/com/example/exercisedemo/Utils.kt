package com.example.exercisedemo

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.util.*

fun saveImageToLocal(url: String, context: Context): String? {
    var inputStream: InputStream? = null
    var fileOutputStream: FileOutputStream? = null
    val outputFile: File?
    val directory = ContextWrapper(context).getDir(STORE_IMAGE_FOLDER, Context.MODE_PRIVATE)
    try {
        inputStream = URL(url).openStream()
        val bitMapImage = BitmapFactory.decodeStream(inputStream)

        outputFile = File(directory, "${UUID.randomUUID()}")
        fileOutputStream = FileOutputStream(outputFile.absoluteFile)
        bitMapImage.compress(Bitmap.CompressFormat.PNG, IMAGE_QUALITY_PERCENT, fileOutputStream)
    } catch (e: Exception) {
        throw e
    } finally {
        inputStream?.close()
        fileOutputStream?.close()
    }

    return outputFile?.absolutePath
}

fun getBitmapImageFromPath(imagePath: String): Bitmap? {
    val file = File(imagePath)
    if (file.exists()) {
        return BitmapFactory.decodeFile(file.absolutePath)
    }
    return null
}

fun releaseStorage(context: Context) {
    val folder = ContextWrapper(context).getDir(STORE_IMAGE_FOLDER, Context.MODE_PRIVATE)
    if (folder.isDirectory && folder.listFiles() != null)
        for (file in folder.listFiles()!!) {
            file.delete()
        }
}

const val STORE_IMAGE_FOLDER = "StoredImageFolder"
const val IMAGE_QUALITY_PERCENT = 100
