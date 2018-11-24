package com.botigocontigo.alfred.foda

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.botigocontigo.alfred.R
import com.botigocontigo.alfred.storage.db.entities.Dimension


class FodaAdapter(private val context:Context, private val dimensionsArray: MutableList<Dimension>): RecyclerView.Adapter<FodaAdapter.ViewHolderFoda>()  {

    class ViewHolderFoda(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.fodaTextView)
        val type: TextView = itemView.findViewById(R.id.typeOfVarViews)
        val listOfTips: ListView = itemView.findViewById(R.id.fodaListView)    }

    override fun getItemCount(): Int {
        return dimensionsArray.size    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFoda {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.foda_view, parent, false)
        return ViewHolderFoda(view)
    }

    override fun onBindViewHolder(holder: ViewHolderFoda, position: Int) {
        holder.name.text = dimensionsArray[position].name
        holder.type.text = dimensionsArray[position].type
        holder.listOfTips.adapter = ContentAdapter(context,dimensionsArray[position])

    }
}