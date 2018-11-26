package com.botigocontigo.alfred.tasks

import com.google.gson.annotations.SerializedName

data class Task(
        var id: String,
        var name: String,
        var responsibleId: String?,
        var supervisorId: String?,
        var frequency: Frequency?,
        var status: String?,
        var completed: Boolean = false
)