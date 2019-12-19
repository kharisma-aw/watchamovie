package com.awkris.watchamovie.testutils

import android.net.UrlQuerySanitizer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Assert.assertEquals

class RequestAssertionUtils(recordedRequest: RecordedRequest) {
    private val sanitizer = UrlQuerySanitizer()
    private val request = recordedRequest
    private val queryParams: Map<String, String>

    enum class Method(var text: String) {
        GET("GET"), POST("POST"), DELETE("DELETE"), PUT("PUT");
    }

    init {
        sanitizer.allowUnregisteredParamaters = true
        sanitizer.unregisteredParameterValueSanitizer = UrlQuerySanitizer.getAmpLegal()
        sanitizer.parseUrl(recordedRequest.path)
        sanitizer.parseQuery(recordedRequest.body.readUtf8())
        queryParams = HashMap()
        for (param in sanitizer.parameterList) {
            queryParams[param.mParameter] = param.mValue
        }
    }

    fun assertRequestLine(expectedMethod: Method, expectedPath: String) {
        val actualPath = standardizePath(request.path)
        assertEquals(expectedMethod.text, request.method)
        assertEquals(standardizePath(expectedPath), actualPath)
    }

    fun assertRequestLine(
        expectedMethod: Method,
        expectedPathFormat: String,
        vararg args: String
    ) {
        assertRequestLine(expectedMethod, format(expectedPathFormat, *args))
    }

    fun assertNoParam() {
        assertNoQueryParam()
        assertNoBodyParam()
    }

    fun assertTotalBodyParams(expectedTotalBodyParams: Int) {
        assertEquals(
            expectedTotalBodyParams.toLong(),
            sanitizer.parameterList.size.toLong()
        )
    }

    fun assertNoBodyParam() {
        assertTotalBodyParams(0)
    }

    fun assertBodyParam(expectedKey: String, expectedValue: String) {
        assertEquals(expectedValue, sanitizer.getValue(expectedKey))
    }

    fun assertBodyParam(expectedKey: String, expectedValue: Boolean) {
        assertBodyParam(expectedKey, expectedValue.toString())
    }

    fun assertBodyParam(expectedKey: String, expectedValue: Int) {
        assertBodyParam(expectedKey, expectedValue.toString())
    }

    fun assertBodyParam(expectedKey: String, expectedValue: Long) {
        assertBodyParam(expectedKey, expectedValue.toString())
    }

    fun assertBodyParam(expectedKey: String, expectedValue: Char) {
        assertBodyParam(expectedKey, expectedValue.toString())
    }

    fun assertBodyParam(expectedKey: String, expectedValue: Float) {
        assertBodyParam(expectedKey, expectedValue.toString())
    }

    fun assertBodyParam(expectedKey: String, expectedValue: Double) {
        assertBodyParam(expectedKey, expectedValue.toString())
    }

    fun assertTotalQueryParams(expectedTotalQueryParams: Int) {
        assertEquals(expectedTotalQueryParams.toLong(), queryParams.size.toLong())
    }

    fun assertNoQueryParam() {
        assertTotalQueryParams(0)
    }

    fun assertQueryParam(expectedKey: String, expectedValue: String) {
        assertEquals(expectedValue, queryParams[expectedKey])
    }

    fun assertQueryParam(expectedKey: String, expectedValue: Int) {
        assertQueryParam(expectedKey, expectedValue.toString())
    }

    fun assertQueryParam(expectedKey: String, expectedValue: Long) {
        assertQueryParam(expectedKey, expectedValue.toString())
    }

    fun assertQueryParam(expectedKey: String, expectedValue: Char) {
        assertQueryParam(expectedKey, expectedValue.toString())
    }

    fun assertQueryParam(expectedKey: String, expectedValue: Float) {
        assertQueryParam(expectedKey, expectedValue.toString())
    }

    fun assertQueryParam(expectedKey: String, expectedValue: Double) {
        assertQueryParam(expectedKey, expectedValue.toString())
    }

    private fun standardizePath(_actualPath: String): String {
        var actualPath = _actualPath
        if (actualPath.contains("?")) {
            actualPath = actualPath.substring(0, actualPath.indexOf("?"))
        }
        return if (actualPath[0] == '/') {
            actualPath.substring(1)
        } else actualPath
    }

    private fun format(format: String, vararg args: String): String {
        return String.format(format.replace("\\{[A-za-z0-9]+\\}".toRegex(), "%s"), *args)
    }
}