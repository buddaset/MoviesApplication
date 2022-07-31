package com.example.movies.presentation.movies.view.movieAdapter

import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.databinding.ViewHolderMovieBinding
import com.example.movies.presentation.core.model.MovieUI

class MovieViewHolder(private val binding: ViewHolderMovieBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val context = itemView.context

    fun bind(movieUI: MovieUI, listener: MovieClickListener) =with(binding) {
            title.text = movieUI.movie.title
            pgAge.text = context.getString(R.string.pg_age, movieUI.movie.pgAge)
            val isFavorite = if (movieUI.isFavorite) R.drawable.ic_like_on else R.drawable.ic_like_off
            likedMovie.setImageResource(isFavorite)
         //todo utils move to viewModel
            genre.text = MovieUtils.getGenreOfMovie(movieUI.genres)
            ratingBar.rating = MovieUtils.getRating(movieUI.rating)
            countReview.text = context.getString(R.string.reviews, movieUI.reviewCount)
            //todo correct runningTime
            runningTime.text = context.getString(R.string.movie_minutes, 100)
            Glide
                .with(context)
                .load(movieUI.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.moviePoster)

            itemView.setOnClickListener {   listener.onClickMovie(movieUI) }
            likedMovie.setOnClickListener { listener.onClickFavorite(!movieUI.isLiked) } // change like and unlike movie state

        
    }
}