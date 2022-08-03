package com.example.movies.core.navigation

import android.os.Bundle
import com.example.movies.R
import com.example.movies.presentation.moviedetails.view.MovieDetailsFragment

interface Screen {



    fun destination(): Int

    fun args(): Bundle? = null


}


class MovieDetailsScreen(val movieId: Long) : Screen {

    override fun destination(): Int = R.id.movieDetailsFragment

    override fun args(): Bundle = MovieDetailsFragment.args(movieId)

}


class MoviesScreen() : Screen {

    override fun destination(): Int = R.id.moviesFragment
}




