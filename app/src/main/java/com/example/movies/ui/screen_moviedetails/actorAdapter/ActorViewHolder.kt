package com.example.movies.ui.screen_moviedetails.actorAdapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.movies.databinding.ViewHolderActorBinding
import com.example.movies.models.ActorData

class ActorViewHolder(private val binding: ViewHolderActorBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bing(actor: ActorData) {
        Log.d("actor", "actor \n ${actor.id}  \n ${actor.name} \n ${actor.imageUrl}")

        binding.nameActor.text = actor.name
        binding.imageActor.load(actor.imageUrl)
    }
}