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


private const val ARG_areaName = "areaName"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DetailAreaFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DetailAreaFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DetailAreaFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
        val v = inflater.inflate(R.layout.fragment_detail_area, container, false)

        //Pongo el titulo del area en el layout
        v.findViewById<TextView>(R.id.txtNombre).text = areaName
        return v
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            //throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
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
