package com.botigocontigo.alfred

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.text.format.DateUtils.formatDateTime
import jdk.nashorn.internal.runtime.ECMAErrors.getMessage
import android.view.LayoutInflater
import android.view.ViewGroup
import java.nio.file.Files.size




/*
class MessageListAdapter(private val mContext: Context, private val mMessageList: List<Message>) : RecyclerView.Adapter(){

}
*/


class MessageListAdapter(private val mContext: Context, private val mMessageList: List<Message>) : RecyclerView.Adapter() {

    val itemCount: Int
        get() = mMessageList.size

    // Determines the appropriate ViewType according to the sender of the message.
    fun getItemViewType(position: Int): Int {
        val message = mMessageList[position] as UserMessage

        return if (message.getSender().getUserId().equals(SendBird.getCurrentUser().getUserId())) {  //TODO change
            // If the current user is the sender of the message
            VIEW_TYPE_MESSAGE_SENT
        } else {
            // If some other user sent the message
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        val view: View

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_sent, parent, false)
            return SentMessageHolder(view)
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_received, parent, false)
            return ReceivedMessageHolder(view)
        }

        return null
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = mMessageList[position] as UserMessage

        when (holder.getItemViewType()) {
            VIEW_TYPE_MESSAGE_SENT -> (holder as SentMessageHolder).bind(message)
            VIEW_TYPE_MESSAGE_RECEIVED -> (holder as ReceivedMessageHolder).bind(message)
        }
    }

    // FIXME after deletes SentMessageHolder is similar to ReceivedMessageHolder. Can I use the same class?
    private inner class SentMessageHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var messageText: TextView
        internal var timeText: TextView //TODO delete

        init {

            messageText = itemView.findViewById<View>(R.id.text_message_body) as TextView
            timeText = itemView.findViewById<View>(R.id.text_message_time) as TextView //TODO delete
        }

        internal fun bind(message: UserMessage) {
            messageText.setText(message.getMessage())

            // Format the stored timestamp into a readable String using method.
            timeText.setText(Utils.formatDateTime(message.getCreatedAt())) //TODO delete
        }
    }

    private inner class ReceivedMessageHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var messageText: TextView
        internal var timeText: TextView //TODO delete
        internal var nameText: TextView //TODO delete
        internal var profileImage: ImageView  //TODO delete

        init {

            messageText = itemView.findViewById<View>(R.id.text_message_body) as TextView
            timeText = itemView.findViewById<View>(R.id.text_message_time) as TextView //TODO delete
            nameText = itemView.findViewById<View>(R.id.text_message_name) as TextView //TODO delete
            profileImage = itemView.findViewById<View>(R.id.image_message_profile) as ImageView //TODO delete
        }

        internal fun bind(message: UserMessage) {
            messageText.setText(message.getMessage())

            // Format the stored timestamp into a readable String using method.
            timeText.setText(Utils.formatDateTime(message.getCreatedAt())) //TODO delete

            nameText.setText(message.getSender().getNickname()) //TODO delete

            // Insert the profile image from the URL into the ImageView.
            Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage) //TODO delete
        }
    }

    companion object {
        private val VIEW_TYPE_MESSAGE_SENT = 1
        private val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }
}