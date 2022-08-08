package com.example.movies.domain.usecase

import com.example.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteMovieIdsUseCase(private val repository: MoviesRepository) {


    operator fun invoke() : Flow<List<Long>> =
        repository.getFavoriteIds()
}