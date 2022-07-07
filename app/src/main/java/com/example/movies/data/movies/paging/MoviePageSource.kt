package com.example.movies.data.movies.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.Query
import com.example.movies.core.util.Result
import com.example.movies.data.movies.local.model.MovieEntityDb
import com.example.movies.data.movies.remote.MoviesRemoteDataSource
import com.example.movies.domain.model.Movie

typealias SearchMovieLoader = suspend (query: String, pageIndex: Int) -> Result<List<Movie>, Throwable>

class MoviePageSource(
    private val moviesRemoteDataSource: MoviesRemoteDataSource,
    private val loader: SearchMovieLoader
) : PagingSource<Int, Movie>() {


    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        val pageIndex: Int = params.key ?: 1  // todo check int = 0


        return try {
            val movies = loader(, pageIndex)
            val nextKey = if (movies.size < pageSize) null else pageIndex + 1
            val prevKey = if (pageIndex == 1) null else pageIndex - 1
            LoadResult.Page(movies, prevKey, nextKey)

        } catch (e: Exception) {
            LoadResult.Error(e)
        }


    }
}