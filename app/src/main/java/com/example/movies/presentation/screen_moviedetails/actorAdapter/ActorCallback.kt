package com.example.movies.presentation.screen_moviedetails.actorAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.movies.domain.model.Actor

class ActorCallback: DiffUtil.ItemCallback<Actor>() {


    override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean {
        return oldItem == newItem
    }
}