package com.botigocontigo.alfred.tasks

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.botigocontigo.alfred.R
import com.botigocontigo.alfred.storage.db.entities.Task


class TaskAdapter(private val switchFlipper: (Int) -> Unit) :
        RecyclerView.Adapter<TaskAdapter.ViewHolderTasks>() {

    private var dataset: List<Task> = listOf()

    var selectedItems: MutableList<Task> = arrayListOf()
    private var recycler: RecyclerView? = null

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
        return ViewHolderTasks(view).listen { position, _ ->
            val item = dataset[position]
            // Verifico si el elemento ya se selecciono anteriormente
            // Si existe, lo quito de la lista y su color de fondo vuelve a la normalidad
            if (selectedItems.count { it.id == item.id } == 1) {
                selectedItems.remove(item)
                view.setBackgroundColor(ContextCompat.getColor(parent.context, R.color.colorBackgroundTask))
            } else {
                selectedItems.add(item)
                view.setBackgroundColor(ContextCompat.getColor(parent.context, R.color.selectedTask))
            }
            switchFlipper(when (selectedItems.size) {
                0 -> 0
                1 -> 1
                else -> 2
            })
        }
    }

    override fun onBindViewHolder(holder: ViewHolderTasks, position: Int) {
        val t = dataset[position]
        holder.name.text = t.name
        holder.interval.text = "Durante ${t.frecValue} ${getPeriod(t.frecType)}"
        holder.assigned.text = t.responsibleId
    }

    override fun getItemCount(): Int = dataset.size

    // Funcion que sirve deseleccionar las tareas marcadas
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


    // Eliminar una tarea
    fun deleteTasks() {
        for (selec in selectedItems) {
            for ((index, item) in dataset.withIndex()) {
                if (selec.id == item.id) {
                    val itemView = recycler?.layoutManager?.findViewByPosition(index)
                    itemView?.setBackgroundColor(ContextCompat
                            .getColor(recycler!!.context, R.color.colorBackgroundTask))
                    dataset = dataset.filter { it.id != item.id }
                    break
                }
            }
        }
        selectedItems = arrayListOf()
    }

    fun setDataset(ds: List<Task>): TaskAdapter {
        dataset = ds
        return this
    }

    private fun getPeriod(frecuency: String?) : String {
        return when (frecuency){
            "Diariamente" -> "Dias"
            "Semanalmente" -> "Semanas"
            "Mensualmente" -> "Meses"
            "Anualmente" -> "Años"
            else -> "desconocido"
        }
    }
}