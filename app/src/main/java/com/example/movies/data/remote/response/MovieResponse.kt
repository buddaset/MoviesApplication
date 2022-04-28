package com.example.movies.data.remote.response

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("adult")  val adult: Boolean,
    @SerializedName("poster_path")  val imagePath: String,
    @SerializedName("vote_average") val rating: Double,
    @SerializedName("vote_count") val reviewCount: Int,
    @SerializedName("overview")  val storyLine: String,
    @SerializedName("genre_ids") val genresId: List<Int>,
    @SerializedName("release_date")  val releaseDate: String,
)