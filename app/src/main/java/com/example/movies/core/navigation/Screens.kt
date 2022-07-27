package com.example.movies.core.navigation

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import com.example.movies.R
import com.example.movies.presentation.moviedetails.view.MovieDetailsFragment
import com.example.movies.presentation.moviedetails.view.MovieDetailsFragmentArgs
import com.example.movies.presentation.movies.view.MoviesFragment

interface Screen {

    val tag: String? get() = null

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




