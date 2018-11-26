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
import com.botigocontigo.alfred.tasks.Frequency


class TasksFragment : Fragment() {

    private lateinit var api: BotigocontigoApi
    private lateinit var services: Services
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

        services = Services(inflater.context)
        api = services.botigocontigoApi()

        api.plansGetAll().call(PlansGetCallbacks(::loadToDb))

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
                doAsync {
                    val tasks = taskDao.getAllByPlanId(mapPlans.getValue(plan))
                    uiThread {
                        recycler!!.adapter = taskAdapter!!.setDataset(tasks)
                        val emptyView = vfrag!!.findViewById<TextView>(R.id.tv_empty_recycler)
                        if (tasks.isEmpty()) {
                            recycler!!.visibility = View.GONE
                            emptyView.visibility = View.VISIBLE
                        }
                        else {
                            recycler!!.visibility = View.VISIBLE
                            emptyView.visibility = View.GONE
                        }
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

    override fun onDestroyView() {
        Log.i("Tareas","Saliendo..")
        savePlans()
        super.onDestroyView()
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
                    val emptyView = vfrag!!.findViewById<TextView>(R.id.tv_empty_recycler)
                    recycler!!.visibility = View.VISIBLE
                    emptyView.visibility = View.GONE
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
        val planId = selected.first().planId
        var cantidad: Int?
        builder.setPositiveButton("Si"){ dialog, which ->
            doAsync {
                selected.forEach { taskDao.deleteAll(it) }
                cantidad = taskDao.getCountTasksByPlan(planId)
                uiThread {
                    taskAdapter?.deleteTasks()
                    taskAdapter?.notifyDataSetChanged()
                    switchFlipper(0)
                    val emptyView = vfrag!!.findViewById<TextView>(R.id.tv_empty_recycler)
                    if (cantidad == 0) {
                        recycler!!.visibility = View.GONE
                        emptyView.visibility = View.VISIBLE
                    } else {
                        recycler!!.visibility= View.VISIBLE
                        emptyView.visibility= View.GONE
                    }
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
                    responsibleId = services.currentUser().userId,
                    supervisorId = services.currentUser().userId,
                    completed = false,
                    planId = planId
            )
            doAsync {
                val userId = planDao.getUserIdByPlan(planId)
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

        doAsync {

            val userId = services.currentUser().userId
            val email = services.currentUser().email

            if (response.isEmpty()) {
                planDao.insertAll(
                        Plan("aaaaa", "Plan Comercial", "Comercio", userId, email, Date()),
                        Plan("bbbbb", "Plan de Comunicación", "Recursos Humanos", userId, email, Date()),
                        Plan("ccccc", "Plan de Administración", "Administración", userId, email, Date())
                )
            } else {
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
                                responsibleId = email,
                                supervisorId = it.supervisorId,
                                frecType = it.frequency!!.type,
                                frecValue = it.frequency!!.value,
                                completed = it.completed,
                                planId = p.id
                        ))
                    }
                }
                Log.i("All Plan DB", planDao.getAll().toString())
            }

            mapPlans = planDao.getAll().map { it.name to it.id }.toMap()

            uiThread { setUpUIElements() }
        }
    }

    private fun savePlans() {

        var plansApi: List<com.botigocontigo.alfred.tasks.Plan> = listOf()

        doAsync {
            val plansDB = planDao.getAll()

            plansApi = plansDB.map { p ->
                com.botigocontigo.alfred.tasks.Plan(
                        id = p.id,
                        name = p.name,
                        userId = p.userId!!,
                        userEmail = p.userEmail!!,
                        businessArea = p.businessArea!!,
                        createAt = p.createdDate!!,
                        tasks = taskDao.getAllByPlanId(p.id).map {
                            com.botigocontigo.alfred.tasks.Task(
                                    id = it.id,
                                    name = it.name!!,
                                    frequency = Frequency(it.frecType!!, it.frecValue!!),
                                    responsibleId = p.userEmail,
                                    supervisorId = it.supervisorId,
                                    status = "Pendiente",
                                    completed = it.completed!!
                            )
                        }
                )
            }
            Log.i("API POST Plan", plansApi.size.toString())
            api.plansSaveAll(plansApi.toTypedArray())
        }
    }
}