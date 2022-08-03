package com.example.movies.data.movies.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movies.data.movies.local.model.GenreEntityDb
import com.example.movies.domain.model.Genre

import com.example.movies.domain.model.Movie

@Entity(
    tableName = "movies",
    primaryKeys = ["id", "title"]
)

data class MovieEntityDb(
    val id: Long,
    val title: String,
    val pgAge: Int,
    var imageUrl: String?,
    val rating: Int,
    val reviewCount: Int,
    val storyLine: String,
    val genres: List<GenreEntityDb>
)


fun MovieEntityDb.toDomain(): Movie =
    Movie(
        id = id,
        title =title,
        pgAge = pgAge,
        imageUrl = imageUrl,
        rating = rating,
        reviewCount = reviewCount,
        storyLine = storyLine,

        genres = genres.map {
            Genre(id = it.id, name = it.name)
        }
    )

fun Movie.toEntity(): MovieEntityDb =
    MovieEntityDb(
        id = id,
        title =title,
        pgAge = pgAge,
        imageUrl = imageUrl,
        rating = rating,
        reviewCount = reviewCount,
        storyLine = storyLine,
        genres = genres.map {
            GenreEntityDb(id = it.id, name = it.name)
        }
    )




