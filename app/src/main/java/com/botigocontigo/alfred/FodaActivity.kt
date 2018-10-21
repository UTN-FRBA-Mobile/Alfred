package com.botigocontigo.alfred

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.*
import com.botigocontigo.alfred.foda.Dimension
import com.botigocontigo.alfred.foda.FodaAdapter

class FodaActivity: AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    val arrayOfStrengths = arrayOf("fortaleza1", "fortaleza2", "fortaleza3","fortaleza4","fortaleza5")
    val arrayOfOpportunities = arrayOf("oportunidad1", "oportunidad2", "oportunidad3","oportunidad4","oportunidad5")
    val arrayOfWeaknesses = arrayOf("debilidad1", "debilidad2", "debilidad2", "debilidad2", "debilidad2")
    val arrayOfThreats = arrayOf("amenaza1","amenaza1","amenaza1","amenaza1","amenaza1","amenaza1")
    val dimensions = arrayOf(
            Dimension("Fortalezas", "Interna", arrayOfStrengths),
            Dimension("Oportunidades", "Externa", arrayOfOpportunities),
            Dimension("Debilidades", "Interna", arrayOfWeaknesses),
            Dimension("Amenazas", "Externa", arrayOfThreats)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foda)
        val button = findViewById<Button>(R.id.fodaButton)

        viewManager = LinearLayoutManager(this)
        viewAdapter = FodaAdapter(this, dimensions)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerFoda).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        button.setText("Nueva Tarea")

    }

}