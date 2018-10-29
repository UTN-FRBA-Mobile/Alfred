package com.botigocontigo.alfred.suggestions

import android.support.v7.widget.RecyclerView
import android.view.TextureView
import android.view.View

class MessageViewHolder  (view: View) : RecyclerView.ViewHolder(view){

    val message: TextureView = view.txtOtherMessage

    open fun bind (message: SuggestionMessage){
        message.text = message.text
    }
}