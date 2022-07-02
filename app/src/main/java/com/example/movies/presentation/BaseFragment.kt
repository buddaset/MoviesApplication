package com.example.movies.presentation

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.movies.R
import com.example.movies.databinding.PartResultBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


open class BaseFragment: Fragment()




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

    root.children
        .filter { it.id != R.id.loadStateView }
        .forEach { it.isVisible = result is Result.Success }

    bindingError.progressBar.isVisible = result is Result.Loading
    bindingError.tryAgainButton.isVisible = result is Result.Error
    bindingError.messageTextView.isVisible = result is Result.Error

    if (result is Result.Success) {
        onSuccess(result.data)
    }
}

fun BaseFragment.onTryAgain(root: View, onTryAgain: ()-> Unit) {
    root.findViewById<Button>(R.id.tryAgainButton).setOnClickListener {
        onTryAgain()
    }
}


