package com.example.movies.domain.usecase

import androidx.paging.PagingData
import com.example.movies.domain.model.Movie
import com.example.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class GetPopularMoviesUseCase(private val repository: MoviesRepository) {


    operator fun invoke() : Flow<PagingData<Movie>> = repository.getPopularMovies()
}