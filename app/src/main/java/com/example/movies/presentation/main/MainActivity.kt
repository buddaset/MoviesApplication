package com.example.movies.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.NavigationUI
import com.example.movies.R
import com.example.movies.core.navigation.Navigator
import com.example.movies.core.navigation.NavigatorImpl
import com.example.movies.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

      val navigator: Navigator by lazy { NavigatorImpl(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navigator.navController)



    }
}