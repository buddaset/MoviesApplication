package com.example.movies.data.moviedetails.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

import com.example.movies.domain.model.Actor

@Entity(
    tableName = "actors",
    primaryKeys = ["movie_id", "actor_id"],
)
data class ActorEntity(
    @ColumnInfo(name = "movie_id") val movieId: Long,
    @ColumnInfo(name = "actor_id") val actorId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image_url") val imageUrl: String?

)
