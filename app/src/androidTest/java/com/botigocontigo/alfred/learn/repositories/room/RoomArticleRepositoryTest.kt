package com.botigocontigo.alfred.learn.repositories.room

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.repositories.ArticlePresentHandler
import com.botigocontigo.alfred.learn.repositories.ArticlesHandler
import com.nhaarman.mockitokotlin2.argumentCaptor
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.io.IOException
import org.mockito.MockitoAnnotations
import java.util.concurrent.CountDownLatch


@RunWith(AndroidJUnit4::class)
class RoomArticleRepositoryTest {
    private lateinit var dao: RoomArticleDao
    private lateinit var db: LearnDatabase
    private lateinit var articleRepository: RoomArticleRepository

    @Mock
    private lateinit var articlesHandler: ArticlesHandler

    @Mock
    private lateinit var articlePresentHandler: ArticlePresentHandler

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        db = Room.inMemoryDatabaseBuilder(context, LearnDatabase::class.java).build()
        dao = db.articleDao()
        articleRepository = RoomArticleRepository(dao)
        dao.nuke()
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
        val article = Article("title", "description", "link", "imageUrl")
        var c = CountDownLatch(1)
        articleRepository.onAsyncTaskDone = { c.countDown() }
        articleRepository.upsert(article)
        c.await()
        c = CountDownLatch(1)
        articleRepository.onAsyncTaskDone = { c.countDown() }
        articleRepository.getAll(articlesHandler)
        c.await()
        val captor = argumentCaptor<Article>()
        verify(articlesHandler, times(1)).handleArticle(captor.capture())
        Assert.assertEquals("title", captor.firstValue.title)
        Assert.assertEquals("description", captor.firstValue.description)
        Assert.assertEquals("link", captor.firstValue.link)
        Assert.assertEquals("imageUrl", captor.firstValue.imageUrl)
    }

    @Test
    @Throws(Exception::class)
    fun writeAndExistsMatchTest() {
        val article = Article("title", "description", "http://specificurl.com", "imageUrl")
        var c = CountDownLatch(1)
        articleRepository.onAsyncTaskDone = { c.countDown() }
        articleRepository.upsert(article)
        c.await()
        c = CountDownLatch(1)
        articleRepository.onAsyncTaskDone = { c.countDown() }
        articleRepository.isPresent(article, articlePresentHandler)
        c.await()
        verify(articlePresentHandler, times(1)).success(true)
    }

    @Test
    @Throws(Exception::class)
    fun writeAndExistsDoesNotMatchTest() {
        val article1 = Article("title", "description", "http://fruta.com", "imageUrl")
        val article2 = Article("title", "description", "http://genericurl.com", "imageUrl")
        var c = CountDownLatch(2)
        articleRepository.onAsyncTaskDone = { c.countDown() }
        articleRepository.upsert(article1)
        articleRepository.upsert(article2)
        c.await()
        val article = Article("title", "description", "imageUrl", "http://specificurl.com")
        c = CountDownLatch(1)
        articleRepository.onAsyncTaskDone = { c.countDown() }
        articleRepository.isPresent(article, articlePresentHandler)
        c.await()
        verify(articlePresentHandler, times(1)).success(false)
    }

}