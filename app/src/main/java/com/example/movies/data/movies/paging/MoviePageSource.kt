package com.example.movies.data.movies.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.Query
import com.example.movies.core.util.Result
import com.example.movies.core.util.getData
import com.example.movies.data.movies.local.model.MovieEntityDb
import com.example.movies.data.movies.remote.MoviesRemoteDataSource
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

        val pageIndex: Int = params.key ?: 1  // todo check int = 0



        return try {
            val movies = loader( pageIndex, params.loadSize).getData()
            Log.d("SearchMov", "movies---- $movies")
            Log.d("SearchMov", "page---- $pageIndex  pageSize ---- ${params.loadSize}")
            val nextKey = if (movies.size == pageSize) pageIndex + 1 else null
            val prevKey = if (pageIndex == 1) null else pageIndex - 1
            Log.d("SearchMov", "next key ---- $nextKey   prev key ---- $prevKey")
            LoadResult.Page(movies, prevKey, nextKey)

        } catch (e: Exception) {
            Log.d("SearchMov", " catch block ---- $e")
            LoadResult.Error(e)
        }


    }
}