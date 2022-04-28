package com.example.movies.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.data.remote.ImageUrlAppender
import com.example.movies.data.remote.MovieService
import com.example.movies.models.ActorData
import com.example.movies.models.GenreData
import com.example.movies.models.MovieData
import com.example.movies.models.MovieDetails
import java.util.*

class MovieRepositoryImpl(private val imageUrlAppender: ImageUrlAppender,
                          private val movieService: MovieService) : MovieRepository {



    override suspend fun getListMovie(): LiveData<List<MovieData>> {
        val resultLiveData = MutableLiveData<List<MovieData>>()
        val genresResponse = movieService.loadGenres().genres
        val response = movieService.loadMoviesPopular().results.map { movie ->
            MovieData(
                id = movie.id,
                title = movie.title,
                pgAge = setPgAge(movie.adult),
                imageUrl = imageUrlAppender.getPosterImageUrl(movie.imagePath),
                rating = movie.rating.toInt(),
                reviewCount = movie.reviewCount,
                storyLine = movie.storyLine,
                isLiked = Random().nextBoolean(),
                genres = genresResponse.filter { genreResponse ->
                    movie.genresId.contains(genreResponse.id) }
                    .map { GenreData(id = it.id , name = it.name) } )
                }
        resultLiveData.value = response
        return resultLiveData
        }



    override suspend fun getMovieDetails(idMovie: Int): LiveData<MovieDetails> {
        val resultLiveData = MutableLiveData<MovieDetails>()
        val movie = movieService.loadMovieDetails(idMovie)
        val movieDetails = MovieDetails(
            id = movie.id,
            title = movie.title,
            pgAge = setPgAge(movie.adult),
            detailImageUrl = imageUrlAppender.getDetailImageUrl(movie.imageDatailPath),
            runningTime = movie.runningTime,
            rating = movie.rating.toInt(),
            reviewCount = movie.reviewCount,
             storyLine = movie.storyLine,
            isLiked= false,
           genres = movie.genres.map { GenreData(id=it.id, name = it.name)},
            actors = getActors(idMovie))
        resultLiveData.value = movieDetails
        return resultLiveData
    }

    private suspend fun getActors(idMovie: Int) : List<ActorData> {
        val actorsMode = movieService.loadMovieCredits(idMovie).cast
                if( actorsMode.isEmpty()) return emptyList<ActorData>()

         return   actorsMode.map { actorResponse ->
            ActorData(
                id = actorResponse.id,
                name = actorResponse.name,
                imageUrl = imageUrlAppender.getActorImageUrl(actorResponse.imageActorPath ))
        }

    }

    private fun setPgAge(isAdult : Boolean) : Int =
       if (isAdult) PG_ADULT else PG_CHILDREN

    companion object {

        const val PG_ADULT = 16
        const val PG_CHILDREN = 13
    }
}