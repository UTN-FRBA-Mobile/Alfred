package com.botigocontigo.alfred.tasks

data class Task(
        val id: Int,
        var name: String,
        var timeValue: Int,
        var timeUnit: String,
        var reminderValue: Int,
        var reminderUnit: String,
        var responsable: String?
)

data class Plan(
        val id: Int,
        var name: String,
        var tasks: ArrayList<Task>
)

