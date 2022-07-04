package com.example.movies.presentation.movies.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.disneyperson.core.delegate.viewBinding
import com.example.movies.R
import com.example.movies.core.application.App
import com.example.movies.core.navigation.MovieDetailsScreen
import com.example.movies.core.navigation.Navigator
import com.example.movies.databinding.FragmentMoviesBinding
import com.example.movies.di.ViewModelFactory
import com.example.movies.presentation.movies.view.movieAdapter.DefaultLoadingStateAdapter
import com.example.movies.presentation.movies.view.movieAdapter.MovieAdapter
import com.example.movies.presentation.movies.viewmodel.MoviesViewModel
import com.example.movies.presentation.util.collectPagingFlow
import com.example.movies.presentation.util.textChange


class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private val binding: FragmentMoviesBinding by viewBinding()

    private val viewModel: MoviesViewModel by viewModels {
        ViewModelFactory(
            (requireActivity().application as App).useCase.getPopularMoviesUseCase(),
            (requireActivity().application as App).useCase.getMoviesBySearchUseCase()
        )
    }

    private val movieAdapter = MovieAdapter { movie ->
        navigator.navigateTo(MovieDetailsScreen(movie.id))

    }

    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMovieAdapter()
        observeMovies()
        observeLoadState()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.textChange(::queryMovie)
    }

    private fun queryMovie(query: String) = viewModel.setSearchBy(query)

    private fun setupMovieAdapter() {

        val footerAdapter = DefaultLoadingStateAdapter { movieAdapter.retry() }
        val headerAdapter = DefaultLoadingStateAdapter { movieAdapter.retry() }
        val adapterWithLoadState = movieAdapter.withLoadStateHeaderAndFooter(
            footer = footerAdapter,
            header = headerAdapter
        )
        binding.movieRecyclerview.adapter = adapterWithLoadState
        binding.movieRecyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.loadStateView.tryAgainButton.setOnClickListener { movieAdapter.retry() }

    }

    private fun observeMovies() {
        collectPagingFlow(viewModel.movies, movieAdapter::submitData)
    }

    private fun getLayoutManager(
        concatAdapter: ConcatAdapter,
        footerAdapter: DefaultLoadingStateAdapter
    ): RecyclerView.LayoutManager {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int =
                if (position == 0 && footerAdapter.itemCount > 0) ITEM_SPAN_SIZE
                else if (position == concatAdapter.itemCount - 1 && footerAdapter.itemCount > 0) ITEM_SPAN_SIZE
                else ERROR_LOADING_SPAN_SIZE
        }
        return layoutManager
    }

    private fun observeLoadState() {

        movieAdapter.addLoadStateListener { loadState ->
            val refreshState = loadState.source.refresh
            val isListEmpty =
                loadState.refresh is LoadState.NotLoading && movieAdapter.itemCount == 0
            Log.d("StateUI", "$isListEmpty")
            binding.movieRecyclerview.isVisible = !isListEmpty
            binding.emptyList.isVisible = isListEmpty
            binding.loadStateView.progressBar.isVisible = refreshState is LoadState.Loading
            binding.loadStateView.tryAgainButton.isVisible = refreshState is LoadState.Error
            binding.loadStateView.messageTextView.isVisible = refreshState is LoadState.Error
            handleError(loadState)
        }

    }


    private fun handleError(loadState: CombinedLoadStates) {
        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error

        errorState?.let {
            Toast.makeText(requireContext(), "${it.error}", Toast.LENGTH_LONG).show()
        }
    }


    companion object {
        const val ITEM_SPAN_SIZE = 2
        const val ERROR_LOADING_SPAN_SIZE = 1

        fun newInstance() = MoviesFragment()
    }
}



