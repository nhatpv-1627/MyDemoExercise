package com.example.exercisedemo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageData(
    val id: Int,
    val url: String
) : Parcelable
