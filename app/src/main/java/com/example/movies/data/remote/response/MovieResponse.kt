package com.example.movies.data.remote.response

data class MovieResponse(
    val id: Int,
    val title: String,
    val adult: Boolean,
    val poster_path: String,
    val vote_average: Double,
    val vote_count: Int,
    val overview: String,
    val genre_ids: List<Int>,
    val release_date: String,
)