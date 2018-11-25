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


private const val ARG_areaName = "areaName"
private const val ARG_areaDetail = "areaDetail"

class DetailAreaFragment : Fragment() {
    private var viewFragDetail: View? = null
    private var areaName: String? = null
    private var areaDetail: String? = null

    private var listener: OnFragmentInteractionListener? = null

    private var txtAreaDetail: TextView? = null
    private var dialogEditArea: AlertDialog? = null
    private var editAreaDetail: EditText? = null

    private fun toast(msg:String){
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            areaName = it.getString(ARG_areaName)
            areaDetail = it.getString(ARG_areaDetail)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewFragDetail = inflater.inflate(R.layout.fragment_detail_area, container, false)

        //Pongo el titulo del area en el layout
        viewFragDetail?.findViewById<TextView>(R.id.txtNombre)?.text = areaName

        //cargo texto area detail
        txtAreaDetail = viewFragDetail?.findViewById<TextView>(R.id.txtAreaDetail)
        txtAreaDetail?.text = areaDetail

        loadEventOnClickEditArea()

        return viewFragDetail
    }

    private fun loadEventOnClickEditArea() {
        viewFragDetail?.findViewById<Button>(R.id.btnEdit)!!.setOnClickListener {
            // dw = dialogView
            val dw: View = LayoutInflater.from(context).inflate(R.layout.dialog_form_area, null)
            val mBuilder = AlertDialog.Builder(context!!).setView(dw)

            editAreaDetail = dw.findViewById<TextInputEditText>(R.id.editAreaDetail)
            editAreaDetail?.setText(txtAreaDetail?.text)

            mBuilder.setTitle("EDITAR" + areaName)
            val mAlertDialog = mBuilder.show()

            dw.btnCancel.setOnClickListener {
                mAlertDialog.dismiss()
            }

            dw.btnOk.setOnClickListener {
                mAlertDialog.dismiss()
                Toast.makeText(context,"Area editada",Toast.LENGTH_SHORT).show()
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
        fun newInstance(areaName: String, areaDetail: String?) =
                DetailAreaFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_areaName, areaName)
                        putString(ARG_areaDetail, areaDetail)
                    }
                }
    }
}
