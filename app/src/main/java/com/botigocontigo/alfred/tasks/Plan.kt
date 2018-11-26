package com.botigocontigo.alfred.tasks

import java.util.Date

data class Plan (
        val id: String,
        val name: String,
        val businessArea: String,
        val userId: String,
        val userEmail: String,
        val createAt: Date,
        val tasks: List<Task> = listOf()
)