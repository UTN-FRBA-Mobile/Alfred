package com.botigocontigo.alfred

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.ImageButton
import com.botigocontigo.alfred.risk.RiskAdapter
import com.botigocontigo.alfred.risk.Risk

class RiskActivity : AppCompatActivity(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_risks)
        val nuevaTareaButton = findViewById<Button>(R.id.riskNuevaTareaButton)
        val sugerenciasButton = findViewById<ImageButton>(R.id.riskSuggestionButton)
        val riesgos = arrayOf(
                Risk("Descripcion riego", 50, "Medio", "Capacidad de deteccion")
        )

        viewManager = LinearLayoutManager(this)
        viewAdapter = RiskAdapter(riesgos)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerRisk).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        nuevaTareaButton.setText("Nueva Tarea")
    }

}