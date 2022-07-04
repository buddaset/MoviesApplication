package com.example.movies.data.movies.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movies.data.movies.local.model.GenreEntityDb
import com.example.movies.domain.model.Genre

import com.example.movies.domain.model.Movie

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



fun MovieEntityDb.toDomain() : Movie =
    Movie(
        id = this.id.toLong(),
        title = this.title,
        pgAge = this.pgAge,
        imageUrl = this.imageUrl,
        rating = this.rating,
        reviewCount =this.reviewCount,
        storyLine = this.storyLine,
        isLiked = this.isLiked,
        genres = genres.map {
            Genre(id = it.id , name = it.name)
        }
    )



