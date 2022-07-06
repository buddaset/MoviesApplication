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


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntityDb>
    ): MediatorResult {
        return try {
            val pageIndex = when (loadType) {
                LoadType.REFRESH -> START_PAGE

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    Log.d("Mediator", " state last ${state.lastItemOrNull()}")
                    val remoteKey = db.withTransaction {
                        remoteKeysDao.getRemoteKey(POPULAR_MOVIES)
                    }
                    if (remoteKey.nextPageKey == null) return  MediatorResult.Success(endOfPaginationReached = true)

                    remoteKey.nextPageKey
                }
            }


            Log.d("Mediator", "mediator ---- page ---- $pageIndex.  loadType ---- $loadType")


            val movies = loader( pageIndex, state.config.pageSize).getData()
            Log.d("Mediator", "mediator --- count movie --- ${movies.size}")
            val nextPageKey = pageIndex + 1

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDao.clearAllMovie()
                    remoteKeysDao.delete(POPULAR_MOVIES)
                }

                movieDao.insertAllMovie(movies)
                remoteKeysDao.insert(MovieRemoteKeys(POPULAR_MOVIES, nextPageKey))
            }
            MediatorResult.Success(endOfPaginationReached = movies.isEmpty())
        } catch (e: Exception) {
            Log.d("Mediator", "mediator ---- exception block   ---- $e")
            return MediatorResult.Error(e)
        }
    }




    companion object {
        private const val START_PAGE = 1

        private const val POPULAR_MOVIES = "popular_movies"
    }
}