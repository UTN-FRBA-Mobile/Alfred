package com.botigocontigo.alfred.utils.executors

class SyncExecutorFactory : ExecutorFactory {

    override fun <asyncOperationType> create(): Executor<asyncOperationType> {
        return SyncExecutor<asyncOperationType>()
    }

}