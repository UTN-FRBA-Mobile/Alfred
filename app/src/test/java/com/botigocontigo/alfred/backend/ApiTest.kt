package com.botigocontigo.alfred.backend

import com.botigocontigo.alfred.utils.AsyncTaskCallbacks
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner

private const val FAKE_USER_ID = "fakeUserId"

@RunWith(MockitoJUnitRunner::class)
class ApiTest {

    @Mock
    private lateinit var adapter: NetworkingAdapter

    @Mock
    private lateinit var permissions: Permissions

    @Mock
    private lateinit var callbacks: AsyncTaskCallbacks<String>

    @Test
    fun learnQueryTest() {
        `when`(permissions.getUserId()).thenReturn(FAKE_USER_ID)
        val api = Api(adapter, permissions)
        api.learnQuery().call(callbacks)
        val expectedQueue = "botigocontigo-api"
        val expectedMethod = "post"
        val expectedUrl = "http://178.128.229.73:3300/methods/api.query?userId=fakeUserId"
        verify(adapter, times(1)).enqueue(expectedQueue, expectedMethod, expectedUrl, callbacks)
    }

}
