package com.botigocontigo.alfred

import android.widget.ArrayAdapter
import android.support.v4.widget.DrawerLayout
import android.os.Bundle
import android.app.Activity
import android.content.res.Configuration
import android.net.Uri
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.AdapterView
import com.botigocontigo.alfred.foda.FodaFragment
import com.botigocontigo.alfred.tasks.TasksFragment

class MenuActivity : AppCompatActivity(), TasksFragment.OnFragmentInteractionListener,FodaFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {

    }

    private var mMenuItemsTitles: Array<String>? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var mDrawerList: ListView? = null
    private var mDrawerToggle: ActionBarDrawerToggle? = null
    private var mDrawerTitle: CharSequence? = null
    private var mTitle: CharSequence? = null



    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interview) //FIXME Change to activity_main?? So we only have one activity and change the fragments

        mMenuItemsTitles = resources.getStringArray(R.array.menu_items_array)
        mDrawerList = findViewById<View>(R.id.left_drawer) as ListView
        mDrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout

        // Set the adapter for the list view
        mDrawerList!!.adapter = ArrayAdapter(this,
                R.layout.drawer_list_item, mMenuItemsTitles!!)
        // Set the list's click listener
        mDrawerList!!.onItemClickListener = DrawerItemClickListener()

        mDrawerTitle = title
        mTitle = mDrawerTitle
        mDrawerLayout = findViewById<View>(R.id.drawer_layout) as? DrawerLayout
        mDrawerToggle = object : ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state.  */
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
                //FIXME todo uncomment or replace to use ToolBar
                //actionBar!!.title = mTitle
                invalidateOptionsMenu() // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state.  */
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                //actionBar!!.title = mDrawerTitle
                invalidateOptionsMenu() // creates call to onPrepareOptionsMenu()
            }
        }

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout!!.setDrawerListener(mDrawerToggle)
        //Use only if the upper statement fails because the deprecated method
        //mDrawerLayout!!.addDrawerListener(mDrawerToggle as ActionBarDrawerToggle)
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeButtonEnabled(true);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        // If the nav drawer is open, hide action items related to the content view
        val drawerOpen = mDrawerLayout!!.isDrawerOpen(mDrawerList!!)
        //Example how to hide a item called "action_websearch"
        //menu.findItem(R.id.action_websearch).isVisible = !drawerOpen
        return super.onPrepareOptionsMenu(menu)
    }

    private inner class DrawerItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            selectItem(position)
        }
    }

    override fun setTitle(title: CharSequence) {
        mTitle = title
        //actionBar!!.title = mTitle
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle!!.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle!!.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        return if (mDrawerToggle!!.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
        // Handle your other action bar items...
    }

    /** Swaps fragments in the main content view  */
    private fun selectItem(position: Int) {
        // Create a new fragment and specify the fragment to show based on position
        val <Fragment> fragment = AppFragments.FRAGMENTS[position]
        val args = Bundle()
        args.putInt("fragment_number", position)
        fragment.arguments = args

        // Insert the fragment by replacing any existing fragment
        supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null) // no se que va en el parametro
                .commit()

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList!!.setItemChecked(position, true)
        setTitle(mMenuItemsTitles!![position])
        mDrawerLayout!!.closeDrawer(mDrawerList!!)

    }
}