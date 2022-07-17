package com.example.movies.presentation.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.domain.model.Movie
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


fun <T> Fragment.collectFlow(flow: Flow<T>, onCollect: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest {
                onCollect(it)
            }
        }
    }
}



fun <T> Fragment.collectPagingFlow(flow: Flow<T>, block: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest {
                block(it)
            }
        }
    }
}


fun Fragment.onTryAgain(root: View, onTryAgain: () -> Unit) {
    root.findViewById<Button>(R.id.tryAgainButton).setOnClickListener {
        onTryAgain()
    }
}



fun SearchView.onTextChange(textChange: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = false
        // do nothing


        override fun onQueryTextChange(newText: String?): Boolean {
            newText?.let { textChange(newText) }
            return true
        }
    })
}

fun View.hideKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}