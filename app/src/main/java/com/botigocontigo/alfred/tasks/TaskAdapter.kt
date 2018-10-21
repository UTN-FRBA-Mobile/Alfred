package com.botigocontigo.alfred.tasks

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.botigocontigo.alfred.R


class TaskAdapter(private val myDataset: ArrayList<Task>) : RecyclerView.Adapter<TaskAdapter.ViewHolderTasks>() {

    private var selectedItems: Int = 0

    class ViewHolderTasks(itemView: View) : ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.task_name)
        val interval: TextView = itemView.findViewById(R.id.task_interval)
        val assigned: TextView = itemView.findViewById(R.id.task_assigned)

        
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTasks {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_view, parent, false)
        return ViewHolderTasks(view)
    }

    override fun onBindViewHolder(holder: ViewHolderTasks, position: Int) {
        holder.name.text = myDataset[position].name
        holder.interval.text = myDataset[position].timeValue.toString()
        holder.assigned.text = myDataset[position].responsable
    }

    override fun getItemCount(): Int = myDataset.size

    private fun showMenuBelow() {
        if (selectedItems == 1) {

        }
    }

}