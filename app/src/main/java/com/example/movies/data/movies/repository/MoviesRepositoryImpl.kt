package com.example.movies.data.movies.repository

import android.content.Context
import androidx.paging.*
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.example.movies.core.util.MovieUrlProvider
import com.example.movies.core.dispatchers.Dispatcher
import com.example.movies.data.core.local.MovieDatabase
import com.example.movies.data.core.util.*
import com.example.movies.data.favoritemovies.local.model.FavoriteIdEntity
import com.example.movies.data.movies.local.model.GenreEntity
import com.example.movies.data.movies.local.model.MovieEntity
import com.example.movies.data.movies.paging.MoviePageLoader
import com.example.movies.data.movies.paging.MoviePageSource
import com.example.movies.data.movies.paging.MoviesRemoteMediator
import com.example.movies.data.movies.paging.SearchMovieLoader
import com.example.movies.data.movies.remote.MoviesRemoteDataSource
import com.example.movies.data.workers.RefreshMoviesWorker
import com.example.movies.domain.model.Movie
import com.example.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class MoviesRepositoryImpl(
    private val urlProvider: MovieUrlProvider,
    private val dispatcher: Dispatcher,
    private val movieDatabase: MovieDatabase,
    private val applicationContext: Context,
    private val moviesRemoteDataSource: MoviesRemoteDataSource
) : MoviesRepository {

    private val movieDao = movieDatabase.movieDao()
    private val favoriteDao = movieDatabase.favoriteDao()


    @OptIn(ExperimentalPagingApi::class)
    override fun getPopularMovies(): Flow<PagingData<Movie>> {
        val pagingSourceFactory = { movieDatabase.movieDao().getPopularMovies() }
        val loader: MoviePageLoader = { pageIndex, pageSize ->
            loadPopularMovies(pageIndex, pageSize)
        }
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = MoviesRemoteMediator(loader = loader, db = movieDatabase),
            pagingSourceFactory = pagingSourceFactory
        )
            .flow
            .map { pagingDb -> pagingDb.map { movieEntity -> movieEntity.toDomain() } }
    }

    private suspend fun loadPopularMovies(
        pageIndex: Int,
        pageSize: Int
    ): Result<List<MovieEntity>, Throwable> = withContext(dispatcher.value) {
        moviesRemoteDataSource.loadPopularMovies(pageIndex, pageSize)
            .mapResult { moviesDto -> moviesDto.map { movieDto ->
                movieDto.toEntity(getGenres(), urlProvider.baseImageUrl)
                }
            }
    }


    override fun getMoviesBySearch(query: String): Flow<PagingData<Movie>> {
        val loader: SearchMovieLoader = { pageIndex, pageSize ->
            loadMoviesBySearch(query, pageIndex, pageSize)
        }
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { MoviePageSource(loader, PAGE_SIZE) }
        ).flow
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> =
        movieDao.getFavoriteMovies()
            .map { moviesEntity -> moviesEntity.map { movieEntity -> movieEntity.toDomain() } }


    override fun getFavoriteIds(): Flow<List<Long>> =
        favoriteDao.getFavoriteIds()


    override suspend fun changeFavoriteFlagMovie(movie: Movie, isFavorite: Boolean) {
        if (isFavorite) {
            favoriteDao.insert(FavoriteIdEntity(movieId = movie.id))
            cacheMovie(movie)
        } else favoriteDao.delete(FavoriteIdEntity(movieId = movie.id))
    }

    private suspend fun cacheMovie(movie: Movie) =
        movieDao.insertMovie(movie.toEntity())


    private suspend fun loadMoviesBySearch(
        query: String,
        pageIndex: Int,
        pageSize: Int
    ): Result<List<Movie>, Throwable> = withContext(dispatcher.value) {
        moviesRemoteDataSource.searchMovies(query, pageIndex, pageSize)
            .mapResult { moviesDto ->
                moviesDto.map { movieDto ->
                    movieDto.toDomain(getGenres(), urlProvider.baseImageUrl)
                }
            }
    }


    private suspend fun getGenres(): List<GenreEntity> {
        val genres = movieDatabase.genreDao().getAllGenres()
        if (genres.isEmpty())
            return updateGenres()
        return genres
    }

    private suspend fun updateGenres(): List<GenreEntity> =
        moviesRemoteDataSource.loadGenres()
            .mapResult { genresGto -> genresGto.map { genreGto -> genreGto.toEntity() } }
            .onSuccess { genresEntity -> movieDatabase.genreDao().insertAll(genresEntity) }
            .getData()


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