package com.example.movies.presentation.favorite_movies.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.disneyperson.core.delegate.viewBinding
import com.example.movies.R
import com.example.movies.core.application.App
import com.example.movies.core.navigation.MovieDetailsScreen
import com.example.movies.core.navigation.Navigator
import com.example.movies.databinding.FragmentFavoriteMoviesBinding
import com.example.movies.presentation.core.model.MovieUI
import com.example.movies.presentation.favorite_movies.viewmodel.FavoriteMoviesState
import com.example.movies.presentation.favorite_movies.viewmodel.FavoriteMoviesViewModel
import com.example.movies.presentation.favorite_movies.viewmodel.FavoriteMoviesViewModelFactory
import com.example.movies.presentation.main.MainActivity

import com.example.movies.presentation.movies.view.movieAdapter.MovieClickListener
import com.example.movies.presentation.core.util.collectFlow

class FavoriteMoviesFragment : Fragment(R.layout.fragment_favorite_movies), MovieClickListener {

    private val binding :FragmentFavoriteMoviesBinding by viewBinding()

    lateinit var navigator: Navigator

    private val viewModel: FavoriteMoviesViewModel by viewModels {
        FavoriteMoviesViewModelFactory(
            (requireActivity().application as App).useCase.getFavoriteMoviesUseCase(),
            (requireActivity().application as App).useCase.getChangeFavoriteFlagMovieUseCase()
        )
    }

    private val moviesAdapter by lazy {   FavoriteMovieAdapter(this) }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            navigator = context.navigator
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectFlow(viewModel.favoriteMovies,::renderState)
        setupAdapter()
    }

    override fun onClickMovie(movieUI: MovieUI) =
        navigator.navigateTo(MovieDetailsScreen(movieId = movieUI.movie.id))


    override fun onClickFavorite(movieUI: MovieUI) {
        viewModel.changeFavoriteFlagMovie(movieUI)
    }

    private fun setupAdapter() {
        binding.movieRecyclerview.adapter = moviesAdapter
        binding.movieRecyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun renderState(state: FavoriteMoviesState) = with(binding) {
        emptyList.isVisible = state is FavoriteMoviesState.Empty
        loadStateView.progressBar.isVisible = state is FavoriteMoviesState.Loading
        loadStateView.messageTextView.isVisible = state is FavoriteMoviesState.Error
        loadStateView.tryAgainButton.isVisible = state is FavoriteMoviesState.Error
        movieRecyclerview.isVisible = state is FavoriteMoviesState.Success

        if (state is FavoriteMoviesState.Success)
            updateMovies(state.data)

    }

    private fun updateMovies(movies: List<MovieUI>) =
        moviesAdapter.submitList(movies)

}