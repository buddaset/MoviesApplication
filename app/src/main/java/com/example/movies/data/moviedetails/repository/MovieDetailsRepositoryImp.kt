package com.example.movies.data.moviedetails.repository

import com.example.movies.core.util.ImageUrlAppender
import com.example.movies.core.util.mapResult
import com.example.movies.core.util.onSuccess
import com.example.movies.core.util.toEntity
import com.example.movies.data.moviedetails.local.MovieDetailsLocalDataSource
import com.example.movies.data.moviedetails.local.model.toDomain
import com.example.movies.data.moviedetails.remote.MovieDetailsRemoteDataSource
import com.example.movies.domain.model.MovieDetails
import com.example.movies.domain.repository.MovieDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieDetailsRepositoryImpl(
    private val remoteDataSource: MovieDetailsRemoteDataSource,
    private val localDataSource: MovieDetailsLocalDataSource,
    private val imageUrlAppender: ImageUrlAppender
) : MovieDetailsRepository {


    override suspend fun getMovieDetails(movieId: Long): Flow<MovieDetails> {
        updateMovieDetails(movieId)
        updateActors(movieId)
        return localDataSource.getMovieDetailsWithActors(movieId)
            .map { it.toDomain() }
    }


    private suspend fun updateMovieDetails(idMovie: Long) {
        remoteDataSource.loadMovieDetails(idMovie)
            .mapResult { movieDto -> movieDto.toEntity(imageUrlAppender.baseImageUrl) }
            .onSuccess { movieEntity -> localDataSource.updateMovieDetails(movieEntity) }
    }


    private suspend fun updateActors(movieId: Long) =
        remoteDataSource.loadMovieActors(movieId)
            .mapResult { listDto -> listDto.map { actorDto -> actorDto.toEntity(movieId) } }
            .onSuccess { actorsEntity -> localDataSource.updateActors(actorsEntity) }


}