package com.example.movies.models

import com.example.movies.data.local.entity.MovieEntityDb


data class MovieData(
    val id: Int,
    val title: String,
    val pgAge: Int,
    var imageUrl : String?,
    val rating: Int,
    val reviewCount : Int,
    val storyLine: String,
    val isLiked: Boolean,
    val genres: List<GenreData>
){

    fun fromMovieDataToMovieEntityDb() : MovieEntityDb =
        MovieEntityDb(
            dbId = 0,
            id = this.id,
            title = this.title,
            pgAge = this.pgAge,
            imageUrl = this.imageUrl,
            rating = this.rating,
            reviewCount =this.reviewCount,
            storyLine = this.storyLine,
            isLiked = this.isLiked,
            genres = this.genres
        )
}