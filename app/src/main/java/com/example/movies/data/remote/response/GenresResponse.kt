package com.example.movies.data.remote.response

import com.example.movies.data.remote.model.GenreDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenresResponse(

    @SerialName("genres") val genres: List<GenreDto>
)