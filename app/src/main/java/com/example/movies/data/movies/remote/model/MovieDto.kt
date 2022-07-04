package com.example.movies.data.movies.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("adult")  val adult: Boolean,
    @SerialName("poster_path")  val imagePath: String,
    @SerialName("vote_average") val rating: Double,
    @SerialName("vote_count") val reviewCount: Int,
    @SerialName("overview")  val storyLine: String,
    @SerialName("genre_ids") val genresId: List<Int>,
    @SerialName("release_date")  val releaseDate: String,
)

