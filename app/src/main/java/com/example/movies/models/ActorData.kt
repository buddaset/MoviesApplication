package com.example.movies.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class ActorData(
    val id : Int,
    val name: String,
    val imageUrl: String?

)