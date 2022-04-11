package com.example.movies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movies.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MoviesListFragment.ClickMovieListener {

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

    override fun clickMovie() {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container_fragment, MoviesDetailsFragment.newInstance())
            .commit()
    }


}