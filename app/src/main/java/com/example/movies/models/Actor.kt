package com.example.movies.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Actor(
    val name: String,
    var image: Int
) : Parcelable