package com.botigocontigo.alfred.utils.executors

class AnkoExecutorFactory : ExecutorFactory {

    override fun <asyncOperationType> create(): Executor<asyncOperationType> {
        return AnkoExecutor<asyncOperationType>()
    }

}