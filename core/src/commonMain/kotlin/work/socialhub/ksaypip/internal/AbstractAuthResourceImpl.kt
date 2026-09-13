package work.socialhub.ksaypip.internal

import work.socialhub.khttpclient.HttpRequest
import work.socialhub.ksaypip.util.Headers

abstract class AbstractAuthResourceImpl(
    uri: String,
    val accessToken: String,
) : AbstractResourceImpl(uri) {

    fun bearerToken(): String {
        return "Bearer $accessToken"
    }

    /**
     * The client-supplied key that makes a retried write a repeat rather than a second write.
     */
    fun HttpRequest.idempotency(key: String?): HttpRequest {
        if (!key.isNullOrBlank()) {
            header(Headers.IDEMPOTENCY_KEY, key)
        }
        return this
    }
}
