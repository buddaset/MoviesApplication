package com.example.movies.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movies.data.dispatchers.IoDispatcher
import com.example.movies.data.remote.MoviePageLoader
import com.example.movies.data.remote.MoviePageSource
import com.example.movies.data.remote.MovieService
import com.example.movies.data.remote.response.MovieDetailsResponse
import com.example.movies.data.utils.ImageUrlAppender
import com.example.movies.data.utils.toActorData
import com.example.movies.data.utils.toMovieData
import com.example.movies.models.ActorData
import com.example.movies.models.GenreData
import com.example.movies.models.MovieData
import com.example.movies.models.MovieDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(
    private val imageUrlAppender: ImageUrlAppender,
    private val movieService: MovieService,
    private val dispatcher: IoDispatcher
) : MovieRepository {






    override fun searchMovie(query: String): Flow<PagingData<MovieData>> {
        val loader: MoviePageLoader = { pageIndex, pageSize ->
            loadListMovies(pageIndex,pageSize, query)
        }
        return Pager(
            config = PagingConfig( pageSize = PAGE_SIZE, enablePlaceholders = false),

            pagingSourceFactory = { MoviePageSource(loader) }
        ).flow

    }



    private suspend fun loadListMovies(pageIndex: Int, pageSize: Int, query: String)
    : List<MovieData> = withContext(dispatcher.value){
       val moviesResponse =  if(query.isBlank())  movieService.loadMoviesPopular(pageIndex,pageSize).results
        else movieService.searchMovie(query,pageIndex, pageSize).results

        val genres = loadGenres()
         val moviesData = moviesResponse.map { it.toMovieData(genres) }
         moviesData.forEach { it.imageUrl = imageUrlAppender.getPosterImageUrl(it.imageUrl)}
        return@withContext moviesData

        }

    private suspend fun loadGenres(): List<GenreData> =
        movieService.loadGenres().genres
            .map { GenreData(id = it.id, name = it.name)}







    override suspend fun getMovieDetails(idMovie: Int): Flow<Result<MovieDetails>> = flow {
        emit(loadMovieDetails(idMovie))
    }
        .flowOn(dispatcher.value)



    private suspend fun loadMovieDetails(idMovie: Int) : Result<MovieDetails> {
        val movieDetailsResponse = try {
            movieService.loadMovieDetails(idMovie)
        } catch (ex: Exception) {
            return Result.Error(error = ex)
        }
        val movieDetails = transformToMovieDetails(movieDetailsResponse)
        return Result.Success(data = movieDetails)
    }


    private suspend fun transformToMovieDetails(movie: MovieDetailsResponse): MovieDetails =
    movie.toMovieData().also {
        it.detailImageUrl = imageUrlAppender.getDetailImageUrl(it.detailImageUrl)
    }



    override  suspend fun getActorsMovie(idMovie: Int): Flow<Result<List<ActorData>>> = flow {
        emit(loadActorsMovie(idMovie))
    }
        .flowOn(dispatcher.value)



   private  suspend fun loadActorsMovie(idMovie: Int): Result<List<ActorData>> {

       val actorsResponse = try {
           movieService.loadMovieCredits(idMovie).cast
       } catch (ex: Exception) {
           return Result.Error(error = ex)
       }
       actorsResponse.forEach { it.imageActorPath = imageUrlAppender.getActorImageUrl(it.imageActorPath) }
       val actorsData = actorsResponse.map { it.toActorData() }
       return Result.Success(data = actorsData)


    }

    companion object {
        private const val PAGE_SIZE = 10
    }



}