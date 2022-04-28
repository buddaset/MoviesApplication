package com.example.movies.data

import com.example.movies.data.dispatchers.IoDispatcher
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

class MovieRepositoryImpl(
    private val imageUrlAppender: ImageUrlAppender,
    private val movieService: MovieService,
    private val dispatcher: IoDispatcher
) : MovieRepository {




    override suspend fun getListMovies(): Flow<Result<List<MovieData>>> = flow{
        emit(loadListMovies())
    }
        .flowOn(dispatcher.value)



    private suspend fun loadListMovies() : Result<List<MovieData>> {
        val moviesResponse = try {
             movieService.loadMoviesPopular().results
        } catch (ex: Exception) {
            return Result.Error(error = ex)
        }
        val genres = loadGenres()
         val moviesData = moviesResponse.map { it.toMovieData(genres) }
         moviesData.forEach { it.imageUrl = imageUrlAppender.getPosterImageUrl(it.imageUrl)}
            return  Result.Success(data = moviesData)

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



}