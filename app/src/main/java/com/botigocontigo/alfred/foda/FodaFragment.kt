package com.botigocontigo.alfred.foda

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.botigocontigo.alfred.R

class FodaFragment : Fragment() {

    val arrayOfStrengths = arrayOf("fortaleza1", "fortaleza2", "fortaleza3", "fortaleza4", "fortaleza5")
    val arrayOfOpportunities = arrayOf("oportunidad1", "oportunidad2", "oportunidad3", "oportunidad4", "oportunidad5")
    val arrayOfWeaknesses = arrayOf("debilidad1", "debilidad2", "debilidad2", "debilidad2", "debilidad2")
    val arrayOfThreats = arrayOf("amenaza1", "amenaza1", "amenaza1", "amenaza1", "amenaza1", "amenaza1")
    val dimensions = arrayOf(
            Dimension("Fortalezas", "Interna", arrayOfStrengths),
            Dimension("Oportunidades", "Externa", arrayOfOpportunities),
            Dimension("Debilidades", "Interna", arrayOfWeaknesses),
            Dimension("Amenazas", "Externa", arrayOfThreats)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_tasks, container, false)
        //  val button = findViewById<Button>(R.id.fodaButton)
        loadRecyclerView(view)
        //  button.setText("Nueva Tarea")
        return view
    }

    private fun loadRecyclerView(view: View) {
        view.findViewById<RecyclerView>(R.id.recyclerFoda).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = FodaAdapter(context,dimensions)
        }
    }

}