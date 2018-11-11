package com.botigocontigo.alfred.tasks

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ViewFlipper
import com.botigocontigo.alfred.R


class TaskAdapter(private var dataset: ArrayList<Task>, private val fg: View) : RecyclerView.Adapter<TaskAdapter.ViewHolderTasks>() {

    var selectedItems: MutableList<Task> = arrayListOf()
    var recycler: RecyclerView? = null

    class ViewHolderTasks(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.task_name)
        val interval: TextView = itemView.findViewById(R.id.task_interval)
        val assigned: TextView = itemView.findViewById(R.id.task_assigned)

        fun listen(event: (position: Int, type: Int) -> Unit): ViewHolderTasks {
            itemView.setOnClickListener {
                event.invoke(getAdapterPosition(), getItemViewType())
            }
            return this
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTasks {
        recycler = parent as RecyclerView
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_view, parent, false)
        return ViewHolderTasks(view).listen { position, type ->
            val item = dataset[position]
            val vf: ViewFlipper = fg.findViewById(R.id.vf_task_options)
            // Verifico si el elemento ya se selecciono anteriormente
            // Si existe, lo quito de la lista y su color de fondo vuelve a la normalidad
            if (selectedItems.filter { task -> task.id == item.id }.size == 1) {
                selectedItems.remove(item)
                view.setBackgroundColor(ContextCompat.getColor(parent.context, R.color.colorBackgroundTask))
            } else {
                selectedItems.add(item)
                view.setBackgroundColor(ContextCompat.getColor(parent.context, R.color.selectedTask))
            }

            vf.setFlipInterval(0)
            vf.inAnimation = null
            vf.outAnimation = null
            vf.displayedChild =
                    when (selectedItems.size) {
                        0 -> 0
                        1 -> 1
                        else -> 2
                    }
        }
    }

    override fun onBindViewHolder(holder: ViewHolderTasks, position: Int) {
        val repeatValue = dataset[position].timeValue
        val repeatUnit = dataset[position].timeUnit
        holder.name.text = dataset[position].name
        holder.interval.text = "Durante $repeatValue $repeatUnit"
        holder.assigned.text = dataset[position].responsible
    }

    override fun getItemCount(): Int = dataset.size

    /**
     * Funcion que sirve deseleccionar las tareas marcadas
     */
    fun deselectTask() {
        val iterator = selectedItems.iterator()
        while (iterator.hasNext()){
            val item = iterator.next()
            for ((index, data) in dataset.withIndex()){
                if (item.id == data.id) {
                    val itemView = recycler?.layoutManager?.findViewByPosition(index)
                    itemView?.setBackgroundColor(ContextCompat
                            .getColor(recycler!!.context, R.color.colorBackgroundTask))
                    break
                }
            }
        }
        selectedItems = arrayListOf()
    }



    /**
     * Eliminar una tarea
     */
    fun deleteTasks() {
        for (selec in selectedItems) {
            for ((index, item) in dataset.withIndex()) {
                if (selec.id == item.id) {
                    val itemView = recycler?.layoutManager?.findViewByPosition(index)
                    itemView?.setBackgroundColor(ContextCompat
                            .getColor(recycler!!.context, R.color.colorBackgroundTask))
                    dataset.remove(item)
                    break
                }
            }
        }
        selectedItems = arrayListOf()
    }
}