package com.botigocontigo.alfred.utils.executors

interface ExecutorFactory {

    fun <asyncOperationType> create(): Executor<asyncOperationType>

}