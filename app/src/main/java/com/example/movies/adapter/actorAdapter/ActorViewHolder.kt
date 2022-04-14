package com.example.movies.adapter.actorAdapter

import androidx.recyclerview.widget.RecyclerView
import com.example.movies.databinding.ViewHolderActorBinding
import com.example.movies.models.ActorData

class ActorViewHolder(private val binding: ViewHolderActorBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bing(actorData: ActorData) {

        binding.nameActor.text = actorData.name
        binding.imageActor.setImageResource(actorData.image)

    }
}