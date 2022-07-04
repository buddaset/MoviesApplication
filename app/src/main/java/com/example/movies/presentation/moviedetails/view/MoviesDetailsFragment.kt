package com.example.movies.presentation.moviedetails.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.disneyperson.core.delegate.viewBinding
import com.example.movies.R
import com.example.movies.core.application.App
import com.example.movies.core.navigation.Navigator
import com.example.movies.databinding.FragmentMoviesDetailsBinding
import com.example.movies.domain.model.MovieDetails
import com.example.movies.presentation.*
import com.example.movies.presentation.moviedetails.viewmodel.DetailsMovieViewModel
import com.example.movies.presentation.movies.view.movieAdapter.MovieUtils
import com.example.movies.presentation.moviedetails.view.actorAdapter.ActorAdapter
import com.example.movies.presentation.util.ViewModelFactory


class MoviesDetailsFragment :Fragment(R.layout.fragment_movies_details) {

    private val binding: FragmentMoviesDetailsBinding by viewBinding()

    private val  actorAdapter = ActorAdapter()

    private val viewModel: DetailsMovieViewModel by viewModels {
        ViewModelFactory(
            (requireActivity().application as App).repository
        )
    }

    lateinit var navigator: Navigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.actorRecycler.adapter = actorAdapter

        binding.backPress.setOnClickListener {
            activity?.onBackPressed()
        }

        collectFlow(viewModel.movieDetails) { result ->
            renderState(root = binding.movieDetailConstraint, result, ::showDetails)
        }

        onTryAgain(binding.root) {
            viewModel.tryAgain()
        }
    }


    private fun showDetails(movie: MovieDetails) {
        val context = requireContext()
        with(binding) {

            title.text = movie.title
            pgAge.text = context.getString(R.string.pg_age, movie.pgAge)
            genre.text = MovieUtils.getGenreOfMovie(movie.genres)
            ratingBar.rating = MovieUtils.getRating(movie.rating)
            countReview.text = context.getString(R.string.reviews, movie.reviewCount)
            storyLine.text = movie.storyLine
            Log.d("AAA", "frarment \n ${movie.actors}  }")
            actorAdapter.submitList(movie.actors)
        }
        Glide.with(context)
            .load(movie.detailImageUrl)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.poster)

    }

    companion object {

      const val MOVIE_ID = "movieId"



        fun newInstance(movieId: Long): MoviesDetailsFragment =
            MoviesDetailsFragment().apply {
                arguments = bundleOf( MOVIE_ID to movieId)

            }


    }
}