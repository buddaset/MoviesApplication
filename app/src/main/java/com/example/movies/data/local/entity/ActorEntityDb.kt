package com.example.movies.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movies.models.ActorData

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
    fun toActorData() : ActorData =
        ActorData(
            id = actorId,
            name = name,
            imageUrl = imageUrl
        )
}
