package com.example.movies.core.navigation

import androidx.fragment.app.Fragment
import com.example.movies.presentation.moviedetails.view.MovieDetailsFragment
import com.example.movies.presentation.movies.view.MoviesFragment

interface Screen {

    val tag: String? get() = null

    fun destination(): Fragment
}


class MovieDetailsScreen(val movieId: Long) : Screen {

    override fun destination(): Fragment = MovieDetailsFragment.newInstance(movieId)
}


class MoviesScreen() : Screen {

    override fun destination(): Fragment = MoviesFragment.newInstance()
}




