package com.example.movies.data.remote.response

import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("backdrop_path") val imageDatailPath: String,
    @SerializedName("runtime") val runningTime: Int,
    @SerializedName("vote_average") val rating: Double,
    @SerializedName("vote_count")  val reviewCount: Int,
    @SerializedName("overview") val storyLine: String,
    @SerializedName("genres") val genres: List<GenreResponse>,
    @SerializedName(" release_date") val release_date: String,

    )