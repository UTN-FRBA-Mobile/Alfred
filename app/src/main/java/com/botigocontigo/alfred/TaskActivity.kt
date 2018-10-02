package com.botigocontigo.alfred

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.botigocontigo.alfred.tasks.*
import kotlinx.android.synthetic.main.dialog_new_task.view.*

class TaskActivity : AppCompatActivity(), OnItemSelectedListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var spinner: Spinner? = null
    private var btnAddTask: ImageButton? = null

    private var tasks: Array<Task> = arrayOf(
            Task(1,"Regar las plantas", "Cada 1 dia", "Eric"),
            Task(2,"Lavar la ropa", "Cada 2 dias", "Kevin"),
            Task(3,"Cagar credito a la sube", "Cada 3 dias", "Eze"),
            Task(4,"Ir al gimnasio", "Cada 2 dias", "Martin"),
            Task(5,"Ir al Congreso", "Cada 1 semana", "Martin"),
            Task(6,"Ir a AFIP", "Cada 1 mes", "Martin")
    )

    private val plans: Array<String> = arrayOf("Plan de Administracion", "Plan de Emprendimiento", "Plan de Viaje", "Plan de Capacitacion")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        btnAddTask = findViewById(R.id.btnAddTask)
        btnAddTask!!.setOnClickListener {
            val mDialogView: View = LayoutInflater.from(this).inflate(R.layout.dialog_new_task, null)
            val mBuilder: AlertDialog.Builder = AlertDialog.Builder(this@TaskActivity)
                    .setTitle("Creacion de Tarea")
                    .setView(mDialogView)

            val mAlertDialog = mBuilder.show()

            val mSpinner: Spinner = mDialogView.findViewById(R.id.dialog_spinner_plans)
            val adapterPlansDialog: ArrayAdapter<String> = ArrayAdapter(this@TaskActivity,
                    android.R.layout.simple_spinner_item, plans)
            adapterPlansDialog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mSpinner.adapter = adapterPlansDialog

            val spRepeatTask: Spinner = mDialogView.findViewById(R.id.dl_spinner_repeat)
            val adapterRepeatDialog: ArrayAdapter<String> = ArrayAdapter(this@TaskActivity,
                    android.R.layout.simple_spinner_item, resources.getStringArray(R.array.repeat_task))
            adapterRepeatDialog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spRepeatTask.adapter = adapterRepeatDialog

            val spRememberTask: Spinner = mDialogView.findViewById(R.id.dl_spinner_remember)
            val adapterRememeberDialog: ArrayAdapter<String> = ArrayAdapter(this@TaskActivity,
                    android.R.layout.simple_spinner_item, resources.getStringArray(R.array.remember_task))
            adapterRememeberDialog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spRememberTask.adapter = adapterRememeberDialog

            mDialogView.btnCancelar.setOnClickListener {
                mAlertDialog.dismiss()
            }

            mDialogView.btnCrearTarea.setOnClickListener {
                mAlertDialog.dismiss()
            }

        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = TaskAdapter(tasks)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerTasks).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        spinner = this.findViewById(R.id.spinner_planes)

        val adapterSpinner: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, plans)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = adapterSpinner

    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {}

    override fun onNothingSelected(arg0: AdapterView<*>) {}

}
