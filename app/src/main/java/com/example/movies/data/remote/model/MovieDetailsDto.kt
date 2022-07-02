package com.example.movies.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("adult") val adult: Boolean,
    @SerialName("backdrop_path") val imageDetailPath: String,
    @SerialName("runtime") val runningTime: Int,
    @SerialName("vote_average") val rating: Double,
    @SerialName("vote_count")  val reviewCount: Int,
    @SerialName("overview") val storyLine: String,
    @SerialName("genres") val genres: List<GenreDto>,
    @SerialName(" release_date") val release_date: String,
    )


