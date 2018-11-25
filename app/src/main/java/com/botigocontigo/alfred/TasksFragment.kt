package com.botigocontigo.alfred

import android.net.Uri
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.botigocontigo.alfred.backend.BotigocontigoApi
import com.botigocontigo.alfred.backend.Permissions
import com.botigocontigo.alfred.storage.db.AppDatabase
import com.botigocontigo.alfred.storage.db.dao.PlanDao
import com.botigocontigo.alfred.storage.db.dao.TaskDao
import com.botigocontigo.alfred.storage.db.entities.Plan
import com.botigocontigo.alfred.storage.db.entities.Task

import com.botigocontigo.alfred.tasks.TaskAdapter
import com.botigocontigo.alfred.tasks.TaskDialogMaker
import kotlinx.android.synthetic.main.dialog_form_task.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import com.botigocontigo.alfred.backend.PlansGetCallbacks


class TasksFragment : Fragment() {

    private lateinit var api: BotigocontigoApi

    private var vfrag: View? = null
    private var taskAdapter: TaskAdapter? = null
    private var recycler: RecyclerView? = null
    private var mListener: OnFragmentInteractionListener? = null

    private lateinit var planDao: PlanDao
    private lateinit var taskDao: TaskDao

    private var mapPlans: Map<String, String> = emptyMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val db = AppDatabase.getInstance(context!!)
            planDao = db.planDao()
            taskDao = db.taskDao()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        vfrag = inflater.inflate(R.layout.fragment_tasks, container, false)

        container!!.bringToFront()

        val services = Services(inflater.context)
        api = services.botigocontigoApi()

