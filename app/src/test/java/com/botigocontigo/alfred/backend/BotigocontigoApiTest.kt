package com.botigocontigo.alfred.backend

import com.botigocontigo.alfred.utils.Api
import com.botigocontigo.alfred.utils.AsyncTaskCallbacks
import com.botigocontigo.alfred.utils.NetworkingAdapter
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner

private const val FAKE_USER_ID = "fakeUserId"

@RunWith(MockitoJUnitRunner::class)
class BotigocontigoApiTest {

    @Mock
    private lateinit var adapter: NetworkingAdapter

    @Mock
    private lateinit var permissions: Permissions

    @Mock
    private lateinit var callbacks: AsyncTaskCallbacks<String>

    @Test
    fun learnQueryTest() {
        `when`(permissions.getUserId()).thenReturn(FAKE_USER_ID)
        val api = BotigocontigoApi(adapter, permissions)
        api.learnQuery().call(callbacks)
        val expectedQueue = "botigocontigo-api"
        val expectedMethod = "post"
        val expectedUrl = "http://178.128.229.73:3300/methods/api.query"
        val expectedBody = "{\"userId\":\"fakeUserId\"}"
        verify(adapter, times(1)).enqueue(expectedQueue, expectedMethod, expectedUrl, expectedBody, callbacks)
    }

    @Test
    fun saveFavoriteArticleWithoutImageTest() {
        `when`(permissions.getUserId()).thenReturn(FAKE_USER_ID)
        val api = BotigocontigoApi(adapter, permissions)
        val title = "Articulo de prueba"
        val description = "Aqui va un lorem ipsum"
        val link = "http://google.com"
        val imageUrl = null
        api.saveFavoriteArticle(title, description, link, imageUrl).call(callbacks)
        val expectedQueue = "botigocontigo-api"
        val expectedMethod = "post"
        val expectedUrl = "http://178.128.229.73:3300/methods/api.insertFavourite"
        val expectedBody = "{\"favourite\":{\"title\":\"Articulo de prueba\",\"description\":\"Aqui va un lorem ipsum\",\"link\":\"http://google.com\"},\"userId\":\"fakeUserId\"}"
        verify(adapter, times(1)).enqueue(expectedQueue, expectedMethod, expectedUrl, expectedBody, callbacks)
    }

    @Test
    fun saveFavoriteArticleWithImageTest() {
        `when`(permissions.getUserId()).thenReturn(FAKE_USER_ID)
        val api = BotigocontigoApi(adapter, permissions)
        val title = "Articulo de prueba"
        val description = "Aqui va un lorem ipsum"
        val link = "http://google.com"
        val imageUrl = "https://www.google.com.ar/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"
        api.saveFavoriteArticle(title, description, link, imageUrl).call(callbacks)
        val expectedQueue = "botigocontigo-api"
        val expectedMethod = "post"
        val expectedUrl = "http://178.128.229.73:3300/methods/api.insertFavourite"
        val expectedBody = "{\"favourite\":{\"title\":\"Articulo de prueba\",\"description\":\"Aqui va un lorem ipsum\",\"link\":\"http://google.com\",\"imageUrl\":\"https://www.google.com.ar/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png\"},\"userId\":\"fakeUserId\"}"
        verify(adapter, times(1)).enqueue(expectedQueue, expectedMethod, expectedUrl, expectedBody, callbacks)
    }

}
