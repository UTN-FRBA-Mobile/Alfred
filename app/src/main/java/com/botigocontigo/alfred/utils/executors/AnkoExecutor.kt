package com.botigocontigo.alfred.utils.executors

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AnkoExecutor<asyncOperationType> : SyncExecutor<asyncOperationType>() {

    override fun run() {
        doAsync {
            val result = async!!()
            uiThread {
                if(sync != null) sync!!(result)
            }
        }
    }

}