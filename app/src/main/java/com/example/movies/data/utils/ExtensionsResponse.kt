package com.example.movies.data.utils


import com.example.movies.data.local.entity.GenreEntityDb
import com.example.movies.data.local.entity.MovieDetailsEntityDb
import com.example.movies.data.local.entity.MovieEntityDb
import com.example.movies.data.remote.response.ActorResponse
import com.example.movies.data.remote.response.MovieDetailsResponse
import com.example.movies.data.remote.response.MovieResponse
import com.example.movies.models.ActorData
import com.example.movies.models.GenreData
import com.example.movies.models.MovieDetails

const val PG_ADULT = 16
const val PG_CHILDREN = 13

fun MovieResponse.toMovieEntityDb(genres: List<GenreEntityDb>) : MovieEntityDb =
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



fun MovieDetailsResponse.toMovieDetails() : MovieDetails =
    MovieDetails(
        id = id,
        title = title,
        pgAge = setPgAge(adult),
        detailImageUrl = imageDatailPath,
        runningTime = runningTime,
        rating = rating.toInt(),
        reviewCount = reviewCount,
        storyLine = storyLine,
        isLiked = false,
        genres = genres.map { GenreData(id = it.id, name = it.name) },
    )

fun MovieDetailsResponse.toMovieDetailEntityDb() : MovieDetailsEntityDb =
    MovieDetailsEntityDb(
        id = id,
        title = title,
        pgAge = setPgAge(adult),
        detailImageUrl = imageDatailPath,
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
        genres = genres.map { GenreData(id = it.id, name = it.name) }
    )


fun ActorResponse.toActorData(): ActorData =

    ActorData(
        id = id,
        name = name,
        imageUrl = imageActorPath
    )


private fun setPgAge(isAdult: Boolean): Int = if (isAdult) PG_ADULT else PG_CHILDREN


