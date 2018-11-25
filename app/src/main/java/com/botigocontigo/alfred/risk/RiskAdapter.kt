package com.botigocontigo.alfred.risk

import android.content.Context
import android.mtp.MtpConstants
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.botigocontigo.alfred.R
import com.botigocontigo.alfred.storage.db.entities.Risk

class RiskAdapter(private val myDataset: MutableList<Risk>, private val context: Context) : RecyclerView.Adapter<RiskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.risk_item, parent, false)
        return RiskViewHolder(view)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: RiskViewHolder, pos: Int) {
        val risk = myDataset.get(pos)
        holder?.bind(risk)
    }



}