package com.botigocontigo.alfred.risk

import android.content.ClipDescription
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import com.botigocontigo.alfred.R
import com.botigocontigo.alfred.storage.db.AppDatabase
import com.botigocontigo.alfred.storage.db.dao.RiskDao
import com.botigocontigo.alfred.storage.db.entities.Risk
import kotlinx.android.synthetic.main.new_risk_form.view.*
import kotlinx.android.synthetic.main.risk_item.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class RiskFragment: Fragment(){

    private var mParam1: String? = null
    private var mParam2: String? = null

    var riesgos: MutableList<Risk> = arrayListOf()

    private val riesgoEjemplo =
        Risk(0, "Aqui va una descripcion", "Alta", "Bajo", "Alta")


    private lateinit var riskDAO: RiskDao

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val db = AppDatabase.getInstance(context!!)
            riskDAO = db.riskDao()
            mParam1 = it.getString(ARG_PARAM1)
            mParam2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_risks, container, false)
        doAsync {
            riesgos = riskDAO.getAll() as MutableList<Risk>
            if(riesgos.size == 0){
                riesgos.add(riesgoEjemplo)
            uiThread {
                loadRecyclerView(view)
            }
            }
        }
        loadEventOnClickNewRisk(view)
        return view
    }


    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    private fun loadRecyclerView(view: View) {
        view.findViewById<RecyclerView>(R.id.recyclerRisk).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            Log.i("Cantidad de Riesgos: ", riesgos.size.toString())
            adapter = RiskAdapter(riesgos, context)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String):  RiskFragment {
            val fragment = RiskFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }

    }

    private fun loadEventOnClickNewRisk(view: View){
        view.findViewById<ImageView>(R.id.agregarRiesgo)!!.setOnClickListener {
            val dw: View = LayoutInflater.from(context).inflate(R.layout.new_risk_form, null)
            val mBuilder = AlertDialog.Builder(context!!).setView(dw)

            val spinnerPdeOcurrencia = dw.findViewById<Spinner>(R.id.spinnerPdeOcurrencia)
            spinnerPdeOcurrencia.adapter = ArrayAdapter(context, R.layout.spinner_tasks, resources.getStringArray(R.array.valores_riesgos)).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            val spinnerCdeDeteccion = dw.findViewById<Spinner>(R.id.spinnerCdeteccion)
            spinnerCdeDeteccion.adapter = ArrayAdapter(context, R.layout.spinner_tasks, resources.getStringArray(R.array.valores_riesgos)).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            val spinnerImpacto = dw.findViewById<Spinner>(R.id.spinnerImpacto)
            spinnerImpacto.adapter = ArrayAdapter(context, R.layout.spinner_tasks, resources.getStringArray(R.array.impacto_riesgo)).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }


            val mAlertDialog = mBuilder.show()

            dw.confirmRisk.setOnClickListener {
                doAsync {
                    crearNuevoRiesgo(dw.descNuevoRiesgo.text.toString(),
                            dw.spinnerPdeOcurrencia.selectedItem as String,
                            dw.spinnerImpacto.selectedItem as String,
                            dw.spinnerCdeteccion.selectedItem as String)
                }
                mAlertDialog.dismiss()
            }

            dw.discardRisk.setOnClickListener {
                mAlertDialog.dismiss()
            }

        view.findViewById<ImageView>(R.id.suggerenciasRiesgo)!!.setOnClickListener {

        }
        }
    }

    private fun crearNuevoRiesgo(desc: String, pOcurrencia: String, impacto: String, cDeteccion: String){
        riskDAO.insertAll( Risk(null, desc, pOcurrencia, impacto, cDeteccion))

    }

}