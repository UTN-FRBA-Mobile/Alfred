package com.botigocontigo.alfred.learn

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.botigocontigo.alfred.R
import com.botigocontigo.alfred.Services
import com.botigocontigo.alfred.learn.fragments.ArticlesFragment
import com.botigocontigo.alfred.learn.fragments.BusyFragment
import com.botigocontigo.alfred.learn.fragments.ErrorFragment
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler
import com.botigocontigo.alfred.learn.repositories.actions.ArticleRepositoryAction


class LearnFragment : Fragment(), ArticlesHandler {

    private val errorFragment = ErrorFragment()
    private val busyFragment = BusyFragment()
    private val articlesFragment = ArticlesFragment()
    private val learnOptionsFragment = LearnOptionsFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        learnOptionsFragment.learnFragment = this
        val viewFragment = inflater.inflate(R.layout.fragment_learn, container, false)

        val floatingButton: View = viewFragment.findViewById(R.id.options)
        floatingButton.setOnClickListener { showOptions() }

        startSearch(inflater.context)

        return viewFragment
    }

    private fun showFragment(fragment: Fragment) {
        fragmentManager!!.beginTransaction().replace(R.id.mainContainer, fragment).commit()
    }

    private fun showError(message: String) {
        showFragment(errorFragment)
        errorFragment.updateText(message)
    }

    private fun showOptions() {
        showFragment(learnOptionsFragment)
    }

    private fun startSearch(context: Context) {
        showFragment(busyFragment)

        val services = Services(context)

        busyFragment.updateText("Understanding your knowledge needs...")

        val learnQueryCallbacks = LearnQueryCallbacks(this)

        val botigocontigoApi = services.botigocontigoApi()
        botigocontigoApi.learnQuery().call(learnQueryCallbacks)
    }

    private fun startGetAll(context: Context) {
        showFragment(busyFragment)

        val services = Services(context)

        busyFragment.updateText("Looking for your favorites...")

        val repository = services.favoritesArticleRepository()
        repository.getAll(this)
    }

    fun showQueryArticles() {
        startSearch(context!!)
    }

    fun showFavoritesArticles() {
        startGetAll(context!!)
    }

    // steps

    fun adaptativeQuerySuccess(query: String) {
        learnOptionsFragment.query = query
        busyFragment.updateText("Fetching the best articles for you...")
        val articleRepository = Services(context!!).generalArticleRepository()
        articleRepository.search(query, this)
    }

    fun adaptativeQueryError() {
        showError("Something went wrong on phase 1")
    }

    override fun searchSuccessful() {
        showFragment(articlesFragment)
        articlesFragment.searchSuccessful()
    }

    override fun handleArticle(article: Article) {
        articlesFragment.handleArticle(article)
    }

    override fun error(action: ArticleRepositoryAction) {
        showError("Something went wrong on phase 1")
    }

}