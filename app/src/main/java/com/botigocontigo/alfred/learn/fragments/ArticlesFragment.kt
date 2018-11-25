package com.botigocontigo.alfred.learn.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.botigocontigo.alfred.R
import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.ArticleAdapter
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler
import com.botigocontigo.alfred.learn.repositories.actions.ArticleRepositoryAction
import kotlinx.android.synthetic.main.content_learn.view.*

class ArticlesFragment : Fragment(), ArticlesHandler {
    private var adapter: ArticleAdapter? = null
    private val articles = ArrayList<Article>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewFragment = inflater.inflate(R.layout.fragment_articles, container, false)
        val context = inflater.context
        val articleList = viewFragment.articleList!!
        articleList.layoutManager = LinearLayoutManager(context)
        adapter = ArticleAdapter(context, articles)
        articleList.adapter = adapter
        return viewFragment
    }

    override fun searchSuccessful() {
        articles.clear()
        adapter?.notifyDataSetChanged()
    }

    override fun handleArticle(article: Article) {
        articles.add(article)
        adapter?.notifyDataSetChanged()
    }

    override fun error(action: ArticleRepositoryAction) {
        articles.clear()
        adapter?.notifyDataSetChanged()
    }

}