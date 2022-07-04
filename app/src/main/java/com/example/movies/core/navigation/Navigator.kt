package com.example.movies.core.navigation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.movies.R


interface Navigator {

    fun navigateTo(screen: Screen, addToBackStack: Boolean = true)

    fun backTo(tag: String)

    fun back()
}

class NavigatorImpl(private val context: Context) : Navigator {

    override fun navigateTo(screen: Screen, backStack: Boolean) {
        getFragmentManager().commit {
            replace(R.id.container, screen.destination(), screen.tag)
            if (backStack)
                addToBackStack(screen.tag)
        }
    }

    override fun backTo(tag: String) {
        getFragmentManager().popBackStack(tag, 0)
    }

    override fun back() {
        getFragmentManager().popBackStackImmediate()
    }


    private fun getFragmentManager() = (context as AppCompatActivity).supportFragmentManager

}
