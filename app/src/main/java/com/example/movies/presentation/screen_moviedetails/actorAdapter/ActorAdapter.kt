package com.example.movies.presentation.screen_moviedetails.actorAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.movies.databinding.ViewHolderActorBinding
import com.example.movies.domain.model.Actor

class ActorAdapter : ListAdapter<Actor, ActorViewHolder>(ActorCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder =
        ActorViewHolder(
            ViewHolderActorBinding
                .inflate(LayoutInflater.from(parent.context), parent , false)
        )

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
       holder.bing(getItem(position))
    }




}