package com.example.movies.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

import com.example.movies.domain.model.Actor

@Entity(tableName = "actors",
primaryKeys = ["movie_id", "actor_id"])
data class ActorEntityDb(
    @ColumnInfo(name = "movie_id")
    val movieId: Int,
    @ColumnInfo(name = "actor_id")
    val actorId: Int,
    val name: String,
    val imageUrl: String?

) {
    fun toActorData() : Actor =
        Actor(
            id = actorId,
            name = name,
            imageUrl = imageUrl
        )
}
