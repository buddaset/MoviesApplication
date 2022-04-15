package com.example.movies.ui.screenMoviesList.movieAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.movies.databinding.ViewHolderMovieBinding
import com.example.movies.models.MovieData

class MovieAdapter(private var listener: MovieListener) : ListAdapter<MovieData, MovieDataViewHolder>(MovieCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDataViewHolder =
        MovieDataViewHolder(ViewHolderMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))

    override fun onBindViewHolder(holder: MovieDataViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            listener.onCLickMovie(getItem(position))
        }
    }


}

interface MovieListener {

    fun onCLickMovie(movie: MovieData)
}