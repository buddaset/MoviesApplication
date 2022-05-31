package com.example.movies.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movies.models.GenreData

@Entity(tableName = "movies")
data class MovieEntityDb(
    @PrimaryKey(autoGenerate = true)
    val dbId: Int,
    val id: Int,
    val title: String,
    val pgAge: Int,
    var imageUrl : String?,
    val rating: Int,
    val reviewCount : Int,
    val storyLine: String,
    val isLiked: Boolean,
    val genres: List<GenreData>
)
