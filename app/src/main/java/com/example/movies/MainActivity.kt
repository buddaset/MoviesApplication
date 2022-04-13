package com.example.movies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movies.databinding.ActivityMainBinding
import com.example.movies.models.MovieItem

class MainActivity : AppCompatActivity(), ClickMovieListener {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container_fragment, MoviesListFragment.newInstance() )
                .commit()
        }


    }

    override fun clickMovie(movie: MovieItem) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container_fragment, MoviesDetailsFragment.newInstance(movie))
            .commit()
    }


}