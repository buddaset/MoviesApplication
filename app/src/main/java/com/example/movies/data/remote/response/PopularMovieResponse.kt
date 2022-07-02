package com.example.movies.data.remote.response

import com.example.movies.data.remote.model.MovieDto
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