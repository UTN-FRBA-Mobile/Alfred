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
        val article = Article("title", "body", "imageUrl")
        articleRepository.saveArticle(article)
        articleRepository.fetchAll(articlesHandler)
        val captor = argumentCaptor<Article>()
        verify(articlesHandler, times(1)).handleArticle(captor.capture())
        Assert.assertEquals("title", captor.firstValue.getTitle())
        Assert.assertEquals("body", captor.firstValue.getBody())
        Assert.assertEquals("imageUrl", captor.firstValue.getImageUrl())
    }

}