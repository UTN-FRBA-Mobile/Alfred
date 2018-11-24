package com.botigocontigo.alfred.learn.repositories.room

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler
import com.nhaarman.mockitokotlin2.argumentCaptor
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.io.IOException
import org.mockito.MockitoAnnotations
import org.junit.Assert


@RunWith(AndroidJUnit4::class)
class RoomArticleRepositoryTest {
    private lateinit var dao: RoomArticleDao
    private lateinit var db: LearnDatabase
    private lateinit var articleRepository: RoomArticleRepository

    @Mock
    private lateinit var articlesHandler: ArticlesHandler

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        db = Room.inMemoryDatabaseBuilder(context, LearnDatabase::class.java).build()
        dao = db.articleDao()
        articleRepository = RoomArticleRepository(dao)
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeArticleAndReadInListTest() {
        val article = Article("title", "description", "imageUrl", "link")
        articleRepository.saveArticle(article)
        articleRepository.fetchAll(articlesHandler)
        val captor = argumentCaptor<Article>()
        verify(articlesHandler, times(1)).handleArticle(captor.capture())
        Assert.assertEquals("title", captor.firstValue.title)
        Assert.assertEquals("description", captor.firstValue.description)
        Assert.assertEquals("imageUrl", captor.firstValue.imageUrl)
        Assert.assertEquals("link", captor.firstValue.link)
    }

    @Test
    @Throws(Exception::class)
    fun writeAndExistsMatchTest() {
        val article = Article("title", "description", "imageUrl", "http://specificurl.com")
        articleRepository.saveArticle(article)
        val isPresent = articleRepository.isPresent(article)
        Assert.assertEquals(true, isPresent)
    }

    @Test
    @Throws(Exception::class)
    fun writeAndExistsDoesNotMatchTest() {
        val article1 = Article("title", "description", "imageUrl", "http://fruta.com")
        val article2 = Article("title", "description", "imageUrl", "http://genericurl.com")
        articleRepository.saveArticle(article1)
        articleRepository.saveArticle(article2)
        val article = Article("title", "description", "imageUrl", "http://specificurl.com")
        val isPresent = articleRepository.isPresent(article)
        Assert.assertEquals(false, isPresent)
    }

}