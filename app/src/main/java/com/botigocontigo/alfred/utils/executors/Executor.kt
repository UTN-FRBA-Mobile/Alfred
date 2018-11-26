package com.botigocontigo.alfred.utils.executors

interface Executor<asyncOperationType> {

    fun sync(function: (asyncOperationType) -> Unit): Executor<asyncOperationType>
    fun async(function: () -> asyncOperationType): Executor<asyncOperationType>
    fun run()

}