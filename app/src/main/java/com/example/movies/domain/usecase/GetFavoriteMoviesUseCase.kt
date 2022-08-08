package com.example.movies.domain.usecase

import com.example.movies.domain.model.Movie
import com.example.movies.domain.model.MovieDetails
import com.example.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteMoviesUseCase(private val repository: MoviesRepository) {


    operator fun invoke(): Flow<List<Movie>>  =
        repository.getFavoriteMovies()
}





