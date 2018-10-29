package com.botigocontigo.alfred.tasks

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner

import com.botigocontigo.alfred.R
import kotlinx.android.synthetic.main.dialog_form_task.view.*

class TasksFragment : Fragment() {

    private var mParam1: String? = null
    private var mParam2: String? = null

    private val plans: ArrayList<String> = arrayListOf(
            "Plan de Administracion",
            "Plan de Emprendimiento",
            "Plan de Viaje",
            "Plan de Capacitacion"
    )

    private var tasks: ArrayList<Task> = tasksDB

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
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_tasks, container, false)

//        loadEventOnClickNewTask(v) // Cargo el evento del boton de crear tarea

        loadSpinnerPlans(v) // Cargo los elementos que deben ir dentro del spinner

        loadTasksOnRecyclerView(v) // Cargo la lista de tareas en el RecyclerView

        loadEventOnClickNewTask(v)

        return v
    }

    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
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

        fun newInstance(param1: String, param2: String): TasksFragment {
            val fragment = TasksFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    private fun loadSpinnerPlans(view: View) {
        val spinner = view.findViewById<Spinner>(R.id.spinner_planes)
        spinner!!.adapter = ArrayAdapter(context, R.layout.spinner_tasks, plans).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

//    private fun loadEventOnClickNewTask(view: View) {
//        view.findViewById<ImageButton>(R.id.btnNewTask)!!.setOnClickListener {
//            activity?.supportFragmentManager
//                    ?.beginTransaction()
//                    ?.replace(R.id.frg_tasks, TaskFormFragment.newInstance(plans), "Lalala")
//                    ?.commit()
//        }
//    }

    private fun loadTasksOnRecyclerView(view: View) {
        view.findViewById<RecyclerView>(R.id.recyclerTasks).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = TaskAdapter(tasks)
        }
    }

    private fun loadEventOnClickNewTask(view: View) {
        view.findViewById<ImageButton>(R.id.btnNewTask)!!.setOnClickListener {
            // dw = dialogView
            val dw: View = LayoutInflater.from(context).inflate(R.layout.dialog_form_task, null)
            val mBuilder = AlertDialog.Builder(context!!).setView(dw)

            val mSpinner: Spinner = dw.findViewById(R.id.spPlans)
            mSpinner.adapter = ArrayAdapter(context, R.layout.spinner_tasks, plans).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            val spRepeatTask: Spinner = dw.findViewById(R.id.spRepeat)
            spRepeatTask.adapter = ArrayAdapter(context, R.layout.spinner_tasks, resources.getStringArray(R.array.repeat_task)).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            val spRememberTask: Spinner = dw.findViewById(R.id.spRemember)
            spRememberTask.adapter = ArrayAdapter(context, R.layout.spinner_tasks, resources.getStringArray(R.array.remember_task)).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            dw.btnOk.text = "CREAR"
            dw.btnCancel.text = "CANCELAR"

            val mAlertDialog = mBuilder.show()

            dw.btnCancel.setOnClickListener {
                mAlertDialog.dismiss()
            }

            dw.btnOk.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }

}