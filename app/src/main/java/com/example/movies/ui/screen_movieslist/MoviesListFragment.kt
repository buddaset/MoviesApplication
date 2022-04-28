package com.example.movies

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.data.Result
import com.example.movies.ui.screen_movieslist.movieAdapter.MovieAdapter
import com.example.movies.ui.screen_movieslist.movieAdapter.MovieListener
import com.example.movies.databinding.FragmentMoviesListBinding
import com.example.movies.models.MovieData
import com.example.movies.ui.BaseFragment
import com.example.movies.ui.ViewModelFactory
import com.example.movies.ui.collectFlow
import com.example.movies.ui.screen_movieslist.ListMovieViewModel


class MoviesListFragment : BaseFragment(), MovieListener {

    private lateinit var binding: FragmentMoviesListBinding
    private lateinit var movieAdapter: MovieAdapter
    private var listener: ClickMovieListener? = null
    private val viewModel: ListMovieViewModel by viewModels {
        ViewModelFactory(
            (requireActivity().application as App).repository
        )
    }

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

        collectFlow(viewModel.listMovie) { result ->
            when (result) {
                is Result.Success -> updateAdapter(result.data)
                is Result.Error -> showError()
                is Result.Loading -> {}

            }
        }
    }


    private fun showError() {
        binding.movieRecyclerview.isVisible = false
        Toast.makeText(requireContext(), "Mistake", Toast.LENGTH_LONG).show()


    }


    private fun updateAdapter(list: List<MovieData>) {
        movieAdapter.submitList(list)
    }

    private fun setupMovieAdapter() {
        movieAdapter = MovieAdapter(this)
        binding.movieRecyclerview.adapter = movieAdapter
    }


    override fun onCLickMovie(movie: MovieData) {
        listener?.clickMovie(movie.id)
    }

    companion object {

        fun newInstance() = MoviesListFragment()
    }
}


interface ClickMovieListener {

    fun clickMovie(movieId: Int)
}


