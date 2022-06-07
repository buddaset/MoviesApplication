package com.example.movies

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.databinding.FragmentMoviesListBinding
import com.example.movies.models.MovieData
import com.example.movies.ui.BaseFragment
import com.example.movies.ui.ViewModelFactory
import com.example.movies.ui.onTryAgain
import com.example.movies.ui.screen_movieslist.ListMovieViewModel
import com.example.movies.ui.screen_movieslist.movieAdapter.DefaultLoadingStateAdapter
import com.example.movies.ui.screen_movieslist.movieAdapter.MovieAdapter
import com.example.movies.ui.screen_movieslist.movieAdapter.MovieListener
import com.example.movies.ui.screen_movieslist.movieAdapter.TryAgainAction
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch


class MoviesListFragment : BaseFragment(), MovieListener {

    private lateinit var binding: FragmentMoviesListBinding

    private lateinit var movieAdapter: MovieAdapter
    private var listener: ClickMovieListener? = null
    private val viewModel: ListMovieViewModel by viewModels {
        ViewModelFactory(
            (requireActivity().application as App).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.isSubmitButtonEnabled = true

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) queryMovie(query)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) queryMovie(query)
                return true
            }
        })
    }

    private fun queryMovie(query: String) {
        viewModel.setSearchBy(query)


    }


    private fun setupMovieAdapter() {
        movieAdapter = MovieAdapter(this)

        val footerAdapter = DefaultLoadingStateAdapter { movieAdapter.retry() }
        val headerAdapter = DefaultLoadingStateAdapter { movieAdapter.retry() }
        val adapterWithLoadState = movieAdapter.withLoadStateHeaderAndFooter(
            footer = footerAdapter,
            header = headerAdapter
        )
        binding.movieRecyclerview.adapter = adapterWithLoadState
        binding.movieRecyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
//            getLayoutManager(adapterWithLoadState, footerAdapter)

        binding.loadStateView.tryAgainButton.setOnClickListener { movieAdapter.retry() }
        observeMovies()
        observeLoadState()
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

    private fun observeMovies() {
        lifecycleScope.launch {
            viewModel.listMovie.collectLatest { pagingData ->
                movieAdapter.submitData(pagingData)
            }
        }
    }


    override fun onCLickMovie(movie: MovieData) {
        listener?.clickMovie(movie.id)
    }

    companion object {
        const val ITEM_SPAN_SIZE = 2
        const val ERROR_LOADING_SPAN_SIZE = 1

        fun newInstance() = MoviesListFragment()
    }
}


interface ClickMovieListener {
    fun clickMovie(movieId: Int)
}


