package com.botigocontigo.alfred.suggestions

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.botigocontigo.alfred.R

class MessageAdapter (val context: Context) : RecyclerView.Adapter<MessageViewHolder>() {

    private val messages: ArrayList<SuggestionMessage> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_bubble_received, parent, false))
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages.get(position)
        holder?.bind(message)
    }

}