package com.botigocontigo.alfred

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
//import android.os.Bundleimport android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "ChatActivity"

class ChatActivity : AppCompatActivity() {
    private lateinit var adapter: MessageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        messageList.layoutManager = LinearLayoutManager(this)
        adapter = MessageAdapter(this)
        messageList.adapter = adapter

        btnSend1.setOnClickListener {
            val message = Message(
                    App.user,
                    btnSend1.text.toString()
            )

            val call = ChatService.create().postMessage(message)

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    resetInput()
                    if (!response.isSuccessful) {
                        Log.e(TAG, response.code().toString());
                        Toast.makeText(applicationContext,"Response was not successful", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    resetInput()
                    Log.e(TAG, t.toString());
                    Toast.makeText(applicationContext,"Error when calling the service", Toast.LENGTH_SHORT).show()
                }
            })
        }
        setupPusher()
    }

    private fun setupPusher() {
       /* val options = PusherOptions()
        options.setCluster("PUSHER_APP_CLUSTER")

        val pusher = Pusher("PUSHER_APP_KEY", options)
        val channel = pusher.subscribe("chat")

        channel.bind("new_message") { channelName, eventName, data ->
            val jsonObject = JSONObject(data)

            val message = Message(
                    jsonObject["user"].toString(),
                    jsonObject["message"].toString(),
                    jsonObject["time"].toString().toLong()
            )

            runOnUiThread {
                adapter.addMessage(message)
                // scroll the RecyclerView to the last added element
                messageList.scrollToPosition(adapter.itemCount - 1);
            }

        }

        pusher.connect()*/
    }

    private fun resetInput() {
        // Clean text box
        txtMessage.text.clear()

        // Hide keyboard
        /*val inputManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
                currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
        */
    }

}
