package com.botigocontigo.alfred.foda

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.botigocontigo.alfred.R

class FodaFragment : Fragment() {

    private var mParam1: String? = null
    private var mParam2: String? = null

    val arrayOfStrengths = arrayOf("fortaleza1", "fortaleza2", "fortaleza3", "fortaleza4", "fortaleza5")
    val arrayOfOpportunities = arrayOf("oportunidad1", "oportunidad2", "oportunidad3", "oportunidad4", "oportunidad5")
    val arrayOfWeaknesses = arrayOf("debilidad1", "debilidad2", "debilidad2", "debilidad2", "debilidad2")
    val arrayOfThreats = arrayOf("amenaza1", "amenaza1", "amenaza1", "amenaza1", "amenaza1", "amenaza1")
    val dimensions = arrayOf(
            Dimension("Fortalezas", "Interna", arrayOfStrengths),
            Dimension("Oportunidades", "Externa", arrayOfOpportunities),
            Dimension("Debilidades", "Interna", arrayOfWeaknesses),
            Dimension("Amenazas", "Externa", arrayOfThreats)
    )

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_foda, container, false)
        loadRecyclerView(view)

        return view
    }


    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    private fun loadRecyclerView(view: View) {
        view.findViewById<RecyclerView>(R.id.recyclerFoda).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
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