package com.botigocontigo.alfred.learn

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.ImageView
import com.botigocontigo.alfred.R
import com.botigocontigo.alfred.Services
import kotlinx.android.synthetic.main.article.view.*
import com.squareup.picasso.Picasso

class ArticleViewHolder  (var view: View) : RecyclerView.ViewHolder(view) {
    private var titleText: TextView = view.txtTitle
    private var bodyText: TextView = view.txtBody
    private var previewImage: ImageView = view.imgPreview
    private var favoriteImage: ImageView = view.imgFavorite
    private var context: Context = view.context

    private var favorite = false

    fun bind(article: Article) {
        titleText.text = article.title
        bodyText.text = article.description
        val imageUrl = article.imageUrl
        Picasso.get().load(imageUrl).into(previewImage)
        view.setOnClickListener { changeFavoriteStatus(article) }
        fetchFavoriteStatus(article)
    }

    private fun changeFavoriteStatus(article: Article) {
        val repository = Services(context).roomArticleRepository()
        if (favorite) repository.deleteArticle(article)
        else repository.upsert(article)
        showFavoriteStatus()
    }

    private fun fetchFavoriteStatus(article: Article) {
        val repository = Services(context).roomArticleRepository()
        val isPresent = repository.isPresent(article)
        favorite = isPresent
        showFavoriteStatus()
    }

    private fun showFavoriteStatus() {
        if(favorite) favoriteImage.setImageResource(R.mipmap.star)
        else favoriteImage.setImageResource(R.mipmap.star_translucent)
    }

}
