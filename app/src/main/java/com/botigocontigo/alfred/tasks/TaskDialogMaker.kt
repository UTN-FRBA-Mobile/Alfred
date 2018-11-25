package com.botigocontigo.alfred.tasks

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.botigocontigo.alfred.R
import kotlinx.android.synthetic.main.dialog_form_task.view.*
import android.support.v4.content.ContextCompat.getSystemService



class TaskDialogMaker (
        private val ctx: Context,
        private val initPlan: String,
        private val arrayPlans: List<String>,
        private val initNameTask: String,
        private val initFrecType: String?,
        private val initFrecValue: String,
        val initResponsible: String?,
        private val operation: String,
        val cancelable: (AlertDialog, View) -> Unit,
        val okable: (AlertDialog, View) -> Unit
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
            val arrayAdapterPlan = ArrayAdapter(ctx, R.layout.spinner_tasks, arrayPlans)
            adapter = arrayAdapterPlan.apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            setSelection(arrayAdapterPlan.getPosition(initPlan))
        }

        spinnerIntervals.apply {
            val arrayAdapter = ArrayAdapter(ctx, R.layout.spinner_tasks,
                    resources.getStringArray(R.array.task_unit_interval))
            adapter = arrayAdapter.apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            setSelection(arrayAdapter.getPosition(initFrecType))

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view2: View?, position: Int, id: Long) {
                    dialog.tv_dialog_interval.text = getPeriod(getItemAtPosition(position).toString())
                }
            }
        }

        etNameTask.setText(initNameTask)
        etIntervalCount.setText(initFrecValue.toString())

        alertDialog = AlertDialog.Builder(ctx).setView(dialog).show()

        alertDialog.window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        alertDialog.setTitle(when(operation){
            "CREAR" -> "Nueva Tarea"
            "EDITAR" -> "Modificar Tarea"
            else -> "Nein"
        })

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

        hideKeypad(etNameTask)
        hideKeypad(etIntervalCount)

    }

    private fun hideKeypad(et: EditText) {
        val imm = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.hideSoftInputFromWindow(et.windowToken, 0)
    }

    private fun getPeriod(frecuency: String) : String {
        return when (frecuency){
            "Diariamente" -> "Dias"
            "Semanalmente" -> "Semanas"
            "Mensualmente" -> "Meses"
            "Anualmente" -> "Años"
            else -> "desconocido"
        }
    }
}