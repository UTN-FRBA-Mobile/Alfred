package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class MovieAdapter(private val context: Context, private val movies: List<ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.api.responses.Movie>, var layoutType: Int, private val storePostersInFs: Boolean) : RecyclerView.Adapter<MovieViewHolder>() {
    private val inflater = LayoutInflater.from(context)

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun getItemViewType(position: Int): Int {
        return layoutType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(inflater.inflate(if(viewType == Layout_grid) R.layout.item_movie_grid else R.layout.item_movie, parent, false), context, storePostersInFs)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    companion object {
        val Layout_grid : Int = 1
        val Layout_list : Int = 2
    }
}