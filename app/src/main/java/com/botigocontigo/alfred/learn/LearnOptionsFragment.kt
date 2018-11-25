package com.botigocontigo.alfred.learn

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.botigocontigo.alfred.R

class LearnOptionsFragment : Fragment() {
    var learnFragment: LearnFragment? = null
    var query: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewFragment = inflater.inflate(R.layout.fragment_learn_options, container, false)
        val queryButton = viewFragment.findViewById<View>(R.id.queryButton)
        val favoritesButton = viewFragment.findViewById<View>(R.id.favoritesButton)
        queryButton.setOnClickListener { learnFragment!!.showQueryArticles() }
        favoritesButton.setOnClickListener { learnFragment!!.showFavoritesArticles() }
        val queryText = viewFragment.findViewById<TextView>(R.id.queryText)
        queryText.text = query
        return viewFragment
    }

}