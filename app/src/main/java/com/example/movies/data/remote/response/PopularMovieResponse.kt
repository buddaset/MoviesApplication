package com.example.movies.data.remote.response

data class PopularMoviesResponse(
    val page: Int,
    val results: List<MovieResponse>,
    val total_pages: Int,
    val total_results: Int
)