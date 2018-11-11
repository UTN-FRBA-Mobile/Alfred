package com.botigocontigo.alfred

import android.net.Uri
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.botigocontigo.alfred.tasks.*
import kotlinx.android.synthetic.main.dialog_form_task.view.*

class TasksFragment : Fragment() {

    private var mParam1: String? = null
    private var mParam2: String? = null

    private var recycler: RecyclerView? = null
    private var adapterRecyclerTasks: TaskAdapter? = null
    private var viewFragment: View? = null
    private var positionPlan: Int = 0

    private val plans: ArrayList<Plan> = plansDB

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
        viewFragment = inflater.inflate(R.layout.fragment_tasks, container, false)

        // Cargo los elementos del spinner con su respectivo evento onclick
        loadSpinnerPlans()
        loadEventOnClickNewTask()
        loadEventsOnClickFlipper()

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

    private fun loadSpinnerPlans() {
        val spinner = viewFragment!!.findViewById<Spinner>(R.id.spinner_plans)
        spinner.adapter = ArrayAdapter(context!!, R.layout.spinner_tasks, plans.map { p -> p.name }).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }
            override fun onItemSelected(parent: AdapterView<*>?, view2: View?, position: Int, id: Long) {
                loadTasksByPlan(plans[position])
                positionPlan = position
            }
        }
    }

    private fun loadTasksByPlan(plan: Plan) {
        val rv = viewFragment!!.findViewById<RecyclerView>(R.id.recyclerTasks)
        adapterRecyclerTasks = TaskAdapter(plan.tasks, viewFragment!!)
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapterRecyclerTasks
    }


    /**
     * Cargo los eventos onClick de los botones del ViewFlipper
     */
    private fun loadEventsOnClickFlipper() {
        viewFragment!!.findViewById<ImageView>(R.id.btn_edit_task).apply {
            bringToFront()
            // Edita una tarea elegida, abriendo un popup
            setOnClickListener {
                val task: Task? = adapterRecyclerTasks?.selectedItems?.first()
                TaskDialogMaker(
                        context!!,
                        positionPlan,
                        plans.map { p -> p.name },
                        task?.name,
                        task?.interval,
                        resources.getStringArray(R.array.task_unit_interval).toCollection(ArrayList()),
                        task?.iterations,
                        task?.responsible,
                        { alertDialog: AlertDialog, _ -> alertDialog.dismiss() },
                        { alertDialog: AlertDialog, view: View ->
                            if (createNewTask(view)) alertDialog.dismiss()
                        },
                        "UPDATE"
                )
            }
        }
        viewFragment!!.findViewById<ImageView>(R.id.btn_deleteTask).apply {
            bringToFront()
            setOnClickListener { deleteTasks() }
        }
        viewFragment!!.findViewById<ImageView>(R.id.btn_deleteAllTasks).apply {
            bringToFront()
            setOnClickListener { deleteTasks() }
        }
    }

    /**
     * Eliminar Tarea/s
     */
    private fun deleteTasks() {
        adapterRecyclerTasks?.deleteTasks()
        adapterRecyclerTasks?.notifyDataSetChanged()

        viewFragment!!.findViewById<ViewFlipper>(R.id.vf_task_options).apply {
            setFlipInterval(0)
            inAnimation = null
            outAnimation = null
            displayedChild = 0
        }
    }

    /**
     * Desmarca las tareas seleccionadas y cambia el flipper al default
     */
    private fun deselectTasks() {
        adapterRecyclerTasks?.deselectTask()
        viewFragment!!.findViewById<ViewFlipper>(R.id.vf_task_options).apply {
            setFlipInterval(0)
            inAnimation = null
            outAnimation = null
            displayedChild = 0
        }
    }

    /**
     * Funciones que usa el actividad Padre
     */
    fun selectedTaskCount() = adapterRecyclerTasks?.selectedItems?.size

    fun unCheckTasks() = deselectTasks()


    /**
     * Cargo el evento de crear un Dialog al momento de hacer click en el boton de agregar
     */
    private fun loadEventOnClickNewTask() {
        viewFragment!!.findViewById<ImageButton>(R.id.btn_newTask)!!.setOnClickListener {
            TaskDialogMaker(
                    context!!,
                    positionPlan,
                    plans.map { p -> p.name },
                    null,
                    0,
                    resources.getStringArray(R.array.task_unit_interval).toCollection(ArrayList()),
                    1,
                    null,
                    { alertDialog: AlertDialog, _ -> alertDialog.dismiss() },
                    { alertDialog: AlertDialog, view: View ->
                        if (createNewTask(view)) alertDialog.dismiss()
                    },
                    "CREATE"
            )
        }
    }

    /**
     * Crear nueva tarea
     */
    fun createNewTask(dialog: View): Boolean {
        // validar si estan los datos necesarios
        val plan: String = dialog.spinner_dialog_plans.selectedItem as String
        val name: String = dialog.et_dialog_nameTask.text.toString()
        val iterations: String = dialog.et_dialog_interval.text.toString()
        var msg: String? = null

        val lastIdTask = plans.filter { p -> p.name == plan }[0].tasks.map { t -> t.id }.max()

        if (plan == "") msg = "Falta completar el Plan"
        if (name == "") msg = "Falta completar el nombre"
        if (iterations == "" || iterations.toInt() <= 0) msg = "Falta completar las iteraciones"

        if (msg != null) {
            Toast.makeText(context, msg, 3000).show()
            return false
        }

        plans.filter { p -> p.name == plan }[0].tasks.add(
                Task(lastIdTask!! + 1, name, iterations.toInt(), 0, "admin"))

        adapterRecyclerTasks!!.notifyDataSetChanged()

        viewFragment!!.findViewById<ViewFlipper>(R.id.vf_task_options).apply {
            setFlipInterval(0)
            inAnimation = null
            outAnimation = null
            displayedChild = 0
        }
        return true
    }

    /**
     * Actualizar la tarea
     */
    private fun updateTask() {

    }


}