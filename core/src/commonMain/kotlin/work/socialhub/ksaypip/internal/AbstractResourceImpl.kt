package work.socialhub.ksaypip.internal

import work.socialhub.khttpclient.HttpRequest
import work.socialhub.khttpclient.HttpResponse
import work.socialhub.ksaypip.SaypipException
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.internal.InternalUtility.errorOf
import work.socialhub.ksaypip.internal.InternalUtility.toJson

abstract class AbstractResourceImpl(
    val uri: String,
) {

    protected suspend inline fun <reified T> proceed(
        function: suspend () -> HttpResponse,
    ): Response<T> {
        try {
            val response = function()
            if (response.status in 200..299) {
                return Response(fromJson<T>(response.stringBody)).also {
                    it.json = response.stringBody
                    it.status = response.status
                }
            }
            throw errorOf(response.status, response.stringBody)
        } catch (e: Exception) {
            throw e as? SaypipException ?: SaypipException(e)
        }
    }

    protected suspend inline fun proceedUnit(
        function: suspend () -> HttpResponse,
    ): ResponseUnit {
        try {
            val response = function()
            if (response.status in 200..299) {
                return ResponseUnit().also {
                    it.json = response.stringBody
                    it.status = response.status
                }
            }
            throw errorOf(response.status, response.stringBody)
        } catch (e: Exception) {
            throw e as? SaypipException ?: SaypipException(e)
        }
    }

    protected suspend inline fun proceedBytes(
        function: suspend () -> HttpResponse,
    ): Response<ByteArray> {
        try {
            val response = function()
            if (response.status in 200..299) {
                return Response(response.body).also {
                    it.json = response.stringBody
                    it.status = response.status
                }
            }
            throw errorOf(response.status, response.stringBody)
        } catch (e: Exception) {
            throw e as? SaypipException ?: SaypipException(e)
        }
    }

    protected inline fun <reified T> fromJson(obj: String): T {
        return InternalUtility.fromJson(obj)
    }

    /**
     * A JSON body. Only valid where the request carries no other body parameter: the HTTP layer
     * sends a lone JSON parameter as the request body and nothing else.
     */
    inline fun <reified T> HttpRequest.body(obj: T): HttpRequest {
        return json(toJson(obj))
    }

    /** A body parameter, omitted when null. */
    fun HttpRequest.pwn(
        key: String,
        value: Any?,
    ): HttpRequest {
        if (value != null) param(key, value)
        return this
    }

    /** A query parameter, omitted when null. */
    fun HttpRequest.qwn(
        key: String,
        value: Any?,
    ): HttpRequest {
        if (value != null) query(key, value)
        return this
    }

    /**
     * `?cursor=…&limit=…`, the pagination every paged list here speaks.
     */
    fun HttpRequest.pagination(
        cursor: String?,
        limit: Int?,
    ): HttpRequest {
        qwn("cursor", cursor)
        qwn("limit", limit)
        return this
    }
}
