package com.example.movies.data.movies.local.model

import androidx.room.Entity

@Entity(
    tableName = "movies",
    primaryKeys = ["id", "title"]
)

data class MovieEntity(
    val id: Long,
    val title: String,
    val pgAge: Int,
    var imageUrl: String?,
    val rating: Int,
    val reviewCount: Int,
    val storyLine: String,
    val isPopular: Boolean = false,
    val genres: List<GenreEntity>
)








