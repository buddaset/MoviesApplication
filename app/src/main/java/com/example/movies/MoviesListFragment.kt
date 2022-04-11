package com.example.movies

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movies.databinding.FragmentMoviesListBinding

class MoviesListFragment : Fragment() {

    private lateinit var binding : FragmentMoviesListBinding

    private var listener: ClickMovieListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is ClickMovieListener)
            listener = context
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMoviesListBinding.inflate(inflater, container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardMovie.setOnClickListener {
            listener?.clickMovie()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
    }

    companion object {

        fun newInstance() = MoviesListFragment()

    }

    interface ClickMovieListener {

        fun clickMovie()
    }
}