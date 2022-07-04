package com.example.movies.domain.model


data class Movie(
    val id: Long,
    val title: String,
    val pgAge: Int,
    var imageUrl : String?,
    val rating: Int,
    val reviewCount : Int,
    val storyLine: String,
    val isLiked: Boolean,
    val genres: List<Genre>
)


