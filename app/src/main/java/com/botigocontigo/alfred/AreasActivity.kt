package com.botigocontigo.alfred

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner

class AreasActivity : AppCompatActivity(), OnItemSelectedListener {

    private var spinner: Spinner? = null

    private val modelos_negocio: Array<String> = arrayOf("Version 1", "Version 2", "Version 3")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_areas)

        spinner = this.findViewById(R.id.spinner_modelos)
        val adapterSpinner: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, modelos_negocio)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = adapterSpinner
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}

}
