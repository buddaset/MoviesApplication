package com.example.movies.data.movies.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.movies.core.util.Result
import com.example.movies.core.util.getData
import com.example.movies.data.core.local.MovieDatabase

import com.example.movies.data.movies.local.model.MovieEntityDb
import com.example.movies.data.movies.local.model.MovieRemoteKeys

typealias MoviePageLoader = suspend (pageIndex: Int, pageSize: Int) -> Result<List<MovieEntityDb>, Throwable>

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator(
    private val loader: MoviePageLoader,
    private val db: MovieDatabase
) : RemoteMediator<Int, MovieEntityDb>() {

    private val movieDao = db.movieDao()
    private val remoteKeysDao = db.movieRemoteKeysDao()


    override suspend fun load(loadType: LoadType, state: PagingState<Int, MovieEntityDb>): MediatorResult {

        val pageIndex: Int = when (loadType) {

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                Log.d("Mediator", "APPEND next page --- ${remoteKeys?.nextKey} ---  prev page -- ${remoteKeys?.prevKey}  ")
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyFirstItem(state)
                Log.d("Mediator", "PREPEND next page --- ${remoteKeys?.nextKey}  prev page --- ${remoteKeys?.prevKey}")
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = (true))

                prevKey
            }

            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                Log.d("Mediator", "REFRESH next page --- ${remoteKeys?.nextKey}  prev page --- ${remoteKeys?.prevKey}")
                remoteKeys?.nextKey?.minus(1) ?: START_PAGE
            }

        }

        return try {
            Log.d("Mediator", "mediator ---- page ---- $pageIndex.  loadType ---- $loadType")
            val movies = loader(pageIndex, state.config.pageSize).getData()
            val endOfPaginationReached = movies.isEmpty()


            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.delete()
                    movieDao.clearAllMovie()
                }
                val prevKey = if (pageIndex == START_PAGE) null else pageIndex - 1
                val nextKey = if (endOfPaginationReached) null else pageIndex + 1
                val keys = movies.map {
                    MovieRemoteKeys(movieId = it.id, nextKey = nextKey, prevKey = prevKey)
                }
                Log.d("Mediator", "keys  $keys")
                remoteKeysDao.insert(keys)
                movieDao.insertAllMovie(movies)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            Log.d("Mediator", "mediator ---- exception block   ---- $e")
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntityDb>): MovieRemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                Log.d("Mediator", " movieId ---- ${movie.id} ---- APPEND ")
                remoteKeysDao.getRemoteKeysByMovieId(movie.id)
            }
    }

    private suspend fun getRemoteKeyFirstItem(state: PagingState<Int, MovieEntityDb>): MovieRemoteKeys? {
        return state.pages.firstOrNull() { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie ->
                Log.d("Mediator", " movieId ---- ${movie.id} ---- PREPEND ")
                remoteKeysDao.getRemoteKeysByMovieId(movie.id)
            }
    }




    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MovieEntityDb>): MovieRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { movieId ->
                Log.d("Mediator", " movieId ---- $movieId  ---- REFRESH ")
                remoteKeysDao.getRemoteKeysByMovieId(movieId)
            }
        }
    }


    companion object {
        private const val START_PAGE = 1

        private const val POPULAR_MOVIES = "popular_movies"
    }
}