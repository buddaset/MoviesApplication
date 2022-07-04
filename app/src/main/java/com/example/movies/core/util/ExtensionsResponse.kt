package com.example.movies.core.util


import com.example.movies.data.moviedetails.local.model.ActorEntityDb
import com.example.movies.data.moviedetails.local.model.MovieDetailsEntityDb
import com.example.movies.data.moviedetails.remote.model.ActorDto
import com.example.movies.data.moviedetails.remote.model.MovieDetailsDto
import com.example.movies.data.movies.local.model.GenreEntityDb
import com.example.movies.data.movies.local.model.MovieEntityDb
import com.example.movies.data.movies.remote.model.GenreDto
import com.example.movies.data.movies.remote.model.MovieDto
import com.example.movies.domain.model.Actor
import com.example.movies.domain.model.Genre
import com.example.movies.domain.model.Movie
import com.example.movies.domain.model.MovieDetails

const val PG_ADULT = 16
const val PG_CHILDREN = 13

//todo Update with urlAppender

fun MovieDto.toDomain(genres: List<GenreEntityDb>, baseUrl: String): Movie =
    Movie(
        id = id.toLong(),
        title = title,
        pgAge = setPgAge(adult),
        imageUrl = getFullUrlImage(baseUrl, this.imagePath),
        rating = rating.toInt(),
        reviewCount = reviewCount,
        storyLine = storyLine,
        isLiked = false,
        genres = genres
            .filter { it.id in this.genresId }
            .map {genreEntity -> genreEntity.toDomain() }

    )

fun getFullUrlImage(baseUrl: String, imagePath: String) =
    "$baseUrl$imagePath"

fun GenreDto.toDomain(): Genre = Genre(id = id, name = name)

fun MovieDto.toEntity(genres: List<GenreEntityDb>, baseUrl: String): MovieEntityDb =
    MovieEntityDb(
        id = id,
        title = title,
        pgAge = setPgAge(adult),
        imageUrl = getFullUrlImage(baseUrl, imagePath),
        rating = rating.toInt(),
        reviewCount = reviewCount,
        storyLine = storyLine,
        isLiked = false,
        genres = genres.filter { it.id in genresId }

    )


fun MovieDetailsDto.toMovieDetails(): MovieDetails =
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

fun MovieDetailsDto.toEntity(baseUrl: String): MovieDetailsEntityDb =
    MovieDetailsEntityDb(
        id = id,
        title = title,
        pgAge = setPgAge(adult),
        detailImageUrl = fullUrl(baseUrl, imageDetailPath),
        runningTime = runningTime,
        rating = rating.toInt(),
        reviewCount = reviewCount,
        storyLine = storyLine,
        isLiked = false,
        genres = genres.map { GenreEntityDb(id = it.id, name = it.name) },
    )


fun MovieDetailsEntityDb.toDomain(): MovieDetails =
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

fun ActorDto.toEntity(movieId: Long): ActorEntityDb =
    ActorEntityDb(
        movieId = movieId,
        actorId = id,
        name = name,
        imageUrl = imageActorPath
    )

private fun fullUrl(baseUrl: String, path: String) = "$baseUrl$path"

private fun setPgAge(isAdult: Boolean): Int = if (isAdult) PG_ADULT else PG_CHILDREN


