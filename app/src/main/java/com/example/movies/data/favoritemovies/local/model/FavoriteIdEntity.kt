package com.example.movies.data.favoritemovies.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorites")
data class FavoriteIdEntity(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val movieId: Long
)
