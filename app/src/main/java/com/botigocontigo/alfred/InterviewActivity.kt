package com.botigocontigo.alfred

import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import android.widget.ArrayAdapter
import android.R.array
import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.R.layout.activity_main
import android.os.Bundle

// TODO Following this Tutorial: https://developer.android.com/training/implementing-navigation/nav-drawer?hl=es-419
class InterviewActivity: AppCompatActivity() {
    private var mPlanetTitles: Array<String>? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var mDrawerList: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interview)

        mPlanetTitles = getResources().getStringArray(R.array.planets_array)
        mDrawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        mDrawerList = findViewById(R.id.left_drawer) as ListView

        // Set the adapter for the list view
        mDrawerList.adapter = ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles)
        // Set the list's click listener
        // mDrawerList.onItemClickListener = DrawerItemClickListener()
    }
}