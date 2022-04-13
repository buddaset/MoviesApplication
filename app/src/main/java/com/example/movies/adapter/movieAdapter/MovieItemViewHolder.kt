package com.example.movies.adapter.movieAdapter

import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.ViewHolderMovieBinding
import com.example.movies.models.MovieItem

class MovieItemViewHolder(private val binding: ViewHolderMovieBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: MovieItem) {

        with(binding) {
            moviePoster.setImageResource(movie.poster)
            moviePg.text = movie.pg
            countReview.text= itemView.context.getString(R.string.reviews, movie.countReviews)
            nameMovie.text = movie.name
            durationMovie.text = itemView.context.getString(R.string.movie_minutes, movie.duration)




        }
    }
}