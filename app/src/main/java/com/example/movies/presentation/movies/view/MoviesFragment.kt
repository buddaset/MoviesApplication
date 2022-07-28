package com.example.movies.presentation.movies.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
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
import com.example.movies.presentation.util.hideKeyboard
import com.example.movies.presentation.util.onTextChange
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class MoviesFragment : Fragment(R.layout.fragment_movies), MenuProvider {

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabMenu()
        setupMovieAdapter()
        setupRefreshLayout()
        observeMovies()
        observeLoadState()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.onTextChange(::queryMovie)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false



    private fun setupTabMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }



    private fun queryMovie(query: String) = viewModel.setSearchBy(query)

    private fun setupMovieAdapter() = with(binding) {
        movieRecyclerview.adapter = movieAdapter
            .withLoadStateFooter(DefaultLoadingStateAdapter { movieAdapter.retry() })
        movieRecyclerview.itemAnimator = null
        movieRecyclerview.layoutManager = GridLayoutManager(requireContext(), 2)

        movieRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_DRAGGING)
                    recyclerView.hideKeyboard()
            }
        })
    }

    private fun setupRefreshLayout() =
        binding.swipeRefresh.setOnRefreshListener { movieAdapter.refresh() }

    private fun observeMovies() =
        collectPagingFlow(viewModel.movies, movieAdapter::submitData)

    @OptIn(FlowPreview::class)
    private fun observeLoadState() =
        movieAdapter.loadStateFlow
            .debounce(DEBOUNCE_UPDATE_STATE_MILLIS)
            .onEach {
                updateState(it)
                Log.d("StateMov", "compinestate ---- $it")
            }
            .catch { handleError(it) }
            .launchIn(lifecycleScope)

    private fun updateState(combinedLoadState: CombinedLoadStates) = with(binding) {
        val loadState = combinedLoadState.mediator?.refresh
        val isListEmpty = (loadState is LoadState.NotLoading || loadState == null)
                && movieAdapter.itemCount == 0

        movieRecyclerview.isVisible = !isListEmpty
        emptyList.isVisible = isListEmpty
        swipeRefresh.isRefreshing = loadState is LoadState.Loading

        if (combinedLoadState.refresh is LoadState.Error)
            handleError((combinedLoadState.refresh as LoadState.Error).error)
    }

    private fun handleError(error: Throwable) =
        Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()


    companion object {
        const val DEBOUNCE_UPDATE_STATE_MILLIS = 300L

    }


}



