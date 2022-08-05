package com.example.movies.data.moviedetails.local.model

import androidx.room.Embedded
import androidx.room.Relation


data class MovieDetailsWithActorsEntity(
    @Embedded
    val movie: MovieDetailsEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "movie_id"
    )
    val actors: List<ActorEntity>
)

