package com.botigocontigo.alfred.areas

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.*


import com.botigocontigo.alfred.R
import com.botigocontigo.alfred.storage.db.AppDatabase
import com.botigocontigo.alfred.storage.db.dao.AreaDao
import com.botigocontigo.alfred.storage.db.entities.Area
import kotlinx.android.synthetic.main.dialog_form_model.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class AreasFragment : Fragment(), View.OnClickListener{

    private var vfrag: View? = null
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var areaDao: AreaDao

    private var mapModels: Map<String?, Int?> = emptyMap()
    private var model: Area? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val db = AppDatabase.getInstance(context!!)
            areaDao = db.areaDao()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        vfrag = inflater.inflate(R.layout.fragment_areas, container, false)

        loadButtons()

        loadEventOnClickNewModel()
        //loadEventOnClickAreaDetail(v)

        doAsync {
            //INSERT FOR TEST
            areaDao.insertAll(
                    Area(1, "1", "Modelo A", "YPF, Repsol, AXION","relA","chanC","valueA","actA","resoA","parA","incA","costA"),
                    Area(2, "1", "Modelo B", "clientesB","relB","chanB","valueB","actB","resoB","partB","incB","costB"),
                    Area(3, "2", "Modelo C", "clientesC","relC","chanC","valueC","actC","resoC","partC","incC","costC")
            )

            //HARDCODED USER ID
            mapModels = areaDao.getModelsByUserId("1").map { it.name to it.id }.toMap()
            model = areaDao.findById(1)

            uiThread { loadSpinnerModelos()  }

        }

        return vfrag
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    override fun onClick(v: View?) {
        switchArea(v!!.id)
    }

    private fun toast(msg:String){
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }

    private fun switchFragment(areaName: String, areaDetail: String?){
        //Cambio al Fragment de Detalle de area
        fragmentManager!!
                .beginTransaction()
                .replace(R.id.content_frame, DetailAreaFragment.newInstance(areaName, areaDetail))
                .addToBackStack(null)
                .commit()
    }

    private fun switchArea(opc : Int){
        when(opc){
            R.id.btnClientes -> switchFragment("Segmento Clientes", model?.clients)
            R.id.btnRelaciones -> switchFragment("Relaciones", model?.relationships)
            R.id.btnCanales -> switchFragment("Canales", model?.channels)
            R.id.btn_PropuestaValor -> switchFragment("Propuesta de Valor", model?.valueProposition)
            R.id.btnActividades -> switchFragment("Actividades",model?.activities)
            R.id.btnRecursos -> switchFragment("Recursos",model?.resources)
            R.id.btnSociosClave -> switchFragment("Socios Clave",model?.partners)
            R.id.btnFuentesIngreso -> switchFragment("Fuentes de Ingreso",model?.income)
            R.id.btnCostos -> switchFragment("Costos",model?.costs)
        }
    }

    private fun loadButtons(){
        val btnClientes = vfrag?.findViewById<Button>(R.id.btnClientes)
        val btnRelaciones = vfrag?.findViewById<Button>(R.id.btnRelaciones)
        val btnCanales = vfrag?.findViewById<Button>(R.id.btnCanales)
        val btnPropuestaValor = vfrag?.findViewById<Button>(R.id.btn_PropuestaValor)
        val btnActividades = vfrag?.findViewById<Button>(R.id.btnActividades)
        val btnRecursos = vfrag?.findViewById<Button>(R.id.btnRecursos)
        val btnSociosClave = vfrag?.findViewById<Button>(R.id.btnSociosClave)
        val btnFuentesIngreso = vfrag?.findViewById<Button>(R.id.btnFuentesIngreso)
        val btnCostos = vfrag?.findViewById<Button>(R.id.btnCostos)

        btnClientes?.setOnClickListener(this)
        btnRelaciones?.setOnClickListener(this)
        btnCanales?.setOnClickListener(this)
        btnPropuestaValor?.setOnClickListener(this)
        btnActividades?.setOnClickListener(this)
        btnRecursos?.setOnClickListener(this)
        btnSociosClave?.setOnClickListener(this)
        btnFuentesIngreso?.setOnClickListener(this)
        btnCostos?.setOnClickListener(this)
    }

    private fun loadSpinnerModelos() {
        val spinner = vfrag?.findViewById<Spinner>(R.id.spinner_modelos)
        spinner!!.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, mapModels.keys.toList()).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun loadEventOnClickNewModel() {
        vfrag?.findViewById<ImageButton>(R.id.btnNewModel)!!.setOnClickListener {
            // dw = dialogView
            val dw: View = LayoutInflater.from(context).inflate(R.layout.dialog_form_model, null)
            val mBuilder = AlertDialog.Builder(context!!).setView(dw)

            //dw.btnOk.text = "AGREGAR"
            //dw.btnCancel.text = "CANCELAR"

            val mAlertDialog = mBuilder.show()

            dw.btnCancel.setOnClickListener {
                mAlertDialog.dismiss()
            }

            dw.btnOk.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }



    private fun loadEventOnClickAreaDetail(view: View) {

        /**SEGMENTO CLIENTES**/
        view.findViewById<Button>(R.id.btnClientes)!!.setOnClickListener {
            Toast.makeText(activity, "Segmento Clientes" , Toast.LENGTH_LONG).show()

            fragmentManager!!
                    .beginTransaction()
                    .replace(R.id.content_frame, DetailAreaFragment())
                    .addToBackStack(null)
                    .commit()
        }

        /**RELACIONES**/
        view.findViewById<Button>(R.id.btnRelaciones)!!.setOnClickListener {
            Toast.makeText(activity, "Relaciones" , Toast.LENGTH_LONG).show()
        }

        /**CANALES**/
        view.findViewById<Button>(R.id.btnCanales)!!.setOnClickListener {
            Toast.makeText(activity, "Canales" , Toast.LENGTH_LONG).show()
        }

        /**PROPUESTA DE VALOR**/
        view.findViewById<Button>(R.id.btn_PropuestaValor)!!.setOnClickListener {
            Toast.makeText(activity, "Propuesta de Valor" , Toast.LENGTH_LONG).show()
        }

        /**ACTIVIDADES**/
        view.findViewById<Button>(R.id.btnActividades)!!.setOnClickListener {
            Toast.makeText(activity, "Actividades" , Toast.LENGTH_LONG).show()
        }

        /**RECURSOS**/
        view.findViewById<Button>(R.id.btnRecursos)!!.setOnClickListener {
            Toast.makeText(activity, "Recursos" , Toast.LENGTH_LONG).show()
        }

        /**SOCIOS CLAVE**/
        view.findViewById<Button>(R.id.btnSociosClave)!!.setOnClickListener {
            Toast.makeText(activity, "Socios Clave" , Toast.LENGTH_LONG).show()
        }

        /**FUENTES DE INGRESO**/
        view.findViewById<Button>(R.id.btnFuentesIngreso)!!.setOnClickListener {
            Toast.makeText(activity, "Fuentes de Ingreso" , Toast.LENGTH_LONG).show()
        }

        /**COSTOS**/
        view.findViewById<Button>(R.id.btnCostos)!!.setOnClickListener {
            Toast.makeText(activity, "Costos" , Toast.LENGTH_LONG).show()
        }

    }


}
