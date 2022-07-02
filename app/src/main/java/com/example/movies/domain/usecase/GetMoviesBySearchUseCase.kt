package com.example.movies.domain.usecase

import com.example.movies.core.util.Result
import com.example.movies.domain.model.Movie
import com.example.movies.domain.repository.MovieRepository

class GetMoviesBySearchUseCase(private val repository: MovieRepository) {


    suspend operator fun invoke(query: String) : Result<List<Movie>, Throwable> =
        repository.getMoviesBySearch(query)
}