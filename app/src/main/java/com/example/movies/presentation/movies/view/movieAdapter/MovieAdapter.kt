package com.example.movies.presentation.movies.view.movieAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.movies.databinding.ViewHolderMovieBinding
import com.example.movies.domain.model.Movie

class MovieAdapter(private var onMovieClick: (Movie) -> Unit) : PagingDataAdapter<Movie, MovieViewHolder>(MovieCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(ViewHolderMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position) ?: return
        holder.bind(movie, onMovieClick)
    }
}


private class MovieCallback: DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldData: Movie, newData: Movie): Boolean =
        oldData.id == newData.id

    override fun areContentsTheSame(oldData: Movie, newData: Movie): Boolean =
        oldData == newData

}
