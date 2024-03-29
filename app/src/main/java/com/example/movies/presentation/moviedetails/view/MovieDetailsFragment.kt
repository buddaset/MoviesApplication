package com.example.movies.presentation.moviedetails.view


import android.content.Context
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
import com.example.movies.domain.model.Actor
import com.example.movies.presentation.core.util.collectFlow
import com.example.movies.presentation.core.util.onTryAgain
import com.example.movies.presentation.main.MainActivity
import com.example.movies.presentation.moviedetails.model.MovieDetailsUI
import com.example.movies.presentation.moviedetails.view.actorAdapter.ActorAdapter
import com.example.movies.presentation.moviedetails.viewmodel.DetailViewModelFactory
import com.example.movies.presentation.moviedetails.viewmodel.DetailsMovieViewModel
import com.example.movies.presentation.moviedetails.viewmodel.MovieDetailsState


class MovieDetailsFragment : Fragment(R.layout.fragment_movies_details) {

    private val binding: FragmentMoviesDetailsBinding by viewBinding()

    private val actorAdapter = ActorAdapter()

    private val viewModel: DetailsMovieViewModel by viewModels {
        DetailViewModelFactory(
            (requireActivity().application as App).useCase.getMovieDetailsUseCase(),
            this,
            this.arguments
        )
    }

    lateinit var navigator: Navigator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            navigator = context.navigator
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupAdapter()
        setupListeners()
        collectFlow(viewModel.movie, ::renderState)
    }

    private fun setupAdapter() {
        binding.actorRecycler.adapter = actorAdapter
    }

    private fun setupListeners() {
        binding.backPress.setOnClickListener { navigator.back() }
        onTryAgain(binding.root) { viewModel.tryAgain() }
    }

    private fun renderState(state: MovieDetailsState) {
        binding.groupMovieDetails.isVisible = state is MovieDetailsState.Success
        binding.loadStateView.progressBar.isVisible = state is MovieDetailsState.Loading
        binding.loadStateView.messageTextView.isVisible = state is MovieDetailsState.Error
        binding.loadStateView.tryAgainButton.isVisible = state is MovieDetailsState.Error

        if (state is MovieDetailsState.Success)
            showMovieDetails(state.data)
    }


    private fun showMovieDetails(movie: MovieDetailsUI) = with(binding) {
        val context = requireContext()
        title.text = movie.details.title
        pgAge.text = context.getString(R.string.pg_age, movie.details.pgAge)
        genre.text = movie.genres
        ratingBar.rating = movie.rating
        countReview.text = context.getString(R.string.reviews, movie.details.reviewCount)
        storyLine.text = movie.details.storyLine
       updateActors(movie.details.actors)
        updatePoster(movie.details.detailImageUrl)
    }

    private fun  updateActors(actors: List<Actor>) =
        actorAdapter.submitList(actors)



    private fun updatePoster(url: String?) =
        Glide.with(requireContext())
            .load(url)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.poster)


    companion object {

        const val MOVIE_ID = "movieId"

        fun args(movieId: Long): Bundle = bundleOf(MOVIE_ID to movieId)
    }
}