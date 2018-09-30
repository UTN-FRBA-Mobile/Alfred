package com.botigocontigo.alfred.learn

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.article.view.*

class ArticleViewHolder  (view: View) : RecyclerView.ViewHolder(view) {
    private var titleText: TextView = view.txtTitle
    private var bodyText: TextView = view.txtBody
    open fun bind(article: Article) {
        titleText.text = article.getTitle()
        bodyText.text = article.getBody()
    }
}
