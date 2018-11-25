package com.botigocontigo.alfred.areas

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.botigocontigo.alfred.R
import com.botigocontigo.alfred.storage.db.AppDatabase
import com.botigocontigo.alfred.storage.db.dao.AreaDao
import com.botigocontigo.alfred.storage.db.entities.Area
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


private const val ARG_areaName = "areaName"

class DetailAreaFragment : Fragment() {
    private var viewFragDetail: View? = null
    private var areaName: String? = null

    private var listener: OnFragmentInteractionListener? = null

    private fun toast(msg:String){
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            areaName = it.getString(ARG_areaName)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewFragDetail = inflater.inflate(R.layout.fragment_detail_area, container, false)

        //Pongo el titulo del area en el layout
        viewFragDetail?.findViewById<TextView>(R.id.txtNombre)?.text = areaName

        return viewFragDetail
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
        fun newInstance(areaName: String) =
                DetailAreaFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_areaName, areaName)
                    }
                }
    }
}
