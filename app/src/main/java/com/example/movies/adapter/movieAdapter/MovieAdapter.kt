package com.example.movies.adapter.movieAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.movies.databinding.ViewHolderMovieBinding
import com.example.movies.models.MovieItem

class MovieAdapter(private var listener: MovieListener) : ListAdapter<MovieItem, MovieItemViewHolder>(MovieCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder =
        MovieItemViewHolder(ViewHolderMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            listener.onCLickMovie(getItem(position))
        }
    }


}

interface MovieListener {

    fun onCLickMovie(movie: MovieItem)
}