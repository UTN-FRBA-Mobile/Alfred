package com.botigocontigo.alfred.learn

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.botigocontigo.alfred.R
import com.botigocontigo.alfred.Services
import com.botigocontigo.alfred.backend.Permissions
import com.botigocontigo.alfred.learn.fragments.ArticlesFragment
import com.botigocontigo.alfred.learn.fragments.BusyFragment
import com.botigocontigo.alfred.learn.fragments.ErrorFragment
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler


class LearnFragment : Fragment(), ArticlesHandler {

    private val errorFragment = ErrorFragment()
    private val busyFragment = BusyFragment()
    private val articlesFragment = ArticlesFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewFragment = inflater.inflate(R.layout.fragment_learn, container, false)
        showFragment(busyFragment)

        val context = inflater.context
        val services = Services(context)

        busyFragment.text = "Understanding your knowledge needs..."

        val user = services.currentUser()
        val permissions = Permissions(user)
        val learnQueryCallbacks = LearnQueryCallbacks(this)

        val botigocontigoApi = services.botigocontigoApi(permissions)
        botigocontigoApi.learnQuery().call(learnQueryCallbacks)

        return viewFragment
    }

    private fun showFragment(fragment: Fragment) {
        fragmentManager!!.beginTransaction().replace(R.id.mainContainer, fragment).commit()
    }

    private fun showError(message: String) {
        showFragment(errorFragment)
        errorFragment.text = message
    }

    // steps

    fun adaptativeQuerySuccess(query: String) {
        busyFragment.text = "Fetching the best articles for you..."
        val articleRepository = Services(context!!).intelligentArticleRepository()
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

    override fun error(query: String) {
        showError("Something went wrong on phase 1")
    }

}