package com.example.movies.models

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
    val genres: List<GenreData>,
    var actors: List<ActorData> = emptyList()
)
