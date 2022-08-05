package com.example.movies.data.core.util

import com.example.movies.data.moviedetails.local.model.ActorEntity
import com.example.movies.data.moviedetails.local.model.MovieDetailsEntity
import com.example.movies.data.moviedetails.local.model.MovieDetailsWithActorsEntity
import com.example.movies.data.moviedetails.remote.model.ActorDto
import com.example.movies.data.moviedetails.remote.model.MovieDetailsDto
import com.example.movies.data.movies.local.model.GenreEntity
import com.example.movies.data.movies.local.model.MovieEntity
import com.example.movies.data.movies.remote.model.GenreDto
import com.example.movies.data.movies.remote.model.MovieDto
import com.example.movies.domain.model.Actor
import com.example.movies.domain.model.Genre
import com.example.movies.domain.model.Movie
import com.example.movies.domain.model.MovieDetails


fun MovieDto.toDomain(genres: List<GenreEntity>, baseUrl: String): Movie =
    Movie(
        id = id,
        title = title,
        pgAge = setPgAge(adult),
        imageUrl = getFullUrlImage(baseUrl, imagePath),
        rating = rating.toInt(),
        reviewCount = reviewCount,
        storyLine = storyLine,
        genres = genres
            .filter { it.id in this.genresId }
            .map { genreEntity -> genreEntity.toDomain() }

    )

fun MovieEntity.toDomain(): Movie =
    Movie(
        id = id,
        title =title,
        pgAge = pgAge,
        imageUrl = imageUrl,
        rating = rating,
        reviewCount = reviewCount,
        storyLine = storyLine,

        genres = genres.map {
            Genre(id = it.id, name = it.name)
        }
    )


fun GenreDto.toDomain(): Genre = Genre(id = id, name = name)

fun GenreEntity.toDomain() : Genre =
    Genre(id = id , name = name)

fun MovieDetailsEntity.toDomain(): MovieDetails =
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


fun ActorDto.toDomain(): Actor =
    Actor(
        id = id,
        name = name,
        imageUrl = imageActorPath
    )

fun ActorEntity.toDomain() : Actor =
    Actor(
        id = actorId,
        name = name,
        imageUrl = imageUrl
    )


fun MovieDetailsDto.toDomain(): MovieDetails =
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