package com.botigocontigo.alfred

import android.net.Uri
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.botigocontigo.alfred.storage.db.AppDatabase
import com.botigocontigo.alfred.storage.db.dao.PlanDao
import com.botigocontigo.alfred.storage.db.entities.Plan
import com.botigocontigo.alfred.storage.db.entities.Task
import com.botigocontigo.alfred.tasks.TaskAdapter
import com.botigocontigo.alfred.tasks.TaskDialogMaker
import kotlinx.android.synthetic.main.dialog_form_task.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class TasksFragment : Fragment() {

    private var vfrag: View? = null
    private var taskAdapter: TaskAdapter? = null
    private var recycler: RecyclerView? = null
    private var mListener: OnFragmentInteractionListener? = null

    private lateinit var planDao: PlanDao
//    private lateinit var taskDao: TaskDao

    private var mapPlans: Map<String, Int> = emptyMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val db = AppDatabase.getInstance(context!!)
            planDao = db.planDao()
//            taskDao = db.taskDao()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        vfrag = inflater.inflate(R.layout.fragment_tasks, container, false)

        doAsync {
            planDao.insertAll(
                    Plan(1, "Plan de Capacitacion", "Aprendizaje", "Hugo", "hugos@f.com", Date(1565481600000), arrayListOf(
                            Task(1, "Enseñar Computacion 2", "Semanalmente", 2, "Hugo", "Carlos", false)
                    )),
                    Plan(2, "Plan de Negocio", "Rentabilidad", "Carlos", "carl25@ww.com", Date(1552262400000), arrayListOf(
                            Task(1, "Contratar Personal", "Mensualmente", 5, "Hugo", "Norma", false),
                            Task(2, "Vender Productos", "Semanalmente", 10, "Carlos", "Hugo", false)
                    )),
                    Plan(3, "Plan de Ejercitacion", "Salud", "Norma", "normal@qq.com", Date(1552176000000), arrayListOf(
                            Task(1, "Correr 10k", "Semanalmente", 2, "Norma", "Carlos", false),
                            Task(2, "Chequeo medico", "Anualmente", 5, "Norma", "Carlos", false)
                    ))
            )

//            taskDao.insertAll(
//                    Task(1, "Correr 10k", "Semanalmente", 2, "Norma", "Carlos", false, 3),
//                    Task(2, "Contratar Personal", "Mensualmente", 5, "Hugo", "Norma", false, 2),
//                    Task(3, "Enseñar Computacion 2", "Semanalmente", 2, "Hugo", "Carlos", false, 1),
//                    Task(4, "Vender Productos", "Semanalmente", 10, "Carlos", "Hugo", false, 2),
//                    Task(5, "Chequeo medico", "Anualmente", 5, "Norma", "Carlos", false, 3)
//            )
            Log.i("Menu", "Inicio")
            Log.i("Plans count", planDao.getAll().size.toString())
//            Log.i("Tasks count", taskDao.getAll().size.toString())

            mapPlans = planDao.getAll().map { it.name to it.id }.toMap()

            uiThread { setUpUIElements() }
        }

        return vfrag
    }

    private fun setUpUIElements() {

        recycler = vfrag!!.findViewById<RecyclerView>(R.id.recyclerTasks)
        recycler!!.layoutManager = LinearLayoutManager(context)
        recycler!!.setHasFixedSize(true)

        taskAdapter = TaskAdapter(::switchFlipper)

        val adapterPlan = ArrayAdapter(context!!, R.layout.spinner_tasks, mapPlans.keys.toList())
        adapterPlan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinnerPlans = vfrag!!.findViewById<Spinner>(R.id.spinner_plans)
        spinnerPlans.adapter = adapterPlan
        spinnerPlans.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }
            override fun onItemSelected(parent: AdapterView<*>?, view2: View?, position: Int, id: Long) {
                val plan = spinnerPlans.selectedItem.toString()
                Log.i("SelectedSpinnerPlan", "$plan - id: ${mapPlans.getValue(plan)}")
                doAsync {
//                    val tasks = taskDao!!.getAllByPlanId(mapPlans.getValue(plan))
                    val tasks = planDao.findTasksById(mapPlans.getValue(plan))
                    uiThread {
                        recycler!!.adapter = taskAdapter!!.setDataset(tasks)
                    }
                }
            }
        }

        vfrag!!.findViewById<ImageView>(R.id.btn_newTask).setOnClickListener { loadNewTaskEvent() }

        vfrag!!.findViewById<ImageView>(R.id.btn_edit_task).apply {
            bringToFront()
            setOnClickListener { loadEditTaskEvent() }
        }

        vfrag!!.findViewById<ImageView>(R.id.btn_deleteTask).apply {
            bringToFront()
            setOnClickListener { loadDeleteTasksEvent() }
        }

        vfrag!!.findViewById<ImageView>(R.id.btn_deleteTask).apply {
            bringToFront()
            setOnClickListener { loadDeleteTasksEvent() }
        }
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

        fun newInstance(param1: String, param2: String): TasksFragment {
            val fragment = TasksFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }


    // Desmarca las tareas seleccionadas y cambia el flipper al default
    private fun deselectTasks() {
        taskAdapter?.deselectTask()
        switchFlipper(0)
    }

    private fun switchFlipper(child: Int) {
        vfrag!!.findViewById<ViewFlipper>(R.id.vf_task_options).apply {
            setFlipInterval(0)
            inAnimation = null
            outAnimation = null
            displayedChild = child
        }
    }

    // Funciones que usa la actividad Padre
    fun selectedTaskCount() = taskAdapter!!.selectedItems.size

    fun unCheckTasks() = deselectTasks()

    private fun loadNewTaskEvent() {
        val planSelected = vfrag!!.findViewById<Spinner>(R.id.spinner_plans).selectedItem.toString()
        TaskDialogMaker(context!!, planSelected, mapPlans.keys.toList(),
                "", null, 1, null, "CREATE",
                { alertDialog: AlertDialog, _ -> alertDialog.dismiss() },
                { alertDialog: AlertDialog, view: View ->
                    createTask(alertDialog, view, { message: String ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    })
                }
        )
    }

    private fun loadEditTaskEvent() {
        val task: Task = taskAdapter!!.selectedItems.first()
        val planSelected = vfrag!!.findViewById<Spinner>(R.id.spinner_plans).selectedItem.toString()
        TaskDialogMaker(context!!, planSelected, mapPlans.keys.toList(),
                task.name!!, task.frecType, task.frecValue!!, task.responsibleId,
                "UPDATE",
                { alertDialog: AlertDialog, _ -> alertDialog.dismiss() },
                { alertDialog: AlertDialog, _: View ->
                    alertDialog.dismiss()
                }
        )
    }

    private fun loadDeleteTasksEvent() {
        val selectedCount = taskAdapter!!.selectedItems.size

        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Alerta")
        builder.setMessage("¿Está seguro que desea eliminar ${
            if (selectedCount > 1) "las $selectedCount tareas" else "1 tarea"
        } del plan?")
        builder.setPositiveButton("Si"){ dialog, which ->
            // Eliminar asincronicamente
            doAsync {
//                val tasks = planDao.findTasksById()
//                taskDao!!.deleteAll()
            }
            taskAdapter?.deleteTasks()
            taskAdapter?.notifyDataSetChanged()
            switchFlipper(0)
            Toast.makeText(context,"Tarea Eliminada",Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){ _, _ -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun createTask(alertDialog: AlertDialog, viewDialog: View, fnError: (String) -> Unit) {

        val planName: String = viewDialog.spinner_dialog_plans.selectedItem as String
        val taskName: String = viewDialog.et_dialog_nameTask.text.toString()
        val frecType: String = viewDialog.spinner_dialog_interval.selectedItem as String
        val frecValue: Int? = viewDialog.et_dialog_interval.text.toString().toInt()
        val responsible = "yo"

        var msg: String? = null

        if (planName == "")
            msg = "Falta completar el Plan"
        if (taskName == "")
            msg = "Falta completar el nombre de la tarea"
        if (frecType == "")
            msg = "Falta completar el periodo de la tarea"
        if (frecValue == null || frecValue <= 0)
            msg = "Falta completar las repeticiones de la tarea"
        if (responsible == "")
            msg = "Falta completar el responsable de la tarea"

        if (msg != null) {
            fnError(msg)
        } else {
            val planId = mapPlans.getValue(planName)
            var tasks = planDao.findTasksById(planId)
            val id = when(tasks.count()) {
                0 -> 0
                else -> tasks.map { it.id }.max()!!.plus(1)
            }
            Log.i("Last Id", id.toString())
            val newTask = Task(
                    id = id,
                    name = taskName,
                    frecType = frecType,
                    frecValue = frecValue!!,
                    responsibleId = responsible,
                    supervisorId = "super",
                    completed = false
//                    ,
//                    planId = mapPlans.getValue(planName)
            )
            doAsync {
                tasks += newTask
                planDao.updateTasksFromPlan(planId, tasks)
//                taskDao!!.insertAll(newTask)
//                val tasks = taskDao!!.getAllByPlanId(mapPlans.getValue(planName))
                uiThread {
                    taskAdapter!!.setDataset(tasks)
                    taskAdapter!!.notifyDataSetChanged()
                    alertDialog.dismiss()
                }
            }
        }
    }
}