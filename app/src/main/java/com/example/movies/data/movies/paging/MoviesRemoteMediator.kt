package com.example.movies.data.movies.paging

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
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }

            LoadType.PREPEND ->  return MediatorResult.Success(endOfPaginationReached = (true))



            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: START_PAGE
            }

        }

        return try {
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
                remoteKeysDao.insert(keys)
                movieDao.insertMovies(movies)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntityDb>): MovieRemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                remoteKeysDao.getRemoteKeysByMovieId(movie.id)
            }
    }


    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MovieEntityDb>): MovieRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { movieId ->
                remoteKeysDao.getRemoteKeysByMovieId(movieId)
            }
        }
    }


    companion object {
        private const val START_PAGE = 1

    }
}