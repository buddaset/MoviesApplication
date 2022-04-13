package com.example.movies.adapter.actorAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.databinding.ViewHolderActorBinding
import com.example.movies.models.Actor

class ActorAdapter : RecyclerView.Adapter<ActorViewHolder>() {

   private val listActor = mutableListOf<Actor>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder =
        ActorViewHolder(
            ViewHolderActorBinding
                .inflate(LayoutInflater.from(parent.context), parent , false)
        )

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
       holder.bing(listActor[position])
    }

    override fun getItemCount(): Int = listActor.size

    fun addData(list: List<Actor>) {
        listActor.clear()
        listActor.addAll(list)
        notifyDataSetChanged()
    }
}