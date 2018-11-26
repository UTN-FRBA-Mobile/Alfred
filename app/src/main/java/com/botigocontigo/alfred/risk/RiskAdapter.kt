package com.botigocontigo.alfred.risk

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.botigocontigo.alfred.R
import com.botigocontigo.alfred.storage.db.entities.Risk

class RiskAdapter(private var myDataset: MutableList<Risk>, private val context: Context) : RecyclerView.Adapter<RiskAdapter.RiskViewHolder>() {

    class RiskViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descrpcion: TextView = itemView.findViewById(R.id.descripcion_riesgo)
        val pDeOcurrecia: TextView = itemView.findViewById(R.id.probabilidad_ocurrencia)
        val impacto: TextView = itemView.findViewById(R.id.impacto)
        val cDeDeteccion: TextView = itemView.findViewById(R.id.c_deteccion)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.risk_item, parent, false)
        return RiskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myDataset.size
    }

    override fun onBindViewHolder(holder: RiskViewHolder, pos: Int) {
        val risk = myDataset.get(pos)
        holder.descrpcion.text = risk.descripcion
        holder.pDeOcurrecia.text = risk.pDeOcurrecia
        holder.impacto.text = risk.impacto
        holder.cDeDeteccion.text = risk.cDeDeteccion
    }

    fun setDataset(ds: MutableList<Risk>): RiskAdapter {
        myDataset = ds
        return this
    }



}