package com.botigocontigo.alfred.risk

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.risk_item.view.*

class RiskViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    private var descrpcion: TextView = view.descripcion_riesgo
    private var pDeOcurrecia: TextView = view.probabilidad_ocurrencia
    private var impacto: TextView = view.impacto
    private var cDeDeteccion: TextView = view.c_deteccion


    open fun bind(risk: Risk) {
        descrpcion.text = risk.descripcion
        pDeOcurrecia.text = risk.pDeOcurrecia.toString()
        impacto.text = risk.impacto
        cDeDeteccion.text = risk.cDeDeteccion
    }
}