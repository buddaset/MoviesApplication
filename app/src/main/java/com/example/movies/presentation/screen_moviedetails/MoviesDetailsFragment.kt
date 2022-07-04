package com.example.movies.presentation.screen_moviedetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.movies.core.application.App
import com.example.movies.R
import com.example.movies.databinding.FragmentMoviesDetailsBinding
import com.example.movies.domain.model.MovieDetails
import com.example.movies.presentation.*
import com.example.movies.presentation.screen_moviedetails.actorAdapter.ActorAdapter
import com.example.movies.presentation.movies.view.movieAdapter.MovieUtils


class MoviesDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentMoviesDetailsBinding

    private lateinit var actorAdapter: ActorAdapter
    private var movieId = UNDEFINED_ID

    private val viewModel: DetailsMovieViewModel by viewModels {
        ViewModelFactory(
            (requireActivity().application as App).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieId = arguments?.getInt(MOVIE_ID) ?: throw IllegalArgumentException("Unknown movie ID")
        viewModel.getMovieDetail(movieId)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMoviesDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actorAdapter = ActorAdapter()
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

        private const val MOVIE_ID = "movieId"
        private const val UNDEFINED_ID = -1

        @JvmStatic
        fun newInstance(movieId: Int): MoviesDetailsFragment {
            val instance = MoviesDetailsFragment()
            val bundle = Bundle()
            bundle.putInt(MOVIE_ID, movieId)
            instance.arguments = bundle
            return instance

        }
    }
}