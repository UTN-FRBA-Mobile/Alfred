package com.botigocontigo.alfred

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.botigocontigo.alfred.learn.ArticleAdapter
import com.botigocontigo.alfred.learn.repositories.google.GoogleArticleRepository
import com.botigocontigo.alfred.learn.repositories.intelligent.IntelligentArticleRepository

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

        // val articleRepository = GoogleArticleRepository(this)
        val articleRepository = IntelligentArticleRepository(this)
        articleRepository.search("consejos para emprender", adapter)
    }

}
