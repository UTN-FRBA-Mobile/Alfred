package com.botigocontigo.alfred.learn

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.botigocontigo.alfred.R
import com.botigocontigo.alfred.Services
import com.botigocontigo.alfred.backend.Permissions
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

        val services = Services(context)

        val user = services.currentUser()
        val permissions = Permissions(user)
        val learnQueryCallbacks = LearnQueryCallbacks(adapter)

        val botigocontigoApi = services.botigocontigoApi(permissions)
        botigocontigoApi.learnQuery().call(learnQueryCallbacks)

        return viewFragment
    }

}