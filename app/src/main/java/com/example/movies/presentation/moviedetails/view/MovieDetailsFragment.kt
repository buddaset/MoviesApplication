package com.example.movies.presentation.moviedetails.view

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.disneyperson.core.delegate.viewBinding
import com.example.movies.R
import com.example.movies.core.application.App
import com.example.movies.core.navigation.Navigator
import com.example.movies.databinding.FragmentMoviesDetailsBinding
import com.example.movies.domain.model.MovieDetails
import com.example.movies.presentation.moviedetails.view.actorAdapter.ActorAdapter
import com.example.movies.presentation.moviedetails.viewmodel.DetailsMovieViewModel
import com.example.movies.presentation.moviedetails.viewmodel.MovieDetailsState
import com.example.movies.presentation.movies.view.movieAdapter.MovieUtils
import com.example.movies.presentation.util.ViewModelFactory
import com.example.movies.presentation.util.collectFlow
import com.example.movies.presentation.util.onTryAgain


class MovieDetailsFragment : Fragment(R.layout.fragment_movies_details) {

    private val binding: FragmentMoviesDetailsBinding by viewBinding()

    private val actorAdapter = ActorAdapter()

    private val viewModel: DetailsMovieViewModel by viewModels {
        ViewModelFactory(
            (requireActivity().application as App).repository
        )
    }

    lateinit var navigator: Navigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.actorRecycler.adapter = actorAdapter

        setupListeners()

        collectFlow(viewModel.movie, ::renderState)
    }

    private fun setupListeners() {
        binding.backPress.setOnClickListener { navigator.back() }

        onTryAgain(binding.root) { viewModel.tryAgain() }
    }

    private fun renderState(state: MovieDetailsState) {
        binding.loadStateView.progressBar.isVisible = state is MovieDetailsState.Loading
        binding.loadStateView.messageTextView.isVisible = state is MovieDetailsState.Error
        binding.loadStateView.tryAgainButton.isVisible = state is MovieDetailsState.Error

        if (state is MovieDetailsState.Success)
            showMovieDetails(state.data)
    }


    private fun showMovieDetails(movie: MovieDetails) = with(binding) {
        val context = requireContext()
        title.text = movie.title
        pgAge.text = context.getString(R.string.pg_age, movie.pgAge)
        genre.text = MovieUtils.getGenreOfMovie(movie.genres)
        ratingBar.rating = MovieUtils.getRating(movie.rating)
        countReview.text = context.getString(R.string.reviews, movie.reviewCount)
        storyLine.text = movie.storyLine
        actorAdapter.submitList(movie.actors)
        updatePoster(movie.detailImageUrl)
    }

    private fun updatePoster(url: String?) =
        Glide.with(requireContext())
            .load(url)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.poster)


    companion object {
        const val MOVIE_ID = "movieId"

        fun newInstance(movieId: Long): MovieDetailsFragment =
            MovieDetailsFragment().apply {
                arguments = bundleOf(MOVIE_ID to movieId)
            }
    }
}