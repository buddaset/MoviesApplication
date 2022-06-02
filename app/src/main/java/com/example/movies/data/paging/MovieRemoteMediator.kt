package com.example.movies.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.movies.data.local.MovieDatabase
import com.example.movies.data.local.entity.MovieEntityDb
import com.example.movies.data.local.entity.MovieRemoteKeys
import com.example.movies.models.MovieData
import androidx.room.withTransaction

typealias MoviePageLoader = suspend (pageIndex: Int, pageSize: Int) -> List<MovieEntityDb>

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val loader: MoviePageLoader,
    private val movieDatabase: MovieDatabase
) : RemoteMediator<Int, MovieEntityDb>() {

    private val movieDao = movieDatabase.movieDao()
    private val remoteKeysDao = movieDatabase.movieRemoteKeysDao()



    override suspend fun load(loadType: LoadType, state: PagingState<Int, MovieEntityDb>): MediatorResult {

            val currentKey = when(loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: START_KEY
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                        ?: return MediatorResult.Success (endOfPaginationReached = remoteKeys != null)
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }
        return try {

            val movies = loader( currentKey, state.config.pageSize)
            val endOfPaginationReached = movies.isEmpty()
            Log.d("Mediator", "End of paging ---- $endOfPaginationReached")


            val prevKey = if( currentKey == START_KEY) null else currentKey - 1
            val nextKey = if (endOfPaginationReached) null else currentKey + 1

            movieDatabase.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        movieDatabase.movieDao().clearAllMovie()
                        remoteKeysDao.deleteAllRemoteKeys()
                    }
                val keys = movies.map { movie ->
                    MovieRemoteKeys(
                        id = movie.id,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                movieDao.insertAllMovie(movies)
                remoteKeysDao.addAllRemoteKeys(keys)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }


    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MovieEntityDb>) :
            MovieRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let {movieId ->
                remoteKeysDao.getRemoteKey(id =movieId)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieEntityDb>) : MovieRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie ->
                remoteKeysDao.getRemoteKey(id = movie.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntityDb>) : MovieRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty()}?.data?.lastOrNull()
            ?.let { movie ->
                remoteKeysDao.getRemoteKey(id = movie.id)
            }
    }

    companion object {
        private const val START_KEY =1
    }
}