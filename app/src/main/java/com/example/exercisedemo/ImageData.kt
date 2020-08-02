package com.example.exercisedemo

import android.graphics.Bitmap

data class ImageData(
    val id:Int,
    val url: String,
    var bitmap: Bitmap? = null
)
