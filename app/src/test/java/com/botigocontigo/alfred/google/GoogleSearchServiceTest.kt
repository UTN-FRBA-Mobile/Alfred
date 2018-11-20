package com.botigocontigo.alfred.google

import com.botigocontigo.alfred.utils.NetworkingAdapter
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GoogleSearchServiceTest {

    @Mock
    private lateinit var adapter: NetworkingAdapter

    @Mock
    private lateinit var callbacks: GoogleSearchCallbacks

    @Test
    fun searchTest() {
        val credentials = Credentials("mykey", "mycx")
        val api = GoogleApi(adapter, credentials)
        val service = GoogleSearchService(api)
        val q = "this+is+a+test"
        service.search("this is a test", callbacks)
        val expectedQueue = "google-api"
        val expectedMethod = "get"
        val expectedUrl = "https://www.googleapis.com/customsearch/v1/siterestrict?q=this+is+a+test&cx=mycx&key=mykey"
        Mockito.verify(adapter, Mockito.times(1)).enqueue(expectedQueue, expectedMethod, expectedUrl, callbacks)
    }

}