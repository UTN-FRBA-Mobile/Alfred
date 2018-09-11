package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


import ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2.TweetsFragment.OnListFragmentInteractionListener
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.item_footer.view.*
import kotlinx.android.synthetic.main.item_header.view.*
import kotlinx.android.synthetic.main.item_image.view.*
import kotlinx.android.synthetic.main.item_profile.view.*

class TweetsAdapter(
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {
    var listTweets: Array<Tweet> = arrayOf()

    override fun getItemViewType(position: Int): Int {
        return if (listTweets[position].image == null) R.layout.item_simple else R.layout.item_image
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item =listTweets[position]
        holder.nameText.text = "User ${position}"
        holder.certifiedIcon.visibility = if (position % 3 == 0) View.VISIBLE else View.GONE
        holder.usernameText.text = "@username${position}"
        holder.tweetContent.text = item.content
        holder.commentCount.text = "${position}"
        holder.retweetCount.text = "${position * 2}"
        holder.likeCount.text = "${position * 3}"
        Picasso.get().load(item.profilePic).into(holder.profilePic)
        if(item.image != null){
            Picasso.get().load(item.image).into(holder.image)
        }
    }

    override fun getItemCount(): Int = listTweets.size

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val profilePic: ImageView = mView.profilePic
        val nameText: TextView = mView.nameText
        val certifiedIcon: View = mView.certifiedIcon
        val usernameText: TextView = mView.usernameText
        val tweetContent: TextView = mView.tweetContent
        val image: ImageView? = mView.image
        val commentCount: TextView = mView.commentCount
        val retweetCount: TextView = mView.retweetCount
        val likeCount: TextView = mView.likeCount
    }
}
