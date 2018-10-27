package com.botigocontigo.alfred.tasks

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ViewFlipper
import com.botigocontigo.alfred.R


class TaskAdapter(private val dataset: ArrayList<Task>, private val fg: View) : RecyclerView.Adapter<TaskAdapter.ViewHolderTasks>() {

    var selectedItems: MutableList<Task> = arrayListOf()

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_view, parent, false)
        return ViewHolderTasks(view).listen { position, type ->
//            val item = myDataset.get(position)
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
        holder.name.text = dataset[position].name
        holder.interval.text = dataset[position].timeValue.toString()
        holder.assigned.text = dataset[position].responsable
    }

    override fun getItemCount(): Int = dataset.size

}