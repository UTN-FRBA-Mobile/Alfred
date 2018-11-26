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
import com.botigocontigo.alfred.Services
import com.botigocontigo.alfred.backend.RisksGetCallbacks
import com.botigocontigo.alfred.storage.db.AppDatabase
import com.botigocontigo.alfred.storage.db.dao.RiskDao
import com.botigocontigo.alfred.storage.db.entities.Risk
import kotlinx.android.synthetic.main.activity_foda.view.*
import kotlinx.android.synthetic.main.activity_risks.view.*
import kotlinx.android.synthetic.main.new_risk_form.view.*
import kotlinx.android.synthetic.main.risk_item.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import android.widget.Toast
import android.view.Gravity
import android.widget.TextView
import com.botigocontigo.alfred.MyPreferences


class RiskFragment: Fragment(){

    private var mParam1: String? = null
    private var mParam2: String? = null

    private var riskAdapter :RiskAdapter? = null

    private var userId = ""

    private var nroSugerencia: Int = 0

    private val sugerencias = arrayListOf(
            "En esta pantalla podras ver tus riegos y agregar nuevos", "La Probabilidad de ocurrencia indica que tan probable es que ocurra el riego",
            "El Impacto es cuanto daño te hace el Riesgo si sucede", "La Capacidad de Deteccion es que tan dificil es detectar que el Riesgo va a suceder",
            "Podes agregar riesgos apretando el boton +", "Podes eliminar un riesgo apretando el boton X que tiene cada uno"
    )

    private val riesgoEjemplo =
        Risk(0, "Aqui va una descripcion", "Probabilidad de Ocurrencia"
                , "Impacto", "Capacidad de Deteccion", "")


    private lateinit var riskDAO: RiskDao

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val db = AppDatabase.getInstance(context!!)
            riskDAO = db.riskDao()
            userId = MyPreferences(context!!).getUserId()
            mParam1 = it.getString(ARG_PARAM1)
            mParam2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_risks, container, false)
        val context = inflater.context
        val riskList = view.recyclerRisk!!
        doAsync {
            var riesgos = arrayListOf<Risk>()
            riesgos.add(riesgoEjemplo)
            riesgos.addAll(riskDAO.getAllByUser(userId) as MutableList<Risk>)
            uiThread {
                riskList.layoutManager = LinearLayoutManager(context)
                riskAdapter = RiskAdapter(riesgos, context)
                Log.i("Cantidad de Riesgos: ", riesgos.size.toString())
                riskList.adapter = riskAdapter
                riskList.adapter?.notifyDataSetChanged()
                loadEventOnClickNewRisk(view)
                //loadRecyclerView(view, riesgos)
            }
        }
        return view
    }


    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    private fun loadRecyclerView(view: View, riesgos: MutableList<Risk>) {
        Log.i("Cantidad de Riesgos: ", riesgos.size.toString())
        view.findViewById<RecyclerView>(R.id.recyclerRisk).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            riskAdapter!!.setDataset(riesgos)
            adapter = riskAdapter
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
        view.findViewById<ImageView>(R.id.suggerenciasRiesgo)!!.setOnClickListener {
            val inflater = layoutInflater
            val layout = inflater.inflate(R.layout.chat_bubble_message, null)

            val text = layout.findViewById(R.id.txtOtherMessage) as TextView
            text.text = sugerencias.get(nroSugerencia)
            nroSugerencia++
            if(nroSugerencia==sugerencias.size){
                nroSugerencia = 0
            }

            val toast = Toast(context)
            toast.setGravity(Gravity.BOTTOM, -170, 150)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()


        }

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
                crearNuevoRiesgo(dw.descNuevoRiesgo.text.toString(),
                            dw.spinnerPdeOcurrencia.selectedItem as String,
                            dw.spinnerImpacto.selectedItem as String,
                            dw.spinnerCdeteccion.selectedItem as String,
                        { message: String ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        })
                mAlertDialog.dismiss()
            }

            dw.discardRisk.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }

    private fun crearNuevoRiesgo(desc: String, pOcurrencia: String, impacto: String, cDeteccion: String,  fnError: (String) -> Unit){
        var msg: String? = null

        if (desc == "")
            msg = "Descripcion necesaria"
        if (msg != null) {
            fnError(msg)
        } else {
            doAsync {
                riskDAO.insertAll(Risk(null, desc, pOcurrencia, impacto, cDeteccion, userId))
                val riesgos = riskDAO.getAllByUser(userId) as MutableList<Risk>
                uiThread {
                    riskAdapter!!.setDataset(riesgos)
                    riskAdapter!!.notifyDataSetChanged()
                }
            }
        }

    }

    private fun persisServerInfo(riesgos: Array<Risk>){
        val services = Services(this.view!!.context)
        val riskGetCallbacks= RisksGetCallbacks()
        val botigocontigoApi = services.botigocontigoApi()
        botigocontigoApi.risksSaveAll(riesgos).call(riskGetCallbacks)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        doAsync {
            val riesgos = riskDAO.getAllByUser(userId) as Array<Risk>
            persisServerInfo(riesgos)
            //post to API
        }
        userId = ""
    }

}