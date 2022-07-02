package com.example.movies.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "movies_detail")
data class MovieDetailsEntityDb(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val pgAge: Int,
    val detailImageUrl: String?,
    val runningTime: Int,
    val rating: Int,
    val reviewCount: Int,
    val storyLine: String,
    val isLiked : Boolean = false,
    val genres: List<GenreEntityDb>,
)
