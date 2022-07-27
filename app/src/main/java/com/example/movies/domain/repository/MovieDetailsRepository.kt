package com.example.movies.domain.repository

import com.example.movies.domain.model.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MovieDetailsRepository {

    suspend fun getMovieDetails(movieId: Long): Flow<MovieDetails>
}