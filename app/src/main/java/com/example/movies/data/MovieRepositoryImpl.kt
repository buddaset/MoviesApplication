package com.example.movies.data

import android.util.Log
import androidx.paging.*
import com.example.movies.data.dispatchers.IoDispatcher
import com.example.movies.data.local.MovieDatabase
import com.example.movies.data.local.entity.ActorEntityDb
import com.example.movies.data.local.entity.GenreEntityDb
import com.example.movies.data.local.entity.MovieEntityDb
import com.example.movies.data.local.entity.toMovieData
import com.example.movies.data.paging.MoviePageLoader
import com.example.movies.data.paging.MovieRemoteMediator
import com.example.movies.data.remote.MovieService
import com.example.movies.data.remote.response.MovieDetailsResponse
import com.example.movies.data.remote.response.toGenreEntityDb
import com.example.movies.data.utils.*
import com.example.movies.models.ActorData
import com.example.movies.models.MovieData
import com.example.movies.models.MovieDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(
    private val imageUrlAppender: ImageUrlAppender,
    private val movieService: MovieService,
    private val dispatcher: IoDispatcher,
    private val movieDatabase: MovieDatabase
) : MovieRepository {

     @OptIn(ExperimentalPagingApi::class)
     override fun searchMovie(query: String): Flow<PagingData<MovieData>> {
         val pagingSourceFactory = { movieDatabase.movieDao().getAllMovies() }

         val loader: MoviePageLoader = { pageIndex, pageSize ->
             loadListMovies(pageIndex, pageSize, query)
         }
         return Pager(
             config = PagingConfig(
                 pageSize = PAGE_SIZE,
                 enablePlaceholders = false),
             remoteMediator = MovieRemoteMediator(loader = loader, movieDatabase = movieDatabase),
             pagingSourceFactory =  pagingSourceFactory
         ).flow
             .map { paging ->
                 paging.map {
                     it.toMovieData()
                 }
             }

     }




    private suspend fun loadListMovies(pageIndex: Int, pageSize: Int, query: String): List<MovieEntityDb> =
        withContext(dispatcher.value) {

        val moviesResponse = if(query.isBlank())
            movieService.loadMoviesPopular(pageIndex, pageSize).results
            else movieService.searchMovie(query, pageIndex,  pageSize).results

        val genres = getGenres()
        val moviesEntityDb = moviesResponse.map { it.toMovieEntityDb(genres) }
        moviesEntityDb.forEach { it.imageUrl = imageUrlAppender.getPosterImageUrl(it.imageUrl) }
        return@withContext moviesEntityDb
    }


    private suspend fun getGenres(): List<GenreEntityDb> {
        var genres= movieDatabase.genreDao().getAllGenres()
        if (genres.isEmpty()) {
           genres =  loadGenres()
            movieDatabase.genreDao().insertAll(genres)
        }
        return genres
    }


    private suspend fun loadGenres(): List<GenreEntityDb> =
        movieService.loadGenres().genres.map { it.toGenreEntityDb() }


    override  fun getMovieDetails(idMovie: Int): Flow<Result<MovieDetails>> = flow {
        emit(getMovieDetailsFromDb(idMovie))
        emit(loadMovieDetails(idMovie))
    }
        .flowOn(dispatcher.value)


    private suspend fun getMovieDetailsFromDb(idMovie: Int) : Result<MovieDetails> {
        return try {
            Result.Success(movieDatabase.movieDetailDao().getMovieById(idMovie).toMovieDetail())
        } catch (ex: NullPointerException) {
            loadMovieDetails(idMovie)
        }
    }


    private suspend fun loadMovieDetails(idMovie: Int): Result<MovieDetails> {
        val movieDetailsResponse = try {

            movieService.loadMovieDetails(idMovie)
        } catch (ex: Exception) {
            return Result.Error(error = ex)
        }
        cacheMovieDetails(movieDetailsResponse)
        val movieDetails = transformToMovieDetails(movieDetailsResponse)
        return Result.Success(data = movieDetails)
    }

    private suspend fun cacheMovieDetails(movieResponse: MovieDetailsResponse) {
        movieDatabase.movieDetailDao().insertMovie(movieResponse.toMovieDetailEntityDb())
    }


    private suspend fun transformToMovieDetails(movie: MovieDetailsResponse): MovieDetails  =
        movie.toMovieDetails().also {
            it.detailImageUrl = imageUrlAppender.getDetailImageUrl(it.detailImageUrl)
    }


    override  fun getActorsMovie(idMovie: Int): Flow<Result<List<ActorData>>> = flow {
        emit(getActorsFromDb(idMovie))
        emit(loadActorsMovie(idMovie))
    }
        .flowOn(dispatcher.value)

    private suspend fun getActorsFromDb(idMovie: Int) : Result<List<ActorData>> {
        val actorsEntityDb = movieDatabase.actorDao().getActorsByMovieId(idMovie)
        if (actorsEntityDb.isEmpty()) return loadActorsMovie(idMovie)
        return Result.Success(data = actorsEntityDb.map { it.toActorData() })
    }


    private suspend fun loadActorsMovie(idMovie: Int): Result<List<ActorData>> {

        val actorsResponse = try {
            movieService.loadMovieCredits(idMovie).cast
        } catch (ex: Exception) {
            return Result.Error(error = ex)
        }
        actorsResponse.forEach {
            it.imageActorPath = imageUrlAppender.getActorImageUrl(it.imageActorPath)
        }
        cacheActors(actorsResponse.map { it.toActorEntityDb(idMovie) })
        val actorsData = actorsResponse.map { it.toActorData() }
        return Result.Success(data = actorsData)
    }

    private suspend fun cacheActors(actorsEntityDb: List<ActorEntityDb>) {
        movieDatabase.actorDao().insertAllActors(actorsEntityDb)
    }

    companion object {
        private const val PAGE_SIZE = 20
    }


}