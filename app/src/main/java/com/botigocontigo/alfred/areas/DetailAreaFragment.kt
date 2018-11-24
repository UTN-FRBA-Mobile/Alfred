package com.botigocontigo.alfred.areas

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
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


private const val ARG_areaName = "areaName"


class DetailAreaFragment : Fragment() {
    //private var vfrag: View? = null
    private var areaName: String? = null

    private var listener: OnFragmentInteractionListener? = null

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
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_detail_area, container, false)

        //Pongo el titulo del area en el layout
        v.findViewById<TextView>(R.id.txtNombre).text = areaName

        //val name = areaDao.findById(1).name

        //v.findViewById<TextView>(R.id.txtDescripcion).text=name
        return v
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
