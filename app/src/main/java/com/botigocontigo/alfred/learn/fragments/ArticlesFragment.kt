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
import kotlinx.android.synthetic.main.content_learn.view.*

class ArticlesFragment : Fragment(), ArticlesHandler {
    private var adapter: ArticleAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewFragment = inflater.inflate(R.layout.fragment_articles, container, false)
        val context = inflater.context
        val articleList = viewFragment.articleList!!
        articleList.layoutManager = LinearLayoutManager(context)
        adapter = ArticleAdapter(context)
        articleList.adapter = adapter
        return viewFragment
    }

    override fun searchSuccessful() {
        adapter?.searchSuccessful()
    }

    override fun handleArticle(article: Article) {
        adapter!!.handleArticle(article)
    }

    override fun error(query: String) {
        adapter!!.error(query)
    }

}