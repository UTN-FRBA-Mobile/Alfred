package com.botigocontigo.alfred.risk

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.botigocontigo.alfred.R

class RiskAdapter(private val myDataset: Array<Risk>) : RecyclerView.Adapter<RiskAdapter.ViewHolderRisks>() {

    class ViewHolderRisks(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descripcion: TextView = itemView.findViewById(R.id.descripcion_riesgo)
        val pOcurrencia: TextView = itemView.findViewById(R.id.probabilidad_ocurrencia)
        val impacto: TextView = itemView.findViewById(R.id.impacto)
        val cDeDeteccion: TextView = itemView.findViewById(R.id.c_deteccion)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiskAdapter.ViewHolderRisks {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.risk_item, parent, false)
        return RiskAdapter.ViewHolderRisks(view)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: ViewHolderRisks, pos: Int) {
        holder.descripcion.text = myDataset[pos].descripcion
        holder.impacto.text = myDataset[pos].impacto
        holder.cDeDeteccion.text = myDataset[pos].cDeDeteccion
        holder.pOcurrencia.text = myDataset[pos].pDeOcurrecia.toString()
    }



}