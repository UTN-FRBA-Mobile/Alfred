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
import android.widget.*

import com.botigocontigo.alfred.R
import kotlinx.android.synthetic.main.dialog_form_task.view.*

class TasksFragment : Fragment() {

    private var mParam1: String? = null
    private var mParam2: String? = null

    private var recycler: RecyclerView? = null
    private var adapterRv: TaskAdapter? = null
    //private var viewFragment: View? = null

    private val plans: ArrayList<Plan> = plansDB

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
        val viewFragment = inflater.inflate(R.layout.fragment_tasks, container, false)

        loadSpinnerPlans(viewFragment)         // Cargo los elementos que deben ir dentro del spinner
        //loadTasksOnRecyclerView(viewFragment)  // Cargo la lista de tareas en el RecyclerView
        loadEventOnClickNewTask(viewFragment)
        loadEventsOnClickFlipper(viewFragment)

        return viewFragment
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
        spinner.adapter = ArrayAdapter(context!!, R.layout.spinner_tasks, plans.map { p -> p.name }).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view2: View?, position: Int, id: Long) {
                //Toast.makeText(context,id.toString(),2000).show()
                loadTasksByPlan(plans[position], view)
            }
        }
    }

    private fun loadTasksByPlan(plan: Plan, view: View) {
        val rv = view.findViewById<RecyclerView>(R.id.recyclerTasks)
        adapterRv = TaskAdapter(plan.tasks, view)
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapterRv
    }


    /**
     * Cargo los eventos onClick de los botones del ViewFlipper
     */
    private fun loadEventsOnClickFlipper(view: View) {
        view.findViewById<ImageView>(R.id.btn_edit_task).apply {
            bringToFront()
            setOnClickListener { editTask(view) }
        }
        view.findViewById<ImageView>(R.id.btn_del_task).apply {
            bringToFront()
            setOnClickListener { deleteTasks(view) }
        }
        view.findViewById<ImageView>(R.id.btn_del_all_task).apply {
            bringToFront()
            setOnClickListener { deleteTasks(view) }
        }
        view.findViewById<ImageView>(R.id.btn_undo_task).apply {
            bringToFront()
            setOnClickListener { deselectTasks(view) }
        }
        view.findViewById<ImageView>(R.id.btn_undo_all_task).apply {
            bringToFront()
            setOnClickListener { deselectTasks(view) }
        }
    }

    /**
     * Edita una tarea elegida, abriendo un popup
     */
    private fun editTask(view: View) {
        Toast.makeText(context, "Tarea Editada", 2000).show()
    }

    /**
     * Eliminar Tarea/s
     */
    private fun deleteTasks(view: View) {
        //Toast.makeText(context, "Tarea Eliminada", 2000).show()
        adapterRv?.deleteTasks()
        adapterRv?.notifyDataSetChanged()

        view.findViewById<ViewFlipper>(R.id.vf_task_options).apply {
            setFlipInterval(0)
            inAnimation = null
            outAnimation = null
            displayedChild = 0
        }
    }

    /**
     * Desmarca las tareas seleccionadas y cambia el flipper al default
     */
    private fun deselectTasks(view: View) {
        adapterRv?.deselectTask()
        view.findViewById<ViewFlipper>(R.id.vf_task_options).apply {
            setFlipInterval(0)
            inAnimation = null
            outAnimation = null
            displayedChild = 0
        }
    }


    /**
     * Cargo el evento de crear un Dialog al momento de hacer click en el boton de agregar
     */
    private fun loadEventOnClickNewTask(view: View) {
        view.findViewById<ImageButton>(R.id.btnNewTask)!!.setOnClickListener {
            // dw = dialogView
            val dw: View = LayoutInflater.from(context).inflate(R.layout.dialog_form_task, null)
            val mBuilder = AlertDialog.Builder(context!!).setView(dw)

            val mSpinner = dw.findViewById<Spinner>(R.id.spPlans)
            mSpinner.adapter = ArrayAdapter(context, R.layout.spinner_tasks, plans.map { p -> p.name }).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            val spRepeatTask = dw.findViewById<Spinner>(R.id.spRepeat)
            spRepeatTask.adapter = ArrayAdapter(context, R.layout.spinner_tasks, resources.getStringArray(R.array.unit_time)).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            val spRememberTask = dw.findViewById<Spinner>(R.id.spRemember)
            spRememberTask.adapter = ArrayAdapter(context, R.layout.spinner_tasks, resources.getStringArray(R.array.unit_time)).apply {
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