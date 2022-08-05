package com.example.movies.domain.usecase

import androidx.paging.PagingData
import com.example.movies.domain.model.Movie
import com.example.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class GetMoviesBySearchUseCase(private val repository: MoviesRepository) {


    operator fun invoke(query: String): Flow<PagingData<Movie>> =
        repository.getMoviesBySearch(query)
}