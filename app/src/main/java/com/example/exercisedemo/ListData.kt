package com.example.exercisedemo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListData(val listImage: List<ImageData>) : Parcelable