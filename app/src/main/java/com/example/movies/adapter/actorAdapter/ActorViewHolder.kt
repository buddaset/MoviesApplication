package com.example.movies.adapter.actorAdapter

import androidx.recyclerview.widget.RecyclerView
import com.example.movies.databinding.ViewHolderActorBinding
import com.example.movies.databinding.ViewHolderMovieBinding
import com.example.movies.models.Actor

class ActorViewHolder(private val binding: ViewHolderActorBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bing(actor: Actor) {

        binding.nameActor.text = actor.name
        binding.imageActor.setImageResource(actor.image)

    }
}