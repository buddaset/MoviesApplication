package com.example.movies.data.moviedetails.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.movies.domain.model.MovieDetails


data class MovieDetailsWithActorsEntity(
    @Embedded val movie: MovieDetailsEntityDb,
    @Relation(
        parentColumn = "id",
        entityColumn = "movie_id"
    )
    val actors: List<ActorEntityDb>

)

fun MovieDetailsWithActorsEntity.toDomain(): MovieDetails =
    MovieDetails(
        id = movie.id,
        title = movie.title,
        pgAge = movie.pgAge,
        detailImageUrl = movie.detailImageUrl,
        runningTime = movie.runningTime,
        rating = movie.rating,
        reviewCount = movie.reviewCount,
        storyLine = movie.storyLine,
        isLiked = movie.isLiked,
        genres = movie.genres.map { it.toDomain() },
        actors = actors.map { it.toDomain() }


    )