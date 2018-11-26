package com.botigocontigo.alfred.areas

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.botigocontigo.alfred.R
import com.botigocontigo.alfred.storage.db.AppDatabase
import com.botigocontigo.alfred.storage.db.dao.AreaDao
import com.botigocontigo.alfred.storage.db.entities.Area
import kotlinx.android.synthetic.main.dialog_form_model.view.*
import org.jetbrains.anko.AlertBuilder
import org.jetbrains.anko.AlertDialogBuilder
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


private const val ARG_areaName = "areaName"

class DetailAreaFragment : Fragment() {
    private var viewFragDetail: View? = null
    private var areaName: String? = null
    private var model: Area? = null

    private var txtAreaDetail: TextView? = null
    private var editAreaDetail: EditText? = null

    private var areasMap: Map<String?, Int?> = emptyMap()

    private lateinit var areaDao: AreaDao

    private fun toast(msg:String){
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            areaName = it.getString(ARG_areaName)
            val db = AppDatabase.getInstance(context!!)
            areaDao = db.areaDao()

            doAsync {
                Log.i("DAOO CREATE: ", areaDao.getAll().toString())
            }
        }

        areasMap=hashMapOf(
                "Segmento Clientes" to 1,
                "Relaciones" to 2,
                "Canales" to 3,
                "Propuesta de Valor" to 4,
                "Actividades" to 5,
                "Recursos" to 6,
                "Socios Clave" to 7,
                "Fuentes de Ingreso" to 8,
                "Costos" to 9
                )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewFragDetail = inflater.inflate(R.layout.fragment_detail_area, container, false)

        //Pongo el titulo del area en el layout
        viewFragDetail?.findViewById<TextView>(R.id.txtNombre)?.text = areaName

        //cargo texto area detail
        txtAreaDetail = viewFragDetail?.findViewById<TextView>(R.id.txtAreaDetail)

        when(areasMap.get(areaName)){
            1->txtAreaDetail?.text = model?.clients
            2->txtAreaDetail?.text = model?.relationships
            3->txtAreaDetail?.text = model?.channels
            4->txtAreaDetail?.text = model?.valueProposition
            5->txtAreaDetail?.text = model?.activities
            6->txtAreaDetail?.text = model?.resources
            7->txtAreaDetail?.text = model?.partners
            8->txtAreaDetail?.text = model?.income
            9->txtAreaDetail?.text = model?.costs
        }


        loadEventOnClickEditArea()

        return viewFragDetail
    }

    private fun loadEventOnClickEditArea() {
        viewFragDetail?.findViewById<Button>(R.id.btnEdit)!!.setOnClickListener {
            val diagView: View = LayoutInflater.from(context).inflate(R.layout.dialog_form_area, null)
            val mBuilder = AlertDialog.Builder(context!!).setView(diagView)

            editAreaDetail = diagView.findViewById<TextInputEditText>(R.id.editAreaDetail)
            editAreaDetail?.setText(txtAreaDetail?.text)

            mBuilder.setTitle(areaName)
            val mAlertDialog = mBuilder.show()

            diagView.btnCancel.setOnClickListener {
                mAlertDialog.dismiss()
            }

            diagView.btnOk.setOnClickListener {
                mAlertDialog.dismiss()
                //Edito el area
                editArea(mAlertDialog,diagView)

                txtAreaDetail?.text= editAreaDetail?.text
            }
        }
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

    companion object {
        @JvmStatic
        fun newInstance(areaName: String, area: Area?) =
                DetailAreaFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_areaName, areaName)
                        model = area
                    }
                }
    }

    private fun editArea(alertDialog: AlertDialog, viewDialog: View) {
        var msg: String? = null

        var newDetail: String? = viewDialog.findViewById<TextInputEditText>(R.id.editAreaDetail).text.toString()

        Log.i("CREATE: ", newDetail)

        if (newDetail == "")
            msg = "Escriba un nombre para el modelo"

        if (msg != null) {
            toast("Error: " + msg)
        } else {

            val newModel = model

            Log.i("MODEL: ", model.toString())

            when(areasMap.get(areaName)){
                1->newModel?.clients=newDetail
                2->newModel?.relationships=newDetail
                3->newModel?.channels=newDetail
                4->newModel?.valueProposition=newDetail
                5->newModel?.activities=newDetail
                6->newModel?.resources=newDetail
                7->newModel?.partners=newDetail
                8->newModel?.income=newDetail
                9->newModel?.costs=newDetail
            }

            Log.i("NEWMODEL: ", newModel.toString())
            doAsync {
                Log.i("DAOO: ", areaDao.getAll().toString())
                areaDao.update(newModel!!)
            Log.i("DAOO: ", areaDao.getAll().toString())}

        }
    }
}
