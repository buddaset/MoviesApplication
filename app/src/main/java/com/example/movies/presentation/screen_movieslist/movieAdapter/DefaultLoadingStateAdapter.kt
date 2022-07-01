package com.example.movies.presentation.screen_movieslist.movieAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.databinding.PartResultBinding

typealias TryAgainAction = () -> Unit

class DefaultLoadingStateAdapter(
    private val tryAgainAction: TryAgainAction) : LoadStateAdapter<DefaultLoadingStateAdapter.Holder>() {


    override fun onBindViewHolder(holder: Holder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PartResultBinding.inflate(inflater, parent, false)
        return  Holder(binding, tryAgainAction)
    }


    class Holder(
        private val binding: PartResultBinding,
        private val tryAgainAction: TryAgainAction
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tryAgainButton.setOnClickListener { tryAgainAction() }
        }

        fun bind(loadState: LoadState) = with(binding) {
            progressBar.isVisible = loadState is LoadState.Loading
            tryAgainButton.isVisible = loadState !is LoadState.Loading
            messageTextView.isVisible = loadState !is LoadState.Loading

        }
    }



}