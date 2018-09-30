package com.botigocontigo.alfred

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.ArticleAdapter

import kotlinx.android.synthetic.main.activity_learn.*
import kotlinx.android.synthetic.main.content_learn.*



class LearnActivity : AppCompatActivity() {
    private lateinit var adapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)
        setSupportActionBar(toolbar)

        articleList.layoutManager = LinearLayoutManager(this)
        adapter = ArticleAdapter(this)
        articleList.adapter = adapter

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        loadArticles()
    }

    fun loadArticles() {
        var title: String
        var body: String
        var article: Article
        // article 1
        title = "titulo 1"
        body = "lorem ipsum 1"
        article = Article(title, body)
        adapter.addArticle(article)
        // article 2
        title = "titulo 2"
        body = "lorem ipsum 2"
        article = Article(title, body)
        adapter.addArticle(article)
        // article 3
        title = "titulo 3"
        body = "lorem ipsum 3"
        article = Article(title, body)
        adapter.addArticle(article)
    }

}
