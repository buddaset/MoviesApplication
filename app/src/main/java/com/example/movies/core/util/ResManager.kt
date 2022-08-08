package com.example.movies.core.util

import android.content.Context
import androidx.annotation.StringRes


//todo add graf DI and add viewModel
interface Resources{

   fun  getRes(resId: Int): String
}

class SimpleTextRecourses(private val context: Context): Resources {

    override fun getRes(@StringRes resId: Int) : String =
        context.getString(resId)


}