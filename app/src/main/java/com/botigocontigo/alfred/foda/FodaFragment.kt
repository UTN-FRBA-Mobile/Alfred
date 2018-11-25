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
import com.botigocontigo.alfred.Services
import com.botigocontigo.alfred.backend.FodaGetCallbacks
import com.botigocontigo.alfred.backend.FodaPostCallbacks
import com.botigocontigo.alfred.storage.db.AppDatabase
import com.botigocontigo.alfred.storage.db.dao.DimensionDao
import com.botigocontigo.alfred.storage.db.entities.DimensionDataBase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class FodaFragment : Fragment() {

    private lateinit var dimensionDao: DimensionDao

    private var mParam2: String? = null
    private var vfoda: View? = null

    lateinit var dimensionsDataBase:MutableList<DimensionDataBase>
    lateinit var dimensions:MutableList<Dimension>


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
            dimensions = getDimensions(1);
            uiThread {         loadRecyclerView() }
        }


        return vfoda
    }

    private fun getDimensions(i: Int): MutableList<Dimension> {
        //esto deberia pegarle a la api
        val apiInfo  = getServerInfo( vfoda!!.context)
       dimensionsDataBase = dimensionDao.getAll() as MutableList<DimensionDataBase>
        var newDimensions:MutableList<Dimension> =arrayListOf()

        if (dimensionsDataBase.isEmpty()) {
            dimensionDao.insertAll(
                    DimensionDataBase(1, Gson().toJson(arrayListOf("Notoriedad de la marca a nivel nacional","Equipo Profesional con amplia Experiencia","Alta fidelización de nuestros clientes","Especializacion de producto","")).toString(), "1", TypesEnum.FORTALEZAS.type, Date()),
                    DimensionDataBase(2, Gson().toJson(arrayListOf("Tendencia favorable en el mercado","Aparición de nuevos segmentos","Probabilidad de establecer alianzas estrategicas","")).toString(), "1", TypesEnum.OPORTUNIDADES.type, Date()),
                    DimensionDataBase(3, Gson().toJson(arrayListOf("Falta de financiación","Costes unitarios elevados","Cartera de productos limitada","")).toString(), "1", TypesEnum.DEBILIDADES.type, Date()),
                    DimensionDataBase(4, Gson().toJson(arrayListOf("Entrada de nuevos competidores","Nueva legislación que afecta al sector","Cambio de hábitos de los consumidores","Globalización de mercados","")).toString(), "1", TypesEnum.AMENAZAS.type, Date())
            )
            Log.i("Menu", "Inicio")
            Log.i("Dimensions Count", dimensionDao.getAll().size.toString())
            dimensionsDataBase = dimensionDao.getAll() as MutableList<DimensionDataBase>
        }
        if (dimensionsDataBase.isNotEmpty()) {
            newDimensions = parseDimensionsfromDimensionDataBase(dimensionsDataBase)
        }

    return newDimensions
    }

    private fun getServerInfo(context: Context) {

        val services = Services(context)

        val fodaGetCallbacks= FodaGetCallbacks()

        val botigocontigoApi = services.botigocontigoApi()
        botigocontigoApi.fodaGetAll().call(fodaGetCallbacks)
    }
    private fun parseDimensionsfromDimensionDataBase(newDimensions: MutableList<DimensionDataBase>): MutableList<Dimension> {
        var postaDimensions:MutableList<Dimension> = arrayListOf()
            newDimensions.forEach{dimensionsDataBase ->
                postaDimensions.add(
                            Dimension(
                                    dimensionsDataBase.id,
                                    dimensionsDataBase.dimension_name,
                                    dimensionsDataBase.userId,
                                    parseListOfStrings(dimensionsDataBase.name)
                            )
                )

            }
        return postaDimensions

    }

    private fun parseListOfStrings(name: String): Array<String> {
        val listType = object : TypeToken<List<String>>() {}.getType()
        val gson: List<String> = Gson().fromJson(name,listType)
        return  gson.toTypedArray()
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

    override fun onDestroyView() {
        super.onDestroyView()
        doAsync {
            persistDimensionDatabaseFromDimension()
            persistOnServer()
            //post to API
        }
    }

    private fun persistOnServer() {

        val services = Services(this.vfoda!!.context)

        val fodaPostCallbacks= FodaPostCallbacks()

        val botigocontigoApi = services.botigocontigoApi()
        botigocontigoApi.fodaSaveAll(this.dimensions ).call(fodaPostCallbacks)
    }

    private fun persistDimensionDatabaseFromDimension() {
        dimensions.forEach { dimension -> dimensionDao.update(DimensionDataBase(
                dimension.id,
                parseJsonFromDimension(dimension.descriptions),
                dimension.userId,
                dimension.type,
                Date()
        )) }
    }

    private fun parseJsonFromDimension(array: Array<String>): String {
       return Gson().toJson(array).toString()
    }

}