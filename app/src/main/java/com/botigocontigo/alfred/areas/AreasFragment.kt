package com.botigocontigo.alfred.areas

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.ImageButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.widget.Button
import android.widget.Toast


import com.botigocontigo.alfred.R
import kotlinx.android.synthetic.main.dialog_form_model.view.*





// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AreasFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private val modelos_negocio: Array<String> =
            arrayOf("Modelo de Afiliacion", "Modelo Freemium",
                    "Modelo de Subastas", "Modelo de Venta Directa",
                    "Modelo de Franquicia", "Modelo de Cola Larga")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_areas, container, false)

        loadSpinnerModelos(v) // Cargo los elementos que deben ir dentro del spinner

        loadEventOnClickNewModel(v)
        loadEventOnClickAreaDetail(v)

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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AreasFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                AreasFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private fun loadSpinnerModelos(view: View) {
        val spinner = view.findViewById<Spinner>(R.id.spinner_modelos)
        spinner!!.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, modelos_negocio).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun loadEventOnClickNewModel(view: View) {
        view.findViewById<ImageButton>(R.id.btnNewModel)!!.setOnClickListener {
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

            //Cambio de fragment
//            val detailFragment = DetailAreaFragment()
//            val manager = childFragmentManager
//            manager.beginTransaction().
//                    replace(R.id.layout_detail_fragment, detailFragment, detailFragment.tag)
//                    .commit()
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
