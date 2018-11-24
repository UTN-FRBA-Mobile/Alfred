package com.botigocontigo.alfred.foda

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.botigocontigo.alfred.R
import com.botigocontigo.alfred.storage.db.AppDatabase
import com.botigocontigo.alfred.storage.db.dao.DimensionDao
import com.botigocontigo.alfred.storage.db.entities.Dimension
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class FodaFragment : Fragment() {

    private lateinit var dimensionDao: DimensionDao

    private var mParam2: String? = null
    private var vfoda: View? = null

    lateinit var dimensions:MutableList<Dimension> ;
    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val db = AppDatabase.getInstance(context!!)
            dimensionDao = db.dimensionDao()
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        vfoda= inflater.inflate(R.layout.activity_foda, container, false)
        doAsync {
            dimensionDao.insertAll(
                    Dimension(1,"fortaleza1", "Interna",1,"Fortalezas", Date()),
                    Dimension(2,"oportunidad1", "Externa", 1,"Oportunidades", Date()),
                    Dimension(3,"debilidad1", "Interna",1, "Debilidades", Date()),
                    Dimension(4,"amenaza1", "Externa",1, "Amenazas", Date())
            )
            Log.i("Menu", "Inicio")
            Log.i("Dimensions Count", dimensionDao.getAll().size.toString())

            dimensions = dimensionDao.getAll() as MutableList<Dimension>

            uiThread {         loadRecyclerView() }
        }


        return vfoda
    }


    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    private fun loadRecyclerView() {
        vfoda!!.findViewById<RecyclerView>(R.id.recyclerFoda).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context) as RecyclerView.LayoutManager?
            adapter = FodaAdapter(context,dimensions)
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

        fun newInstance(param1: String, param2: String):  FodaFragment {
            val fragment = FodaFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }

    }

}