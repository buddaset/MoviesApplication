package com.example.movies.data.core.remote.response

import com.example.movies.data.movies.remote.model.MovieDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesResponse(
    val page: Int,
    @SerialName("result")
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)