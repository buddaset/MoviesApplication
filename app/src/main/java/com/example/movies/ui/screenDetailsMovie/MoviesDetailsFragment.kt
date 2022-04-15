package com.example.movies.ui.screenDetailsMovie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.ui.screenDetailsMovie.actorAdapter.ActorAdapter
import com.example.movies.databinding.FragmentMoviesDetailsBinding
import com.example.movies.models.MovieData
import com.example.movies.ui.screenMoviesList.movieAdapter.MovieUtils
import com.squareup.picasso.Picasso
import java.lang.IllegalArgumentException


class MoviesDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMoviesDetailsBinding
    private lateinit var movie : MovieData
    private lateinit var actorAdapter: ActorAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentMoviesDetailsBinding.inflate(inflater, container, false)
        movie = arguments?.getParcelable(MOVIE_ITEM) ?: throw IllegalArgumentException("Not have movieItem")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actorAdapter = ActorAdapter()
        binding.actorRecycler.adapter = actorAdapter
        showDetails(movie)

        binding.backPress.setOnClickListener {
            activity?.onBackPressed()
        }

    }

    private fun showDetails(movie: MovieData) {
        val context = requireContext()
        with(binding) {

            title.text = movie.title
            pgAge.text = context.getString(R.string.pg_age,movie.pgAge)
            genre.text = MovieUtils.getGenreOfMovie(movie.genres)
            ratingBar.rating = MovieUtils.getRating(movie.rating)
            countReview.text = context.getString(R.string.reviews, movie.reviewCount)
            storyLine.text = movie.storyLine
            actorAdapter.submitList(movie.actors)
        }
        Picasso.get()
            .load(movie.detailImageUrl)
            .into(binding.poster)

    }

    companion object {

        private const val MOVIE_ITEM = "movieItem"

        @JvmStatic
        fun newInstance(movie: MovieData) : MoviesDetailsFragment {
            val instance = MoviesDetailsFragment()
            val bundle = Bundle()
                bundle.putParcelable(MOVIE_ITEM, movie)
            instance.arguments = bundle
            return instance

        }




    }
}