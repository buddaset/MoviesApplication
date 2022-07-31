package com.example.movies.presentation.movies.view.movieAdapter

import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.databinding.ViewHolderMovieBinding
import com.example.movies.domain.model.Movie

class MovieViewHolder(private val binding: ViewHolderMovieBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val context = itemView.context

    fun bind(movie: Movie, listener: MovieClickListener) {

        with(binding) {
            title.text = movie.title
            pgAge.text = context.getString(R.string.pg_age, movie.pgAge)
            val likeRes = if (movie.isLiked) R.drawable.ic_like_on else R.drawable.ic_like_off
            likedMovie.setImageResource(likeRes)
         //todo utils move to viewModel
            genre.text = MovieUtils.getGenreOfMovie(movie.genres)
            ratingBar.rating = MovieUtils.getRating(movie.rating)
            countReview.text = context.getString(R.string.reviews, movie.reviewCount)
            //todo correct runningTime
            runningTime.text = context.getString(R.string.movie_minutes, 100)
            Glide
                .with(context)
                .load(movie.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.moviePoster)

            itemView.setOnClickListener {   listener.onClickMovie(movie) }
            likedMovie.setOnClickListener { listener.onClickFavorite(!movie.isLiked) } // change like and unlike movie state

        }
    }
}