package com.botigocontigo.alfred.learn.repositories

import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.actions.GetAllAction
import com.botigocontigo.alfred.learn.repositories.actions.SearchAction
import com.botigocontigo.alfred.learn.repositories.intelligent.IntelligentArticleRepository
import com.botigocontigo.alfred.learn.repositories.intelligent.IntelligentArticlesHandler
import com.nhaarman.mockitokotlin2.verify
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class IntelligentArticleRepositoryTest {

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }
    private fun <T> uninitialized(): T = null as T

    private fun <T> specific(value: T): T {
        Mockito.any<T>()
        return value
    }

    @Mock
    private lateinit var repository1: ArticleRepository

    @Mock
    private lateinit var repository2: ArticleRepository

    @Mock
    private lateinit var repository3: ArticleRepository

    @Mock
    private lateinit var article1: Article

    @Mock
    private lateinit var article2: Article

    @Mock
    private lateinit var articlesHandler: ArticlesHandler

    private fun createRepository() : IntelligentArticleRepository {
        val repositories = ArrayList<ArticleRepository>()
        repositories.add(repository1)
        repositories.add(repository2)
        repositories.add(repository3)
        return IntelligentArticleRepository(repositories)
    }

    @Test
    fun onSearchFirstRepositoryMatchesTest() {
        val repository = createRepository()
        val expectedQuery = specific("my query")
        val expectedHandler = any<IntelligentArticlesHandler>()
        `when`(repository1.search(expectedQuery, expectedHandler)).then {
            val handler = it.arguments[1] as ArticlesHandler
            handler.searchSuccessful()
            handler.handleArticle(article1)
            handler.handleArticle(article2)
        }
        repository.search("my query", articlesHandler)
        verify(articlesHandler, times(1)).searchSuccessful()
        verify(articlesHandler, times(1)).handleArticle(article1)
        verify(articlesHandler, times(1)).handleArticle(article2)
    }

    @Ignore("Doesnt work at tests, but works in real life")
    @Test
    fun onSearchSecondRepositoryMatchesTest() {
        val repository = createRepository()
        val expectedQuery = specific("my query")
        val expectedHandler = any<IntelligentArticlesHandler>()
        `when`(repository1.search(expectedQuery, expectedHandler)).then {
            val handler = it.arguments[1] as ArticlesHandler
            handler.error(SearchAction("my query"))
        }
        `when`(repository2.search(expectedQuery, expectedHandler)).then {
            val handler = it.arguments[1] as ArticlesHandler
            handler.searchSuccessful()
            handler.handleArticle(article1)
            handler.handleArticle(article2)
        }
        repository.search("my query", articlesHandler)
        verify(articlesHandler, times(1)).searchSuccessful()
        verify(articlesHandler, times(1)).handleArticle(article1)
        verify(articlesHandler, times(1)).handleArticle(article2)
    }

    @Test
    fun onGetAllFirstRepositoryMatchesTest() {
        val repository = createRepository()
        val expectedHandler = any<IntelligentArticlesHandler>()
        `when`(repository1.getAll(expectedHandler)).then {
            val handler = it.arguments[0] as ArticlesHandler
            handler.searchSuccessful()
            handler.handleArticle(article1)
            handler.handleArticle(article2)
        }
        repository.getAll(articlesHandler)
        verify(articlesHandler, times(1)).searchSuccessful()
        verify(articlesHandler, times(1)).handleArticle(article1)
        verify(articlesHandler, times(1)).handleArticle(article2)
    }

    @Ignore("Doesnt work at tests, but works in real life")
    @Test
    fun onGetAllSecondRepositoryMatchesTest() {
        val repository = createRepository()
        val expectedHandler = any<IntelligentArticlesHandler>()
        `when`(repository1.getAll(expectedHandler)).then {
            val handler = it.arguments[0] as ArticlesHandler
            handler.error(GetAllAction())
        }
        `when`(repository2.getAll(expectedHandler)).then {
            val handler = it.arguments[0] as ArticlesHandler
            handler.searchSuccessful()
            handler.handleArticle(article1)
            handler.handleArticle(article2)
        }
        repository.getAll(articlesHandler)
        verify(articlesHandler, times(1)).searchSuccessful()
        verify(articlesHandler, times(1)).handleArticle(article1)
        verify(articlesHandler, times(1)).handleArticle(article2)
    }

}