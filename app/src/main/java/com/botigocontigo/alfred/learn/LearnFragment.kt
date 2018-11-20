package com.botigocontigo.alfred.learn

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.botigocontigo.alfred.R
import com.botigocontigo.alfred.Services
import com.botigocontigo.alfred.google.Credentials
import com.botigocontigo.alfred.google.GoogleApi
import com.botigocontigo.alfred.google.GoogleSearchService
import com.botigocontigo.alfred.learn.repositories.ArticleRepository
import com.botigocontigo.alfred.learn.repositories.google.GoogleArticleRepository
import com.botigocontigo.alfred.learn.repositories.intelligent.IntelligentArticleRepository
import com.botigocontigo.alfred.learn.repositories.room.LearnDatabase
import com.botigocontigo.alfred.learn.repositories.room.RoomArticleRepository
import com.botigocontigo.alfred.utils.VolleyAdapter
import kotlinx.android.synthetic.main.content_learn.view.*

class LearnFragment : Fragment() {
    private lateinit var adapter: ArticleAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewFragment = inflater.inflate(R.layout.fragment_learn, container, false)

        val context = inflater.context
        val articleList = viewFragment.articleList!!
        articleList.layoutManager = LinearLayoutManager(context)
        adapter = ArticleAdapter(context)
        articleList.adapter = adapter

        val articleRepository = Services(context).intelligentArticleRepository()
        articleRepository.search("consejos para emprender", adapter)

        return viewFragment
    }

}