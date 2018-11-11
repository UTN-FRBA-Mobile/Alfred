package com.botigocontigo.alfred.tasks

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.botigocontigo.alfred.R
import kotlinx.android.synthetic.main.dialog_form_task.view.*

class TaskDialogMaker (
        private val ctx: Context,
        private val initPlan: Int?,
        private val arrayPlans: List<String>,
        private val initNameTask: String?,
        private val initInterval: Int?,
        private val arrayIntervals: ArrayList<String>,
        private val initCount: Int?,
        val initResponsible: String?,
        val cancelable: (AlertDialog, View) -> Unit,
        val okable: (AlertDialog, View) -> Unit,
        private val operation: String
        ) {

    private var dialog : View
    private var spinnerPlans : Spinner
    private var spinnerIntervals : Spinner
    private var etNameTask : EditText
    private var etIntervalCount : EditText
    private var alertDialog : AlertDialog


    init {
        dialog = LayoutInflater.from(ctx).inflate(R.layout.dialog_form_task, null)
        spinnerPlans = dialog.findViewById(R.id.spinner_dialog_plans)
        spinnerIntervals = dialog.findViewById(R.id.spinner_dialog_interval)
        etNameTask = dialog.findViewById<EditText>(R.id.et_dialog_nameTask)
        etIntervalCount = dialog.findViewById<EditText>(R.id.et_dialog_interval)

        spinnerPlans.apply {
            adapter = ArrayAdapter(ctx, R.layout.spinner_tasks, arrayPlans).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            if (initPlan !== null) setSelection(initPlan)
        }

        spinnerIntervals.apply {
            adapter = ArrayAdapter(ctx, R.layout.spinner_tasks, arrayIntervals).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            if (initInterval !== null) setSelection(initInterval)

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view2: View?, position: Int, id: Long) {
                    val selected = parent?.getItemAtPosition(position).toString()
                    dialog.tv_dialog_interval.text = getPeriod(selected)
                }
            }
        }

        if (initNameTask !== null) etNameTask.setText(initNameTask)
        if (initCount !== null) etIntervalCount.setText(initCount.toString())

        alertDialog = AlertDialog.Builder(ctx).setView(dialog).show()

        dialog.btn_cancel.apply {
            text = "CANCELAR"
            setOnClickListener {
                cancelable(alertDialog, dialog)
            }
        }

        dialog.btn_ok.apply {
            text = operation
            setOnClickListener {
                okable(alertDialog, dialog)
            }
        }
    }

    private fun getPeriod(interval: String) : String {
        return when (interval){
            "Diariamente" -> "dias"
            "Semanalmente" -> "semanas"
            "Mensualmente" -> "meses"
            "Anualmente" -> "años"
            else -> "desconocido"
        }
    }

}