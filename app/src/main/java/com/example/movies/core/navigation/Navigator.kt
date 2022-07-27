package com.example.movies.core.navigation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.movies.R


interface Navigator {

    val navController: NavController

    fun navigateTo(screen: Screen, addToBackStack: Boolean = true)

    fun backTo(screen: Screen)

    fun back()
}

class NavigatorImpl(private val context: Context) : Navigator {

    override val navController: NavController
    get() = navController()

    override fun navigateTo(screen: Screen, addToBackStack: Boolean) {
        navController().navigate(screen.destination(), screen.args())
    }




    override fun backTo(screen: Screen) {
        navController().popBackStack(screen.destination(), inclusive = false)
    }

    override fun back() {
        navController().popBackStack()
    }




    private fun navController(): NavController {
        val navHost =
            (context as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.container_fragment) as NavHostFragment
        return navHost.navController
    }

}
