package com.botigocontigo.alfred.learn

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.botigocontigo.alfred.R
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler
import java.lang.RuntimeException

class ArticleAdapter (val context: Context, private val articles: ArrayList<Article>) : RecyclerView.Adapter<ArticleViewHolder>() {

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(LayoutInflater.from(context).inflate(R.layout.article, parent, false))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder?.bind(article)
    }

}
