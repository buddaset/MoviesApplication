package com.example.movies.models

import android.os.MessageQueue
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class MovieData(
    val id: Int,
    val title: String,
    val pgAge: Int,
    var imageUrl : String?,
    val rating: Int,
    val reviewCount : Int,
    val storyLine: String,
    val isLiked: Boolean,
    val genres: List<GenreData>,


)