package com.example.movies

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.android.academy.fundamentals.homework.features.data.loadMovies
import com.example.movies.ui.screenMoviesList.movieAdapter.MovieAdapter
import com.example.movies.ui.screenMoviesList.movieAdapter.MovieListener
import com.example.movies.databinding.FragmentMoviesListBinding
import com.example.movies.models.MovieData
import com.example.movies.ui.ViewModelFactory
import com.example.movies.ui.screenMoviesList.ListMovieViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesListFragment : Fragment(), MovieListener {

    private lateinit var binding: FragmentMoviesListBinding
    private lateinit var movieAdapter: MovieAdapter
    private var listener: ClickMovieListener? = null
    private val viewModel: ListMovieViewModel by viewModels { ViewModelFactory(requireActivity().application) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ClickMovieListener)
            listener = context
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMovieAdapter()
        viewModel.listMovie.observe(viewLifecycleOwner, this::updateAdapter)
    }

    private fun updateAdapter(list: List<MovieData>) {
        movieAdapter.submitList(list)
    }

    private fun setupMovieAdapter() {
        movieAdapter = MovieAdapter(this)
        binding.movieRecyclerview.adapter = movieAdapter
    }


    override fun onCLickMovie(movie: MovieData) {
        listener?.clickMovie(movie)
    }

    companion object {

        fun newInstance() = MoviesListFragment()
    }
}


interface ClickMovieListener {

    fun clickMovie(movie: MovieData)
}


