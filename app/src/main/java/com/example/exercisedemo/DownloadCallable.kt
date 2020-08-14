package com.example.exercisedemo

import android.content.Context
import android.util.Log
import java.util.concurrent.Callable

class DownloadCallable(
    private val imageData: ImageData,
    private val context: Context
) : Callable<Int> {
    override fun call(): Int {
        Log.d("ccccc", "thread ${imageData.id} is running...")
        saveImageToExternalStorage(imageData, context)
        Thread.sleep(2000)
        Log.d("ccccc", "thread ${imageData.id} is terminated")
        return imageData.id
    }

}
