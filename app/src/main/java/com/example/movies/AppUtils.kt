package com.example.movies

import android.content.Context
import com.example.movies.models.Actor
import com.example.movies.models.MovieItem

class AppUtils(private val context: Context) {


    val fakeData: List<MovieItem> = listOf(
        MovieItem("Avengers: End Game", "13+", R.drawable.movie_avengers_endgame,false, 5, 128, 132, context.getString(R.string.overview), listOf(Actor("Robert Downey Jr.", R.drawable.robert_downey_jr) , Actor("Chris Evans", R.drawable.chris_evans) , Actor("Marc Ruffalo", R.drawable.mark_ruffalo) , Actor("Chris Hemsworth", R.drawable.chris_hemsworth))),
        MovieItem("Tenet", "16+",R.drawable.movie_tonet, true, 5, 98, 97, "Example overview movie. Fake data. Soon.", listOf(Actor("Actor1", R.drawable.holder_place) ,Actor("Actor2", R.drawable.holder_place) , Actor("Actor3", R.drawable.holder_place), Actor("Actor4", R.drawable.holder_place))),
        MovieItem("Black Widow", "13+",R.drawable.movie_black_widow, false, 10, 38, 102, "Example overview movie. Fake data. Soon.", listOf(Actor("Actor1", R.drawable.holder_place) ,Actor("Actor2", R.drawable.holder_place) , Actor("Actor3", R.drawable.holder_place), Actor("Actor4", R.drawable.holder_place))),
        MovieItem("Wonder Woman 1984", "13+",R.drawable.movie_wonder_woman, false, 3, 74, 122, "Example overview movie. Fake data. Soon.", listOf(Actor("Actor1", R.drawable.holder_place) ,Actor("Actor2", R.drawable.holder_place) , Actor("Actor3", R.drawable.holder_place), Actor("Actor4", R.drawable.holder_place))),

    )
}



        /*


          val name: String,
    val pg : String,
    var isFavorite : Boolean = false,
    val ratingStars : Int,
    val countReviews : Int,
    val duration: Int,
    val overview: String
    val actors : List<Actor>



         */