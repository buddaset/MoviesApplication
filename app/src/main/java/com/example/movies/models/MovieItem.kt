package com.example.movies.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieItem(
    val name: String,
    val pg : String,
    val poster: Int,
    var isFavorite : Boolean = false,
    val ratingStars : Int,
    val countReviews : Int,
    val duration: Int,
    val overview: String,
    val actors : List<Actor>

) : Parcelable