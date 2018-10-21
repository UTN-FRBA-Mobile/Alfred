package com.botigocontigo.alfred

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

// TODO Following this tutorial https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui (now as a reference)
// TODO Following tutorial https://www.androidtutorialpoint.com/basics/android-chat-bubble-layout-9-patch-image-using-listview/
//class MessageListActivity : AppCompatActivity() {

class InterviewFragment : Fragment() {
    private var mMessageRecycler: RecyclerView? = null
    private var mMessageAdapter: MessageListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)

        mMessageRecycler = findViewById<View>(R.id.reyclerview_message_list) as RecyclerView
        mMessageAdapter = MessageListAdapter(this, messageList)
        mMessageRecycler!!.setLayoutManager(LinearLayoutManager(this))
    }
}