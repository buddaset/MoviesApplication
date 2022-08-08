package com.example.movies.data.core.util


import com.example.movies.data.moviedetails.local.model.ActorEntity
import com.example.movies.data.moviedetails.local.model.MovieDetailsEntity
import com.example.movies.data.moviedetails.remote.model.ActorDto
import com.example.movies.data.moviedetails.remote.model.MovieDetailsDto
import com.example.movies.data.movies.local.model.GenreEntity
import com.example.movies.data.movies.local.model.MovieEntity
import com.example.movies.data.movies.remote.model.GenreDto
import com.example.movies.data.movies.remote.model.MovieDto
import com.example.movies.domain.model.Movie


fun MovieDto.toEntity(genres: List<GenreEntity>, baseUrl: String): MovieEntity =
    MovieEntity(
        id = id,
        title = title,
        pgAge = setPgAge(adult),
        imageUrl = getFullUrlImage(baseUrl, imagePath),
        rating = rating.toInt(),
        reviewCount = reviewCount,
        storyLine = storyLine,
        genres = genres.filter { it.id in genresId }
    )


fun Movie.toEntity(): MovieEntity =
    MovieEntity(
        id = id,
        title =title,
        pgAge = pgAge,
        imageUrl = imageUrl,
        rating = rating,
        reviewCount = reviewCount,
        storyLine = storyLine,
        genres = genres.map {
            GenreEntity(id = it.id, name = it.name)
        }
    )

fun MovieDetailsDto.toEntity(baseUrl: String): MovieDetailsEntity =
    MovieDetailsEntity(
        id = id,
        title = title,
        pgAge = setPgAge(adult),
        detailImageUrl = fullUrl(baseUrl, imageDetailPath),
        runningTime = runningTime,
        rating = rating.toInt(),
        reviewCount = reviewCount,
        storyLine = storyLine,
        isLiked = false,
        genres = genres.map { GenreEntity(id = it.id, name = it.name) },
    )

fun ActorDto.toEntity(movieId: Long, baseImageUrl: String): ActorEntity =
    ActorEntity(
        movieId = movieId,
        actorId = id,
        name = name,
        imageUrl = fullUrl(baseImageUrl, imageActorPath)
    )

fun GenreDto.toEntity(): GenreEntity =
    GenreEntity(id = this.id , name = this.name)



internal fun fullUrl(baseUrl: String, path: String?) = "$baseUrl$path"

internal fun setPgAge(isAdult: Boolean): Int = if (isAdult) PG_ADULT else PG_CHILDREN

internal fun getFullUrlImage(baseUrl: String, imagePath: String?) =
    "$baseUrl$imagePath"

const val PG_ADULT = 16
const val PG_CHILDREN = 13

