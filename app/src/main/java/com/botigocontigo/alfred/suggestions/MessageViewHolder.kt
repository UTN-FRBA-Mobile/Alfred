package com.botigocontigo.alfred.suggestions

import android.support.v7.widget.RecyclerView

import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.chat_bubble_received.view.*

class MessageViewHolder  (view: View) : RecyclerView.ViewHolder(view){

    val message: TextView = view.txtOtherMessage

    open fun bind (message: SuggestionMessage){
        message.text = message.text
    }
}