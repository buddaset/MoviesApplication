package com.example.movies.ui.screen_movieslist.movieAdapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.movies.R
import com.example.movies.databinding.ViewHolderMovieBinding
import com.example.movies.models.MovieData

class MovieDataViewHolder(private val binding: ViewHolderMovieBinding) : RecyclerView.ViewHolder(binding.root) {

    private val context = itemView.context

    fun bind(movie: MovieData) {

        with(binding) {
            title.text = movie.title
            pgAge.text = context.getString(R.string.pg_age,movie.pgAge)
            val likeRes= if (movie.isLiked) R.drawable.ic_like_on else R.drawable.ic_like_off
            likedMovie.setImageResource(likeRes)
            genre.text = MovieUtils.getGenreOfMovie(movie.genres)
            ratingBar.rating = MovieUtils.getRating(movie.rating)
            countReview.text = context.getString(R.string.reviews, movie.reviewCount)
            //todo correct runningTime
            runningTime.text = context.getString(R.string.movie_minutes, 100)
            Log.d("XXX", "${movie.title}   ->>>    ${movie.imageUrl}")
            moviePoster.load(movie.imageUrl)






        }
    }
}