package com.botigocontigo.alfred.learn

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.ImageView
import kotlinx.android.synthetic.main.article.view.*
import com.botigocontigo.alfred.R


class ArticleViewHolder  (view: View) : RecyclerView.ViewHolder(view) {
    private var titleText: TextView = view.txtTitle
    private var bodyText: TextView = view.txtBody
    private var previewImage: ImageView = view.imgPreview

    open fun bind(article: Article) {
        titleText.text = article.getTitle()
        bodyText.text = article.getBody()
        previewImage.setImageResource(article.getImageResourceId())
    }
}
