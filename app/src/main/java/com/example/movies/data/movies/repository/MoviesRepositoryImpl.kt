package com.example.movies.data.movies.repository

import android.content.Context
import android.util.Log
import androidx.paging.*
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.example.movies.core.dispatchers.IoDispatcher
import com.example.movies.core.util.*
import com.example.movies.data.core.local.MovieDatabase
import com.example.movies.data.movies.local.model.GenreEntityDb
import com.example.movies.data.movies.local.model.MovieEntityDb
import com.example.movies.data.movies.local.model.toDomain
import com.example.movies.data.movies.paging.MoviePageLoader
import com.example.movies.data.movies.paging.MoviesRemoteMediator
import com.example.movies.data.movies.remote.MoviesRemoteDataSource
import com.example.movies.data.movies.remote.model.toEntity
import com.example.movies.data.workers.RefreshMoviesWorker
import com.example.movies.domain.model.Movie
import com.example.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


typealias PagingSourceFactory = () -> PagingSource<Int, MovieEntityDb>

class MoviesRepositoryImpl(
    private val imageUrlAppender: ImageUrlAppender,
    private val dispatcher: IoDispatcher,
    private val movieDatabase: MovieDatabase,
    private val applicationContext: Context,
    private val moviesRemoteDataSource: MoviesRemoteDataSource
) : MoviesRepository {

    override fun getPopularMovies(): Flow<PagingData<Movie>> {
        val pagingSourceFactory = { movieDatabase.movieDao().getPopularMovies() }
        val loader: MoviePageLoader = { pageIndex, pageSize ->
            loadPopularMovies(pageIndex, pageSize)
        }
        return createPagingDataFlow(pagingSourceFactory, loader)
    }

    private suspend fun loadPopularMovies(pageIndex: Int, pageSize: Int): Result<List<MovieEntityDb>, Throwable> =
        moviesRemoteDataSource.loadPopularMovies(pageIndex, pageSize)
            .mapResult { moviesDto ->
                Log.d("MoviesRepositoryImpl", "$moviesDto")
                moviesDto.map { movieDto ->
                    movieDto.toEntity(getGenres(), imageUrlAppender.baseImageUrl)
                }
            }

    @OptIn(ExperimentalPagingApi::class)
    private fun createPagingDataFlow(
        pagingSourceFactory: PagingSourceFactory,
        loader: MoviePageLoader
    ): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = MoviesRemoteMediator(loader = loader, movieDatabase = movieDatabase),
            pagingSourceFactory = pagingSourceFactory
        )
            .flow
            .map { pagingDb -> pagingDb.map { movieEntity -> movieEntity.toDomain() } }


    override suspend fun getMoviesBySearch(query: String): Result<List<Movie>, Throwable> =
        moviesRemoteDataSource.searchMovies(query)
            .mapResult { moviesDto ->
                moviesDto.map { movieDto ->
                    movieDto.toDomain(getGenres(), imageUrlAppender.baseImageUrl)
                }
            }


    private suspend fun getGenres(): List<GenreEntityDb> {
        updateGenres()
        val genres = movieDatabase.genreDao().getAllGenres()
        Log.d("MoviesRepositoryImpl", "$genres")
        return genres
    }

    private suspend fun updateGenres() =
        moviesRemoteDataSource.loadGenres()
            .mapResult { genresGto -> genresGto.map { genreGto -> genreGto.toEntity() } }
            .onSuccess { genresEntity -> movieDatabase.genreDao().insertAll(genresEntity) }


    override fun periodicalBackgroundUpdateMovie() {
        val workManager = WorkManager.getInstance(applicationContext)

        workManager.enqueueUniquePeriodicWork(
            RefreshMoviesWorker.WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            RefreshMoviesWorker.makePeriodicWorkRequest()
        )
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}