package com.example.movies.domain.usecase

import com.example.movies.domain.model.MovieDetails
import com.example.movies.domain.repository.MovieDetailsRepository
import com.example.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class GetMovieDetailsUseCase(private val repository: MovieDetailsRepository) {


    suspend operator fun invoke(movieId: Long) : Flow<MovieDetails> =
        repository.getMovieDetails(movieId)
}