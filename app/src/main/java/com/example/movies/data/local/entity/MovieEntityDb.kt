package com.example.movies.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movies.models.GenreData

import com.example.movies.models.MovieData

@Entity(tableName = "movies")
data class MovieEntityDb(
    @PrimaryKey
    val id: String,
    val title: String,
    val pgAge: Int,
    var imageUrl : String?,
    val rating: Int,
    val reviewCount : Int,
    val storyLine: String,
    val isLiked: Boolean,
    val genres: List<GenreEntityDb>
)



fun MovieEntityDb.toMovieData() : MovieData =
    MovieData(
        id = this.id.toInt(),
        title = this.title,
        pgAge = this.pgAge,
        imageUrl = this.imageUrl,
        rating = this.rating,
        reviewCount =this.reviewCount,
        storyLine = this.storyLine,
        isLiked = this.isLiked,
        genres = genres.map {
            GenreData(id = it.id , name = it.name)
        }
    )



