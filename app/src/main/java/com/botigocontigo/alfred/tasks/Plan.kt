package com.botigocontigo.alfred.tasks

data class Plan (
        val id: String,
        val name: String,
        val businessArea: String,
        val userId: String,
        val userEmail: String,
        val createAt: String,
        val tasks: List<Task> = listOf()
)