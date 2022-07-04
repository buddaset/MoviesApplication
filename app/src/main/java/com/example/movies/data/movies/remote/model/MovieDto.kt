package com.example.movies.data.movies.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    @SerialName("adult")  val adult: Boolean,
    @SerialName("genre_ids") val genresId: List<Int> ,
    @SerialName("id") val id: Long,
    @SerialName("overview")  val storyLine: String,
    @SerialName("poster_path")  val imagePath: String,
    @SerialName("release_date")  val releaseDate: String,
    @SerialName("title") val title: String,
    @SerialName("vote_average") val rating: Double,
    @SerialName("vote_count") val reviewCount: Int,
)

