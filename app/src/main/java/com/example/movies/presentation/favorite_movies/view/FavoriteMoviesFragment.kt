package com.example.movies.presentation.favorite_movies.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.disneyperson.core.delegate.viewBinding
import com.example.movies.R
import com.example.movies.core.application.App
import com.example.movies.databinding.FragmentFavoriteMoviesBinding
import com.example.movies.domain.model.Movie
import com.example.movies.presentation.favorite_movies.viewmodel.FavoriteMoviesState
import com.example.movies.presentation.favorite_movies.viewmodel.FavoriteMoviesViewModel
import com.example.movies.presentation.favorite_movies.viewmodel.FavoriteMoviesViewModelFactory
import com.example.movies.presentation.favorite_movies.view.favoritemovieadapter.FavoriteMoviesAdapter
import com.example.movies.presentation.movies.view.movieAdapter.MovieClickListener
import com.example.movies.presentation.util.collectFlow

class FavoriteMoviesFragment : Fragment(R.layout.fragment_favorite_movies), MovieClickListener {

    private val binding :FragmentFavoriteMoviesBinding by viewBinding()

    private val viewModel: FavoriteMoviesViewModel by viewModels {
        FavoriteMoviesViewModelFactory(
            (requireActivity().application as App).useCase.getFavoriteMoviesUseCase(),
            (requireActivity().application as App).useCase.getChangeFavoriteFlagMovie()
        )
    }

    private val moviesAdapter by lazy {   FavoriteMoviesAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        collectFlow(viewModel.favoriteMovies,::renderState)


    }

    override fun onClickMovie(movie: Movie) {
        TODO("Not yet implemented")
    }

    override fun onClickFavorite(movie: Movie) {
        viewModel.changeFavoriteFlagMovie(movie)
    }

    private fun renderState(state: FavoriteMoviesState) = with(binding) {
        movieRecyclerview.isVisible = state is FavoriteMoviesState.Success
        emptyList.isVisible = state is FavoriteMoviesState.Empty
        loadStateView.progressBar.isVisible = state is FavoriteMoviesState.Loading
        loadStateView.messageTextView.isVisible = state is FavoriteMoviesState.Error
        loadStateView.tryAgainButton.isVisible = state is FavoriteMoviesState.Error

        if (state is FavoriteMoviesState.Success)
            updateMovies(state.data)
    }

    private fun updateMovies(movies: List<Movie>) =
        moviesAdapter.submitList(movies)




}