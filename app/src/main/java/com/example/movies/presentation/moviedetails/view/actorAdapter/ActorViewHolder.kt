package com.example.movies.presentation.moviedetails.view.actorAdapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.databinding.ViewHolderActorBinding
import com.example.movies.domain.model.Actor

class ActorViewHolder(private val binding: ViewHolderActorBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bing(actor: Actor) {

        binding.nameActor.text = actor.name
        Glide.with(itemView.context)
            .load(actor.imageUrl)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.imageActor)
    }
}