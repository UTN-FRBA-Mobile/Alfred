package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

class SearchedMoviesAdapter(context: Context, private val searchResults: List<String>) : RecyclerView.Adapter<SearchedMovieViewHolder>() {

    private val inflater = LayoutInflater.from(context)!!
    private val mListener: SearchedMoviesAdapter.IListener = context as SearchedMoviesAdapter.IListener

    interface IListener {
        fun itemClicked(title: String)
    }

    override fun getItemCount(): Int {
        return searchResults.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedMovieViewHolder {
        return SearchedMovieViewHolder(inflater.inflate(R.layout.item_searched_movie, parent, false))
    }

    override fun onBindViewHolder(holderSearchedMovie: SearchedMovieViewHolder, position: Int) {
        holderSearchedMovie.bind(searchResults[position])
        holderSearchedMovie.itemView.setOnClickListener { view ->
            mListener.itemClicked(view.findViewById<TextView>(R.id.movieTitle).text.toString())
        }
    }
}