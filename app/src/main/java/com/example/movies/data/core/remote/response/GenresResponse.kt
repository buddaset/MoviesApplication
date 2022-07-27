package com.example.movies.data.core.remote.response

import com.example.movies.data.movies.remote.model.GenreDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenresResponse(

    @SerialName("genres") val genres: List<GenreDto>
)