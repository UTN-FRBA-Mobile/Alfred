package com.botigocontigo.alfred.learn

import android.content.Context
import android.graphics.drawable.Drawable
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

    fun bind(article: Article) {
        titleText.text = article.title
        bodyText.text = article.body
        val imageUrl = article.imageUrl
        Picasso.get().load(imageUrl).into(previewImage)
        fetchFavoriteStatus(article)
        view.setOnClickListener { changeFavoriteStatus(article) }
    }

    private fun changeFavoriteStatus(article: Article) {
        val repository = Services(context).roomArticleRepository()
        if(article.favorite)
            repository.deleteArticle(article)
        else
            repository.saveArticle(article)
        fetchFavoriteStatus(article)
    }

    private fun fetchFavoriteStatus(article: Article) {
        val isFavorite = Services(context).roomArticleRepository().isPresent(article)
        article.favorite = isFavorite
        if(article.favorite) {
            favoriteImage.setImageResource(R.mipmap.star)
        } else {
            favoriteImage.setImageResource(R.mipmap.star_translucent)
        }

    }

}
