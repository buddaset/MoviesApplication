package com.example.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movies.adapter.actorAdapter.ActorAdapter
import com.example.movies.databinding.ActivityMainBinding
import com.example.movies.databinding.FragmentMoviesDetailsBinding
import com.example.movies.models.MovieItem
import java.lang.IllegalArgumentException


class MoviesDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMoviesDetailsBinding
    private lateinit var movie : MovieItem
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


    }

    private fun showDetails(movie: MovieItem) {
        with(binding) {
            poster.setImageResource(movie.poster)
            pgMovie?.text= movie.pg
            nameMovie.text = movie.name
            countReview.text = getString(R.string.reviews, movie.countReviews)
            textOverview.text = movie.overview
            actorAdapter.addData(movie.actors)

        }

    }

    companion object {

        private const val MOVIE_ITEM = "movieItem"

        @JvmStatic
        fun newInstance(movie: MovieItem) : MoviesDetailsFragment {
            val instance = MoviesDetailsFragment()
            val bundle = Bundle()
                bundle.putParcelable(MOVIE_ITEM, movie)
            instance.arguments = bundle
            return instance

        }




    }
}