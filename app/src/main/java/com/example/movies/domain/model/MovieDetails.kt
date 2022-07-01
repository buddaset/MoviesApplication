package com.example.movies.domain.model

data class MovieDetails(
    val id: Int,
    val title: String,
    val pgAge: Int,
    var detailImageUrl: String?,
    val runningTime: Int,
    val rating: Int,
    val reviewCount : Int,
    val storyLine: String,
    val isLiked: Boolean,
    val genres: List<Genre>,
    var actors: List<Actor> = emptyList()
)
