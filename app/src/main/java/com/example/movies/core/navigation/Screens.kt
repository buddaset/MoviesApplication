package com.example.movies.core.navigation

import androidx.fragment.app.Fragment
import com.example.movies.presentation.screen_moviedetails.MoviesDetailsFragment

interface Screen {

    val tag: String? get() = null

    fun destination(): Fragment


}

class MovieDetailsScreen(val movieId: Long) : Screen {

    override fun destination(): Fragment = MoviesDetailsFragment.newInstance(movieId)



}