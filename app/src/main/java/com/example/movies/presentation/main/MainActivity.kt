package com.example.movies.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.movies.R
import com.example.movies.core.navigation.MoviesScreen
import com.example.movies.core.navigation.Navigator
import com.example.movies.core.navigation.NavigatorImpl

class MainActivity : AppCompatActivity() {

      val navigator: Navigator by lazy { NavigatorImpl(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            navigator.navigateTo(screen = MoviesScreen(), addToBackStack = false)

    }
}