package com.example.movies.data.core.remote.response

import com.example.movies.data.movies.remote.model.MovieDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesResponse(

    @SerialName("results")
    val results: List<MovieDto>,

)