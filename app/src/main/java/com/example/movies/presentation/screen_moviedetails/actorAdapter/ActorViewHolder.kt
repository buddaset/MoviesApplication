package com.example.movies.presentation.screen_moviedetails.actorAdapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.R

import com.example.movies.databinding.ViewHolderActorBinding
import com.example.movies.domain.model.Actor

class ActorViewHolder(private val binding: ViewHolderActorBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bing(actor: Actor) {
        Log.d("actor", "actor \n ${actor.id}  \n ${actor.name} \n ${actor.imageUrl}")

        binding.nameActor.text = actor.name
        Glide.with(itemView.context)
            .load(actor.imageUrl)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.imageActor)

    }
}