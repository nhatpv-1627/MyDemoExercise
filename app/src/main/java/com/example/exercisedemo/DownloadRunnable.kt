package com.example.exercisedemo

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.core.os.bundleOf

class DownloadRunnable(
    private val imageData: ImageData,
    private val context: Context,
    private val onComplete: (path: String?) -> Unit,
    private val handler: Handler

) : Runnable {

    /**
     * other method of Message class to sendData
     * sendEmptyMessage(int)
     * sendMessageAtTime(Message, long)
     * sendMessageDelayed(Message, long)
     * */

    // There are two ways to communicate with main UI: use callback or Handle/Message/
    override fun run() {
        Log.d("ccccc", "task ${imageData.id} is running...")
        val message = Message()
        message.what = DOWNLOAD_IMAGE_SUCCESS_CODE
        Thread.sleep(2000)
        val path = saveImageToExternalStorage(imageData, context)
        Log.d("ccccc", "task ${imageData.id} is terminated")
        onComplete(path)
        path?.let {
            // we can use message.arg1/arg2 to send without using bundle.
            message.data = bundleOf(EXTRA_DOWNLOAD_SUCCESS to imageData.id)
            handler.sendMessage(message)
        } ?: handler.sendEmptyMessage(DOWNLOAD_IMAGE_FAIL_CODE)

    }
}
