package com.botigocontigo.alfred.learn

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.botigocontigo.alfred.R
import com.botigocontigo.alfred.learn.repositories.ArticleRepositoryResultHandler
import java.lang.RuntimeException

class ArticleAdapter (val context: Context) : RecyclerView.Adapter<ArticleViewHolder>(), ArticleRepositoryResultHandler {

    private val articles: ArrayList<Article> = ArrayList()

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(LayoutInflater.from(context).inflate(R.layout.article, parent, false))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles.get(position)
        holder?.bind(article)
    }

    // ArticleRepositoryResultHandler methods

    override fun handleArticle(article: Article) {
        articles.add(article)
        notifyDataSetChanged()
    }

    override fun error(query: String) {
        throw RuntimeException("Something went wrong fetching articles of query: $query")
    }

}
