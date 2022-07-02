package com.example.movies.core.utils


import com.example.movies.data.local.entity.ActorEntityDb
import com.example.movies.data.local.entity.GenreEntityDb
import com.example.movies.data.local.entity.MovieDetailsEntityDb
import com.example.movies.data.local.entity.MovieEntityDb
import com.example.movies.data.remote.model.ActorDto
import com.example.movies.data.remote.model.MovieDetailsDto
import com.example.movies.data.remote.model.MovieDto
import com.example.movies.domain.model.Actor
import com.example.movies.domain.model.Genre
import com.example.movies.domain.model.MovieDetails

const val PG_ADULT = 16
const val PG_CHILDREN = 13

fun MovieDto.toMovieEntityDb(genres: List<GenreEntityDb>) : MovieEntityDb =
    MovieEntityDb(
        id = id,
        title = title,
        pgAge = setPgAge(adult),
        imageUrl = imagePath,
        rating = rating.toInt(),
        reviewCount = reviewCount,
        storyLine = storyLine,
        isLiked = false,
        genres = genres.filter { it.id in genresId }

    )



fun MovieDetailsDto.toMovieDetails() : MovieDetails =
    MovieDetails(
        id = id,
        title = title,
        pgAge = setPgAge(adult),
        detailImageUrl = imageDetailPath,
        runningTime = runningTime,
        rating = rating.toInt(),
        reviewCount = reviewCount,
        storyLine = storyLine,
        isLiked = false,
        genres = genres.map { Genre(id = it.id, name = it.name) },
    )

fun MovieDetailsDto.toMovieDetailEntityDb() : MovieDetailsEntityDb =
    MovieDetailsEntityDb(
        id = id,
        title = title,
        pgAge = setPgAge(adult),
        detailImageUrl = imageDetailPath,
        runningTime = runningTime,
        rating = rating.toInt(),
        reviewCount = reviewCount,
        storyLine = storyLine,
        isLiked = false,
        genres = genres.map { GenreEntityDb(id = it.id, name = it.name) },
    )


fun MovieDetailsEntityDb.toMovieDetail(): MovieDetails =
    MovieDetails(
        id = id,
        title = title,
        pgAge = pgAge,
        detailImageUrl = detailImageUrl,
        runningTime = runningTime,
        rating = rating,
        reviewCount = reviewCount,
        storyLine = storyLine,
        isLiked = isLiked,
        genres = genres.map { Genre(id = it.id, name = it.name) }
    )


fun ActorDto.toActorData(): Actor =

    Actor(
        id = id,
        name = name,
        imageUrl = imageActorPath
    )

fun ActorDto.toActorEntityDb(movieId: Int) : ActorEntityDb =
    ActorEntityDb(
        movieId = movieId,
        actorId = id,
        name = name,
        imageUrl = imageActorPath
    )


private fun setPgAge(isAdult: Boolean): Int = if (isAdult) PG_ADULT else PG_CHILDREN