        doAsync {

            api.plansGetAll().call(PlansGetCallbacks(::loadToDb))

            if (planDao.getPlansCount() == 0) {
                planDao.insertAll(
                        Plan("aaa", "Plan Comercial", "Comercio", "Hugo", "hugos@f.com", Date(1565481600000)),
                        Plan("bbb", "Plan de Comunicación", "Recursos Humanos", "Carlos", "carl25@ww.com", Date(1552262400000)),
                        Plan("ccc", "Plan de Administración", "Salud", "Norma", "normal@qq.com", Date(1552176000000))
                )
            }
//
//            taskDao.insertAll(
//                    Task("qwe123", "Correr 10k", "Semanalmente", "2", "Norma", "Carlos", false, "ccc"),
//                    Task("asd123", "Contratar Personal", "Mensualmente", "5", "Hugo", "Norma", false, "bbb"),
//                    Task("zxc123", "Enseñar Computacion 2", "Semanalmente", "2", "Hugo", "Carlos", false, "aaa"),
//                    Task("tyu789", "Vender Productos", "Semanalmente", "10", "Carlos", "Hugo", false, "bbb"),
//                    Task("oiu098", "Chequeo medico", "Anualmente", "5", "Norma", "Carlos", false, "ccc")
//            )

            Log.i("Plans count", planDao.getAll().size.toString())

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

        val adapterPlan = ArrayAdapter<String>(context!!, R.layout.spinner_tasks, resources.getStringArray(R.array.all_plans))
        adapterPlan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinnerPlans = vfrag!!.findViewById<Spinner>(R.id.spinner_plans)
        spinnerPlans.adapter = adapterPlan
        spinnerPlans.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }
            override fun onItemSelected(parent: AdapterView<*>?, view2: View?, position: Int, id: Long) {
                val plan = spinnerPlans.selectedItem.toString()
                Log.i("SelectedSpinnerPlan", "$plan - id: ${mapPlans.getValue(plan)}")
                doAsync {
                    val tasks = taskDao.getAllByPlanId(mapPlans.getValue(plan))
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

        vfrag!!.findViewById<ImageView>(R.id.btn_deleteAllTasks).apply {
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
        TaskDialogMaker(context!!, planSelected, listOf(planSelected),
                "", null, "1", null, "CREATE",
                cancelable = { alertDialog: AlertDialog, _ ->
                    alertDialog.dismiss()
                },
                okable = { alertDialog: AlertDialog, view: View ->
                    createTask(alertDialog, view, { message: String ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    })
                }
        )
    }

    private fun loadEditTaskEvent() {
        val task: Task = taskAdapter!!.selectedItems.first()
        val planSelected = vfrag!!.findViewById<Spinner>(R.id.spinner_plans).selectedItem.toString()
        TaskDialogMaker(context!!, planSelected, listOf(planSelected),
                task.name!!, task.frecType, task.frecValue!!, task.responsibleId,
                "UPDATE",
                cancelable = { alertDialog: AlertDialog, _ ->
                    alertDialog.dismiss()
                },
                okable = { alertDialog: AlertDialog, view: View ->
                    updateTask(task.id, alertDialog, view, { message: String ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    })
                }
        )
    }

    private fun loadDeleteTasksEvent() {
        val selected = taskAdapter!!.selectedItems
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Alerta")
        builder.setMessage("¿Está seguro que desea eliminar ${
            if (selected.size > 1) "las ${selected.size} tareas" else "1 tarea"
        } del plan?")
        builder.setPositiveButton("Si"){ dialog, which ->
            doAsync {
                selected.forEach { taskDao.deleteAll(it) }
                uiThread {
                    taskAdapter?.deleteTasks()
                    taskAdapter?.notifyDataSetChanged()
                    switchFlipper(0)
                    Toast.makeText(context, if (selected.size > 1) "Tareas Eliminadas" else "Tarea Eliminada",Toast.LENGTH_SHORT).show()
                }
            }
        }
        builder.setNegativeButton("No"){ _, _ -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun createTask(alertDialog: AlertDialog, viewDialog: View, fnError: (String) -> Unit) {

        val planName: String = viewDialog.spinner_dialog_plans.selectedItem as String
        val taskName: String = viewDialog.et_dialog_nameTask.text.toString()
        val frecType: String = viewDialog.spinner_dialog_interval.selectedItem as String
        val frecValue: String? = viewDialog.et_dialog_interval.text.toString()

        var msg: String? = null

        if (planName == "")
            msg = "Falta completar el Plan"
        if (taskName == "")
            msg = "Falta completar el nombre de la tarea"
        if (frecType == "")
            msg = "Falta completar el periodo de la tarea"
        if (frecValue == null || frecValue == "")
            msg = "Falta completar las repeticiones de la tarea"

        if (msg != null) {
            fnError(msg)
        } else {
            val planId = mapPlans.getValue(planName)
            val newTask = Task(
                    id = UUID.randomUUID().toString(),
                    name = taskName,
                    frecType = frecType,
                    frecValue = frecValue!!,
                    completed = false,
                    planId = planId
            )
            doAsync {
                val userId = planDao.findById(planId).userId
                newTask.responsibleId = userId
                newTask.supervisorId = userId
                taskDao.insertAll(newTask)
                val tasks = taskDao.getAllByPlanId(planId)
                uiThread {
                    taskAdapter!!.setDataset(tasks)
                    taskAdapter!!.notifyDataSetChanged()
                    alertDialog.dismiss()
                }
            }
        }
    }

    private fun updateTask(taskId: String, alertDialog: AlertDialog, viewDialog: View, fnError: (String) -> Unit) {
        val planName: String = viewDialog.spinner_dialog_plans.selectedItem as String
        val taskName: String = viewDialog.et_dialog_nameTask.text.toString()
        val frecType: String = viewDialog.spinner_dialog_interval.selectedItem as String
        val frecValue: String? = viewDialog.et_dialog_interval.text.toString()

        var msg: String? = null

        if (planName == "")
            msg = "Falta completar el Plan"
        if (taskName == "")
            msg = "Falta completar el nombre de la tarea"
        if (frecType == "")
            msg = "Falta completar el periodo de la tarea"
        if (frecValue == null || frecValue == "")
            msg = "Falta completar las repeticiones de la tarea"

        if (msg != null) {
            fnError(msg)
        } else {
            doAsync {
                taskDao.updateTask(taskId, taskName, frecType, frecValue!!)
                val tasks = taskDao.getAllByPlanId(mapPlans.getValue(planName))
                uiThread {
                    taskAdapter!!.setDataset(tasks)
                    taskAdapter!!.notifyDataSetChanged()
                    deselectTasks()
                    alertDialog.dismiss()
                }
            }
        }
    }

    private fun loadToDb(response: List<com.botigocontigo.alfred.tasks.Plan>) {

        planDao.deleteAllRows()
        taskDao.deleteAllRows()

        response.forEach { p ->
            planDao.insertAll(Plan(
                    id = p.id,
                    name = p.name,
                    businessArea = p.businessArea,
                    userId = p.userId,
                    userEmail = p.userEmail,
                    createdDate = p.createAt
            ))

            p.tasks.forEach {
                taskDao.insertAll(Task(
                        id = it.id,
                        name = it.name,
                        responsibleId = it.responsibleId,
                        supervisorId = it.supervisorId,
                        frecType = it.frequency!!.type,
                        frecValue = it.frequency!!.value,
                        completed = it.completed,
                        planId = p.id
                ))
            }
        }
    }

    private fun savePlans(plans: List<Plan>) {


//        api.plansSaveAll()
    }
}