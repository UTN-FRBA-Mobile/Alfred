package com.botigocontigo.alfred.risk

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.botigocontigo.alfred.R
import com.botigocontigo.alfred.storage.db.AppDatabase
import com.botigocontigo.alfred.storage.db.dao.RiskDao
import com.botigocontigo.alfred.storage.db.entities.Risk
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class RiskAdapter(private var myDataset: MutableList<Risk>, private val context: Context) : RecyclerView.Adapter<RiskAdapter.RiskViewHolder>() {

    class RiskViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descrpcion: TextView = itemView.findViewById(R.id.descripcion_riesgo)
        val pDeOcurrecia: TextView = itemView.findViewById(R.id.probabilidad_ocurrencia)
        val impacto: TextView = itemView.findViewById(R.id.impacto)
        val cDeDeteccion: TextView = itemView.findViewById(R.id.c_deteccion)
        val deleteRisk: ImageView = itemView.findViewById(R.id.deleteRisk)
    }

    val db = AppDatabase.getInstance(context!!)
    private val riskDao: RiskDao = db.riskDao()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.risk_item, parent, false)
        view.findViewById<ImageView>(R.id.deleteRisk).bringToFront()
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
        holder.deleteRisk.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            builder.setTitle("Alerta")
            builder.setMessage("¿Está seguro que desea eliminar el riesgo?")
            builder.setPositiveButton("Si"){dialog, which ->
                doAsync {
                    riskDao.deleteAll(risk)
                    uiThread {
                        myDataset.remove(risk)
                        notifyDataSetChanged()
                    }
                }
            }
            builder.setNegativeButton("No"){ _, _ -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()
            }
    }

    fun setDataset(ds: MutableList<Risk>): RiskAdapter {
        myDataset = ds
        return this
    }



}