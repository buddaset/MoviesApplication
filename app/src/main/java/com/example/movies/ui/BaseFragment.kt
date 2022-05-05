package com.example.movies.ui

import android.service.media.MediaBrowserService
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.movies.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.movies.data.Result
import com.example.movies.databinding.PartResultBinding

open class BaseFragment: Fragment() {



}




fun <T> BaseFragment.collectFlow(flow: Flow<T>, onCollect: (T) -> Unit)  {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest {
                onCollect(it)
            }
        }
    }
}


fun <T> BaseFragment.renderState(root: ViewGroup, result: Result<T>, onSuccess: (T) -> Unit) {
    val bindingError = PartResultBinding.bind(root)

    root.children.forEach { it.isVisible = false }
    Log.d("Detail", "render")

    when(result){
        is Result.Loading -> {
            root.findViewById<ProgressBar>(R.id.progressBarMovieDetail).isVisible = true
        }

        is Result.Error -> {
            bindingError.errorContainer.isVisible = true
        }

        is Result.Success -> {
            root.children
                .filter { it.id != R.id.progressBarMovieDetail && it.id != R.id.errorContainer }
                .forEach { it.isVisible = true }
            onSuccess(result.data)
        }
    }

}

fun BaseFragment.onTryAgain(root: View, onTryAgain: ()-> Unit) {
    root.findViewById<Button>(R.id.tryAgainButton).setOnClickListener {
        onTryAgain()
    }
}


