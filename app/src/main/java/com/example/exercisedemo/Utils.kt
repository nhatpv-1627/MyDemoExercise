package com.example.exercisedemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.URL

fun getImageBitmapFromUrl(url: String): Bitmap? {
    val inputStream = URL(url).openStream()
    return BitmapFactory.decodeStream(inputStream)
}
