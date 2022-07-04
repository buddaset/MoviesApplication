package com.example.movies.presentation.util

import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.movies.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


fun <T> Fragment.collectFlow(flow: Flow<T>, onCollect: (T) -> Unit)  {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest {
                onCollect(it)
            }
        }
    }
}


fun Fragment.onTryAgain(root: View, onTryAgain: ()-> Unit) {
    root.findViewById<Button>(R.id.tryAgainButton).setOnClickListener {
        onTryAgain()
    }
}
