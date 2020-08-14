package com.example.exercisedemo

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.*
import androidx.annotation.RequiresApi

class DownloadService : Service() {

    private var serviceHandler: ServiceHandler? = null
    private lateinit var notification: Notification
    private lateinit var notificationBuilder: Notification.Builder
    private var binder: IBinder = DownloadBinder()

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    var notifyUiCallback: NotifyUiCallback? = null

    override fun onCreate() {
        createNotification()
        // create a background thread, bcs service runs on main-thread by default
        HandlerThread(DEMO_SERVICE_THREAD_NAME, Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()
            var count = 0
            serviceHandler = ServiceHandler(
                looper,
                this@DownloadService,
                object : ServiceHandler.ProcessCallback {
                    override fun onDownloading(progress: Pair<Int, Int>) {
                        count++
                        if (count == progress.second / 3) {
                            count = 0
                            notifyUiCallback?.notifyProcess(progress)
                        }
                        updateNotification(progress)
                    }

                    override fun onFinishedDownloading() {
                        stopForeground(true)
                        stopSelf()
                    }

                })
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val listData = intent.getParcelableExtra<ListData>(EXTRA_IMAGE_LIST)
        val bundle = Bundle()
        bundle.putParcelable(EXTRA_IMAGE_LIST, listData)

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.data = bundle
            serviceHandler?.sendMessage(msg)
        }

        startForeground(FOREGROUND_SERVICE_CODE, notification)

        // do not create service if it is killed
        return START_NOT_STICKY
    }

    private fun createNotification() {
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }
        notificationBuilder =
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) Notification.Builder(this)
            else Notification.Builder(this, createNotificationChannel())

        notificationBuilder.setContentTitle(getText(R.string.uploading_images))
            .setSmallIcon(R.drawable.ic_download)
            .setContentIntent(pendingIntent)
            .setTicker(getText(R.string.ticker_text))
        notification = notificationBuilder.build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val chan = NotificationChannel(
            UPLOAD_NOTIFICATION_CHANNEL_ID,
            UPLOAD_NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return UPLOAD_NOTIFICATION_CHANNEL_ID
    }

    private fun updateNotification(progress: Pair<Int, Int>) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder.setContentText(
            getString(R.string.uploaded_in_sum, progress.first, progress.second)
        )
        notification = notificationBuilder.build()
        notificationManager.notify(FOREGROUND_SERVICE_CODE, notification)
    }

    class ServiceHandler(
        looper: Looper,
        private val context: Context,
        private val processCallback: ProcessCallback
    ) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            try {
                val listData = msg.data.getParcelable<ListData>(EXTRA_IMAGE_LIST)
                listData?.listImage?.forEachIndexed { index, image ->
                    saveImageToExternalStorage(image, context)
                    Thread.sleep(1000)
                    processCallback.onDownloading(Pair(index + 1, listData.listImage.size))
                }

            } catch (e: InterruptedException) {
                //Restore interrupt status
                Thread.currentThread().interrupt()
            }
            processCallback.onFinishedDownloading()
        }

        interface ProcessCallback {
            fun onDownloading(progress: Pair<Int, Int>)

            fun onFinishedDownloading()
        }
    }

    inner class DownloadBinder : Binder() {
        fun getService(): DownloadService = this@DownloadService
    }

    interface NotifyUiCallback {
        fun notifyProcess(process: Pair<Int, Int>)
    }

}
