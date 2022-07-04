package com.example.movies.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movies.ClickMovieListener
import com.example.movies.presentation.movies.view.MoviesListFragment
import com.example.movies.R
import com.example.movies.databinding.ActivityMainBinding
import com.example.movies.presentation.moviedetails.view.MovieDetailsFragment

class MainActivity : AppCompatActivity(), ClickMovieListener {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container_fragment, MoviesListFragment.newInstance())
                .commit()


        }
    }

    override fun clickMovie(movieId: Int) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container_fragment, MovieDetailsFragment.newInstance(movieId))
            .commit()
    }

}