package com.botigocontigo.alfred.utils.executors

open class SyncExecutor<asyncOperationType> : Executor<asyncOperationType> {
    protected var async: (() -> asyncOperationType)? = null
    protected var sync: ((asyncOperationType) -> Unit)? = null

    override fun sync(function: (asyncOperationType) -> Unit): Executor<asyncOperationType> {
        sync = function
        return this
    }

    override fun async(function: () -> asyncOperationType): Executor<asyncOperationType> {
        async = function
        return this
    }

    override fun run() {
        val result = async!!()
        if(sync != null) sync!!(result)
    }

}