package com.example.movies

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
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
    private lateinit var mainLoadStateHolder: DefaultLoadingStateAdapter.Holder
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

        onTryAgain(binding.root) {
            viewModel.tryAgain()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null)
                    queryMovie(query)
            return    true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null)
                    queryMovie(query)
            return true
            }

        })

    }

    private fun queryMovie(query: String) {
        viewModel.setSearchBy(query)


    }

    private fun scrollStartList() {
        binding.movieRecyclerview.scrollToPosition(0)
    }


    private fun setupMovieAdapter() {
        movieAdapter = MovieAdapter(this)

        val tryAgainAction: TryAgainAction = { movieAdapter.retry() }
        val footerAdapter = DefaultLoadingStateAdapter(tryAgainAction)
        val headerAdapter = DefaultLoadingStateAdapter(tryAgainAction)

        val adapterWithLoadState = movieAdapter.withLoadStateHeaderAndFooter(
            footer = footerAdapter,
            header = headerAdapter
        )

        binding.movieRecyclerview.adapter = adapterWithLoadState
        binding.movieRecyclerview.layoutManager = getLayoutManager(adapterWithLoadState, footerAdapter)
        mainLoadStateHolder = DefaultLoadingStateAdapter.Holder(
            binding.loadStateView,
            binding.swipeRefreshLayout,
            tryAgainAction
        )


        observeMovies()
        observeLoadState()



    }

    private fun  getLayoutManager(concatAdapter: ConcatAdapter, footerAdapter: DefaultLoadingStateAdapter) : RecyclerView.LayoutManager {
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
        lifecycleScope.launch {
            movieAdapter.loadStateFlow.debounce(200).collectLatest { state ->
                mainLoadStateHolder.bind(state.refresh)
            }
        }

    }

    private fun observeMovies(){
      lifecycleScope.launch {
          viewModel.listMovie.collectLatest { pagingData ->
              movieAdapter.submitData(pagingData)
              scrollStartList()
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


