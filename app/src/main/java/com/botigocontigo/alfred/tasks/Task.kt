package com.botigocontigo.alfred.tasks

data class Task(
        var id: String,
        var name: String,
        var responsibleId: String?,
        var supervisorId: String?,
        var frequency: Frequency?,
        var status: String?,
        var completed: Boolean = false
)