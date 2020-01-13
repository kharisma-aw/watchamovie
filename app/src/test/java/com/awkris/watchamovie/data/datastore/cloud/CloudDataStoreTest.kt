package com.awkris.watchamovie.data.datastore.cloud

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import timber.log.Timber
import java.io.File
import kotlin.test.assertNotNull

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
abstract class CloudDataStoreTest {
    private lateinit var server: MockWebServer
    protected lateinit var baseUrl: String

    @Before
    @Throws(Exception::class)
    open fun setUp() {
        server = MockWebServer()
        baseUrl = server.url("/").toString()
    }

    @After
    @Throws(Exception::class)
    open fun tearDown() {
        server.shutdown()
    }

    @Throws(Exception::class)
    protected fun getJson(path: String): String {
        val uri = this.javaClass.classLoader!!.getResource(path)
        val json = File(assertNotNull(uri).path)
        return String(json.readBytes())
    }

    @Throws(Exception::class)
    protected fun enqueueSuccessResponse(responseFilePath: String) {
        enqueueResponse(200, responseFilePath)
    }

    @Throws(Exception::class)
    protected fun enqueueErrorResponse(responseFilePath: String) {
        enqueueResponse(500, responseFilePath)
    }

    @Throws(InterruptedException::class)
    protected fun takeRequest(): RecordedRequest {
        return server.takeRequest()
    }

    @Throws(Exception::class)
    private fun enqueueResponse(httpResponseCode: Int, responseFilePath: String) {
        Timber.d("enqueueing response")
        server.enqueue(
            MockResponse()
                .setResponseCode(httpResponseCode)
                .setBody(getJson(responseFilePath))
        )
    }
}