package com.botigocontigo.alfred

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.*

class FodaActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.foda_layout)

        val textViewStrengths = findViewById<TextView>(R.id.fodaTextViewStrengths)
        val fodaViewStrengths = findViewById<ListView>(R.id.fodaListViewStrengths)
        val textViewOpportunities = findViewById<TextView>(R.id.fodaTextViewOpportunities)
        val fodaViewOpportunities = findViewById<ListView>(R.id.fodaListViewOpportunities)
        val fodaButton = findViewById<Button>(R.id.fodaButton)
        val arrayOfStrengths = arrayOf("fortaleza1", "fortaleza2", "fortaleza3","fortaleza4","fortaleza5")
        val arrayOfOpportunities = arrayOf("oportunidad1", "oportunidad2", "oportunidad3","oportunidad4","oportunidad5")



        textViewOpportunities.setText("Oportunidades")
        textViewStrengths.setText("Fortalezas")
        fodaViewOpportunities.adapter = FodaAdapter(this, arrayOfOpportunities)
        fodaViewStrengths.adapter = FodaAdapter(this,arrayOfStrengths )
        fodaButton.setText("Nueva Tarea")



    }

}


    private class FodaAdapter(context: Context, inputArray: Array<String>): BaseAdapter() {
        private val fodaContext: Context
        private val length: Int
        private val array:Array<String>

        init {
            fodaContext = context
            length = inputArray.size
            array= inputArray
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val fodaRadioButton = RadioButton(this.fodaContext)
            fodaRadioButton.setText(this.array[position])

            return fodaRadioButton
        }

        override fun getItem(position: Int): Any {
            return "testing"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return this.length   }

    }