package com.example.movies

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movies.adapter.movieAdapter.MovieAdapter
import com.example.movies.adapter.movieAdapter.MovieListener
import com.example.movies.databinding.FragmentMoviesListBinding
import com.example.movies.models.MovieData

class MoviesListFragment : Fragment(), MovieListener {

    private lateinit var binding: FragmentMoviesListBinding
    private lateinit var movieAdapter: MovieAdapter
    private var listener: ClickMovieListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ClickMovieListener)
            listener = context
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = MovieAdapter(this)

        setupMovieAdapter()


    }

    private fun setupMovieAdapter() {
        binding.movieRecyclerview?.adapter = movieAdapter
        val data = AppUtils(requireContext()).fakeData
        movieAdapter.submitList(data)

    }


    companion object {

        fun newInstance() = MoviesListFragment()

    }

    override fun onCLickMovie(movie: MovieData) {
        listener?.clickMovie(movie)

    }

}


interface ClickMovieListener {

        fun clickMovie(movie: MovieData)
    }


