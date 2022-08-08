package com.example.movies.domain.usecase

import com.example.movies.domain.model.Movie
import com.example.movies.domain.repository.MoviesRepository

class ChangeFavoriteFlagMovieUseCase(private val repository: MoviesRepository) {

    suspend operator fun invoke(movie: Movie, isLiked: Boolean) =
        repository.changeFavoriteFlagMovie(movie,isLiked)
}