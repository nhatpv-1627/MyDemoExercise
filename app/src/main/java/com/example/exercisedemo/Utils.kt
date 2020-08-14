package com.example.exercisedemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

fun getImageBitmapFromUrl(url: String): Bitmap? {
    val inputStream = URL(url).openStream()
    return BitmapFactory.decodeStream(inputStream)
}

fun saveImageToExternalStorage(image: ImageData, context: Context): String? {
    val bitmap: Bitmap? = getImageBitmapFromUrl(image.url)
    val externalDir = File("${context.externalMediaDirs.firstOrNull()}/$STORE_IMAGE_FOLDER")
    if (!externalDir.exists()) externalDir.mkdir() // create this dir if it doesn't exist
    val newFile = File(externalDir, "image(${image.id}).png")
    if (newFile.exists()) newFile.delete()

    bitmap?.compress(Bitmap.CompressFormat.PNG, IMAGE_QUALITY_PERCENT, newFile.outputStream())

    return newFile.absolutePath
}

fun saveTextFileToCache(id: Int, textMessage: String, context: Context): String? {
    // create dir from Cache
    val cacheDir = File("${context.cacheDir.absolutePath}/$STORE_TEXT_FOLDER")
    if (!cacheDir.exists()) cacheDir.mkdir() // create this dir if it doesn't exist
    val newFile = File(cacheDir, "text(${id}).txt")
    if (newFile.exists()) newFile.delete()
    newFile.writeText(textMessage)
    return newFile.absolutePath
}

fun readTextFromFile(filePath: String?): String? {
    return if (filePath == null) null else File(filePath).readText()
}

fun getCurrentTime(): String {
    val calender = Calendar.getInstance()
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.US)
    return simpleDateFormat.format(calender.time)
}

const val STORE_IMAGE_FOLDER = "StoredImageFolder"
const val STORE_TEXT_FOLDER = "StoredTextFolder"
const val IMAGE_QUALITY_PERCENT = 100
const val REQUEST_PERMISSION_CODE = 96
const val DOWNLOAD_IMAGE_SUCCESS_CODE = 69
const val DOWNLOAD_IMAGE_FAIL_CODE = 70
const val CORE_THREAD_NUM = 3
const val MAX_THREAD_NUM = 5
const val THREAD_ALIVE_TIME = 10L
const val UPLOAD_NOTIFICATION_CHANNEL_ID = "UPLOAD_NOTIFICATION_CHANNEL_ID"
const val UPLOAD_NOTIFICATION_CHANNEL_NAME = "UPLOAD_NOTIFICATION_CHANNEL_NAME"
const val FOREGROUND_SERVICE_CODE = 12
const val DEMO_SERVICE_THREAD_NAME = "DEMO_SERVICE_THREAD_NAME"

// extra
const val EXTRA_DOWNLOAD_SUCCESS = "EXTRA_DOWNLOAD_SUCCESS"
const val EXTRA_IMAGE_LIST = "EXTRA_IMAGE_LIST"
