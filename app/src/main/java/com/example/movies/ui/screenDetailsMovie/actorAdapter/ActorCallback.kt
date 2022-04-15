package com.example.movies.ui.screenDetailsMovie.actorAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.movies.models.ActorData

class ActorCallback: DiffUtil.ItemCallback<ActorData>() {
    override fun areItemsTheSame(oldItem: ActorData, newItem: ActorData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ActorData, newItem: ActorData): Boolean {
        return oldItem == newItem
    }
}