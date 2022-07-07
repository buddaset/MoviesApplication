package com.example.movies.presentation.movies.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.*
import com.example.disneyperson.core.delegate.viewBinding
import com.example.movies.R
import com.example.movies.core.application.App
import com.example.movies.core.navigation.MovieDetailsScreen
import com.example.movies.core.navigation.Navigator
import com.example.movies.databinding.FragmentMoviesBinding
import com.example.movies.di.ViewModelFactory
import com.example.movies.presentation.main.MainActivity
import com.example.movies.presentation.movies.view.movieAdapter.DefaultLoadingStateAdapter
import com.example.movies.presentation.movies.view.movieAdapter.MovieAdapter
import com.example.movies.presentation.movies.viewmodel.MoviesViewModel
import com.example.movies.presentation.util.collectPagingFlow
import com.example.movies.presentation.util.onTextChange
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private val binding: FragmentMoviesBinding by viewBinding()
    lateinit var navigator: Navigator

    private val viewModel: MoviesViewModel by viewModels {
        ViewModelFactory(
            (requireActivity().application as App).useCase.getPopularMoviesUseCase(),
            (requireActivity().application as App).useCase.getMoviesBySearchUseCase()
        )
    }

    private val movieAdapter = MovieAdapter { movie ->
        navigator.navigateTo(MovieDetailsScreen(movie.id))

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            navigator = context.navigator
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMovieAdapter()
        setupRefreshLayout()
        observeMovies()

        observeLoadState2()
        observeLoadState()
        initSwipeToRefresh()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.onTextChange(::queryMovie)
    }

    private fun queryMovie(query: String) = viewModel.setSearchBy(query)

    private fun setupMovieAdapter() {



        binding.movieRecyclerview.adapter = movieAdapter
            .withLoadStateFooter(DefaultLoadingStateAdapter { movieAdapter.retry()})
        binding.movieRecyclerview.itemAnimator = null
        binding.movieRecyclerview.layoutManager = GridLayoutManager(requireContext(), 2)

    }

    private fun setupRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            movieAdapter.refresh()
        }
    }

    private fun observeMovies() {
        collectPagingFlow(viewModel.movies, movieAdapter::submitData)
    }

    private fun initSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener { movieAdapter.refresh() }
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

    private fun observeLoadState2() {

        movieAdapter.loadStateFlow
            .debounce(300)
            .onEach { updateState(it.refresh) }
            .launchIn(lifecycleScope)
    }


    private fun updateState(loadState: LoadState) = with(binding) {
        swipeRefresh.isRefreshing = loadState is LoadState.Loading
    }


    private fun observeLoadState() {


        movieAdapter.addLoadStateListener { loadState ->

            val refreshState = loadState.mediator?.refresh
            Log.d("LoadState", "loadState --- $refreshState")

            val isListEmpty =
                loadState.refresh is LoadState.NotLoading && movieAdapter.itemCount == 0
            binding.movieRecyclerview.isVisible = !isListEmpty
            binding.emptyList.isVisible = isListEmpty
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



