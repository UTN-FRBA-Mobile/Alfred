package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class SearchedMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.movieTitle)

    fun bind(searchedMovie: String) {
        title.text = searchedMovie
    }
}