package com.botigocontigo.alfred.tasks

data class Task(
        var name: String,
        var responsibleId: String?,
        var supervisorId: String?,
        var frequency: Frequency?,
        var completed: Boolean = false
)