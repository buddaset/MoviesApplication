package com.example.movies.ui.screenDetailsMovie.actorAdapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.databinding.ViewHolderActorBinding
import com.example.movies.models.ActorData

class ActorViewHolder(private val binding: ViewHolderActorBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bing(actor: ActorData) {

        binding.nameActor.text = actor.name
        Glide.with(itemView.context)
            .load(actor.imageUrl)
            .into(binding.imageActor)

    }
}