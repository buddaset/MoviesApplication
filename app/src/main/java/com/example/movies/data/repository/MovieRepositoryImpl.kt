package com.example.movies.data.repository

import android.content.Context
import androidx.paging.*
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.example.movies.core.dispatchers.IoDispatcher
import com.example.movies.core.utils.*
import com.example.movies.data.Result
import com.example.movies.data.local.MovieDatabase

import com.example.movies.data.local.entity.GenreEntityDb
import com.example.movies.data.local.entity.MovieEntityDb
import com.example.movies.data.local.entity.toDomain
import com.example.movies.data.mapResult
import com.example.movies.data.onSuccess

import com.example.movies.data.paging.MoviePageLoader
import com.example.movies.data.paging.MovieRemoteMediator
import com.example.movies.data.remote.MovieApi
import com.example.movies.data.remote.MoviesRemoteDataSource
import com.example.movies.data.remote.model.toEntity
import com.example.movies.data.workers.RefreshMoviesWorker
import com.example.movies.domain.model.Actor
import com.example.movies.domain.model.Movie
import com.example.movies.domain.model.MovieDetails
import com.example.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


typealias PagingSourceFactory = () -> PagingSource<Int, MovieEntityDb>

class MovieRepositoryImpl(
    private val imageUrlAppender: ImageUrlAppender,
    private val dispatcher: IoDispatcher,
    private val movieDatabase: MovieDatabase,
    private val applicationContext: Context,
    private val moviesRemoteDataSource: MoviesRemoteDataSource
) : MovieRepository {

    override fun getPopularMovies(): Flow<PagingData<Movie>> {
        val pagingSourceFactory = { movieDatabase.movieDao().getPopularMovies() }
        val loader: MoviePageLoader = { pageIndex, pageSize ->
            loadPopularMovies(pageIndex, pageSize)
        }
        return createPagingDataFlow(pagingSourceFactory, loader)
    }

    private suspend fun loadPopularMovies(
        pageIndex: Int, pageSize: Int
    ): Result<List<MovieEntityDb>, Throwable> =
        moviesRemoteDataSource.loadPopularMovies(pageIndex, pageSize)
            .mapResult { moviesDto -> moviesDto.map { movieDto ->
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
            remoteMediator = MovieRemoteMediator(loader = loader, movieDatabase = movieDatabase),
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
        return movieDatabase.genreDao().getAllGenres()
    }

    private suspend fun updateGenres() =
        moviesRemoteDataSource.loadGenres()
            .mapResult { genresGto -> genresGto.map { genreGto -> genreGto.toEntity() } }
            .onSuccess { genresEntity -> movieDatabase.genreDao().insertAll(genresEntity) }


    override suspend fun getMovieDetails(movieId: Int): Flow<MovieDetails> {
        updateDataMovieDetails(movieId)
        return movieDatabase.movieDetailDao().getMovieById(movieId)
            .map { it.toMovieDetail() }
    }

    private suspend fun updateDataMovieDetails(idMovie: Int) {
        moviesRemoteDataSource.loadMovieDetails(idMovie)
            .mapResult { movieDto -> movieDto.toEntity() }
            .onSuccess { movieEntity -> movieDatabase.movieDetailDao().insertMovie(movieEntity) }
    }


    override suspend fun getActorsMovie(movieId: Int): Flow<List<Actor>> {
        updateActors(movieId)
        return movieDatabase.actorDao().getActorsByMovieId(movieId)
            .map { actorsEntity -> actorsEntity.map { actorEntity -> actorEntity.toDomain() } }
    }

    private suspend fun updateActors(movieId: Int) =
        moviesRemoteDataSource.loadMovieActors(movieId)
            .mapResult { listDto -> listDto.map { actorDto -> actorDto.toEntity(movieId) } }
            .onSuccess { actorsEntity -> movieDatabase.actorDao().insertAllActors(actorsEntity) }


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