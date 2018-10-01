package com.botigocontigo.alfred.tasks

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.botigocontigo.alfred.R


class TaskAdapter(private val myDataset: Array<Task>) : RecyclerView.Adapter<TaskAdapter.ViewHolderTasks>() {

    class ViewHolderTasks(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        holder.interval.text = myDataset[position].remember
        holder.assigned.text = myDataset[position].assigned
    }

    override fun getItemCount(): Int = myDataset.size

}