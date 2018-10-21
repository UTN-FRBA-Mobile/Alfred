package com.botigocontigo.alfred

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.botigocontigo.alfred.tasks.*

class TaskActivity : AppCompatActivity(),
        TasksFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        supportFragmentManager
                .beginTransaction()
                .add(R.id.frg_tasks, TasksFragment.newInstance("a","b"), "Lalala")
                .commit()
    }
}
