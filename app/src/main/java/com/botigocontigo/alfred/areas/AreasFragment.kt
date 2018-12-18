package com.botigocontigo.alfred.areas

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.*
import com.botigocontigo.alfred.MyPreferences


import com.botigocontigo.alfred.R
import com.botigocontigo.alfred.Services
import com.botigocontigo.alfred.backend.AreasGetCallbacks
import com.botigocontigo.alfred.backend.BotigocontigoApi
import com.botigocontigo.alfred.storage.db.AppDatabase
import com.botigocontigo.alfred.storage.db.dao.AreaDao
import com.botigocontigo.alfred.storage.db.entities.Area
import kotlinx.android.synthetic.main.dialog_form_model.view.*
import kotlinx.android.synthetic.main.fragment_areas.*
import kotlinx.android.synthetic.main.fragment_areas.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


class AreasFragment : Fragment(), View.OnClickListener{

    private var executeInitializeDB: Boolean = true
    private var vfrag: View? = null
    private lateinit var api: BotigocontigoApi

    private lateinit var areaDao: AreaDao

    private var mapModels: Map<String?, String?> = emptyMap()
    private var model: Area? = null
    private var userId = ""
    var detailLoad: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val db = AppDatabase.getInstance(context!!)
            areaDao = db.areaDao()
            userId=MyPreferences(context!!).getUserId()!!
        }
        Log.i("USERID: ", userId)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        vfrag = inflater.inflate(R.layout.fragment_areas, container, false)
        loadButtons()

        toast("Seleccione un area para ver el detalle")

        val services = Services(inflater.context)
        api = services.botigocontigoApi()

        api.areasGetAll().call(AreasGetCallbacks(::loadToDB))
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
        if(spinner_modelos.selectedItem==null){
            toast("Seleccione un modelo")
        }else{
            switchArea(v!!.id)
        }
    }

    private fun toast(msg:String){
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun switchFragment(areaName: String){
        //Cambio al Fragment de Detalle de area
        executeInitializeDB = false
        Log.i("areas en el 1ro", model.toString())

        fragmentManager!!
                .beginTransaction()
                .replace(R.id.content_frame, DetailAreaFragment.newInstance(areaName, model))
                .commit()
        val spinner = vfrag!!.findViewById<Spinner>(R.id.spinner_modelos)
        MyPreferences(context!!).setActualModel(spinner!!.selectedItemPosition)
        detailLoad = true
    }

    private fun switchArea(opc : Int){
        when(opc){
            R.id.btnClientes -> switchFragment("Segmento Clientes")
            R.id.btnRelaciones -> switchFragment("Relaciones")
            R.id.btnCanales -> switchFragment("Canales")
            R.id.btn_PropuestaValor -> switchFragment("Propuesta de Valor")
            R.id.btnActividades -> switchFragment("Actividades")
            R.id.btnRecursos -> switchFragment("Recursos")
            R.id.btnSociosClave -> switchFragment("Socios Clave")
            R.id.btnFuentesIngreso -> switchFragment("Fuentes de Ingreso")
            R.id.btnCostos -> switchFragment("Costos")
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
        Log.i("selected model 1",  MyPreferences(context!!).getActualModel().toString())

        spinner!!.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, mapModels.keys.toList()).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        }
        if(!mapModels.isEmpty() && MyPreferences(context!!).getActualModel() != 0){
            spinner!!.setSelection(MyPreferences(context!!).getActualModel())
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }
            override fun onItemSelected(parent: AdapterView<*>?, view2: View?, position: Int, id: Long) {
                val modelName = spinner_modelos.selectedItem.toString()
                MyPreferences(context!!).setActualModel(position)
                Log.i("selected model 2",  MyPreferences(context!!).getActualModel().toString())
                doAsync {
                    model = areaDao.findById(mapModels.getValue(modelName)!!)
                }
            }
        }

        Log.i("MAP ITEMS", mapModels.toList().toString())
    }

    private fun loadEventOnClickNewModel() {
        vfrag?.findViewById<ImageButton>(R.id.btnNewModel)!!.setOnClickListener {

            val diagView: View = LayoutInflater.from(context).inflate(R.layout.dialog_form_model, null)
            val mBuilder = AlertDialog.Builder(context!!).setView(diagView)

            val mAlertDialog = mBuilder.show()

            diagView.btnCancel.setOnClickListener {
                mAlertDialog.dismiss()
            }

            diagView.btnOk.setOnClickListener {
                mAlertDialog.dismiss()
                toast("Modelo Creado")
                createModel(mAlertDialog,diagView)
            }
        }
    }

    private fun loadEventOnClickDeleteModel() {
        vfrag?.findViewById<ImageButton>(R.id.btnDeleteModel)!!.setOnClickListener {

            if (spinner_modelos.selectedItem!=null){
                val mBuilder = AlertDialog.Builder(context!!)
                val modelToDelete = spinner_modelos.selectedItem.toString()

                mBuilder.setTitle("Alerta")
                mBuilder.setMessage("¿Desea eliminar el modelo: "+ modelToDelete + "?")
                mBuilder.setPositiveButton("SI", { dialogInterface: DialogInterface, i: Int ->
                    toast("Modelo eliminado")
                    deleteModel(modelToDelete)
                })
                mBuilder.setNegativeButton("NO", { dialogInterface: DialogInterface, i: Int ->
                    toast("Operacion cancelada")
                })

                mBuilder.show()
            }else{
                toast("Seleccione un modelo")
            }

        }
    }

    private fun deleteModel(model: String) {
        var msg: String? = null
        MyPreferences(context!!).setActualModel(0)
        doAsync {
            areaDao.deleteModel(mapModels[model]!!)
            mapModels = areaDao.getModelsByUserId(userId).map { it.name to it.id }.toMap()

            uiThread {
                loadSpinnerModelos()
            }
        }
    }

    private fun createModel(alertDialog: AlertDialog, viewDialog: View) {
        var msg: String? = null

        var newModelName: String? = viewDialog.findViewById<TextInputEditText>(R.id.txtNewModel).text.toString()

        Log.i("CREATE: ", newModelName)

        if (newModelName == "" )
            msg = "Escriba un nombre para el modelo"

        if (msg != null) {
            toast("Error: "+msg)
        } else {
            val newModel = Area(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    name = newModelName,
                    activities = "",
                    channels = "",
                    clients = "",
                    costs = "",
                    income = "",
                    partners = "",
                    relationships = "",
                    resources = "",
                    valueProposition = ""
            )
            doAsync {
                areaDao.insertAll(newModel)
                mapModels = areaDao.getModelsByUserId(userId).map { it.name to it.id }.toMap()

                persistServerInfo()

                uiThread {
                    MyPreferences(context!!).setActualModel(mapModels.size - 1)
                    loadSpinnerModelos()
                    alertDialog.dismiss()
                }
            }
        }
    }

    private fun loadToDB(areas: List<Area>) {
        Log.i("loadTODB: areas",areas.toString())
        doAsync {
            if (executeInitializeDB) {
                if (areas.isNotEmpty()) {
                    areaDao.deleteAllRows()
                    areaDao.insertAll(*areas.toTypedArray())
                } else {
                    if(areaDao.getAreasCount()<1) {
                        areaDao.insertAll(
                                Area(UUID.randomUUID().toString(), userId, "Modelo A", "clientesA", "relA", "chanA", "valueA", "actA", "resoA", "parA", "incA", "costA"),
                                Area(UUID.randomUUID().toString(), userId, "Modelo B", "clientesB", "relB", "chanB", "valueB", "actB", "resoB", "partB", "incB", "costB"),
                                Area(UUID.randomUUID().toString(), userId, "Modelo C", "clientesC", "relC", "chanC", "valueC", "actC", "resoC", "partC", "incC", "costC")
                        )
                    }
                }
            }

            mapModels = areaDao.getModelsByUserId(userId).map { it.name to it.id }.toMap()

            uiThread {
                loadEventOnClickNewModel()
                loadEventOnClickDeleteModel()
                loadSpinnerModelos()
            }
        }
    }


    private fun persistServerInfo(){
        val services = Services(this.view!!.context)
        val botigocontigoApi = services.botigocontigoApi()
        doAsync {
            val models = areaDao.getModelsByUserId(userId)
            botigocontigoApi.areasSaveAll(models.toTypedArray())
        }


    }

    override fun onDestroy() {
        super.onDestroy()
    }


}
