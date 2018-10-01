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
        var imageResourceId: Int
        var article: Article
        body = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
        // article 1
        title = "Titulo 1"
        imageResourceId = R.mipmap.article_image_2
        article = Article(title, body, imageResourceId)
        adapter.addArticle(article)
        // article 2
        title = "Titulo 2"
        imageResourceId = R.mipmap.article_image_1
        article = Article(title, body, imageResourceId)
        adapter.addArticle(article)
        // article 3
        title = "Titulo 3"
        imageResourceId = R.mipmap.article_image_2
        article = Article(title, body, imageResourceId)
        adapter.addArticle(article)
    }

}
