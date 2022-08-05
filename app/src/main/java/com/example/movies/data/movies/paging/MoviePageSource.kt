package com.example.movies.data.movies.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movies.data.core.util.Result
import com.example.movies.data.core.util.getData
import com.example.movies.domain.model.Movie

typealias SearchMovieLoader = suspend (pageIndex: Int, pageSize: Int) -> Result<List<Movie>, Throwable>

class MoviePageSource(
    private val loader: SearchMovieLoader,
    private val pageSize: Int
) : PagingSource<Int, Movie>() {


    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        val pageIndex: Int = params.key ?: START_PAGE

        return try {
            val movies = loader( pageIndex, params.loadSize).getData()
            val nextKey = if (movies.size == pageSize) pageIndex + 1 else null
            val prevKey = if (pageIndex == START_PAGE) null else pageIndex - 1
            LoadResult.Page(movies, prevKey, nextKey)

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val START_PAGE = 1
    }
}