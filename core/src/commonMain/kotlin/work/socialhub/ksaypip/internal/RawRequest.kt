package work.socialhub.ksaypip.internal

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse as KtorHttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.content.ByteArrayContent
import io.ktor.util.toMap
import work.socialhub.khttpclient.HttpResponse
import work.socialhub.ksaypip.util.MediaType

/**
 * The two requests the shared HTTP helper cannot express.
 *
 * Everywhere else a request goes through khttpclient, as in the sibling libraries. These two
 * cannot: an upload's body content type is the caller's statement about the bytes and not
 * something to derive from a filename extension, and a feedback screenshot is a multipart part
 * whose type has to be the same statement. So these are built on the Ktor client directly — the
 * same client khttpclient itself opens, with the platform engine found the same way — and the
 * answer is wrapped back into a [HttpResponse] so the resource implementations proceed exactly
 * as they do elsewhere.
 */
object RawRequest {

    suspend fun postBytes(
        url: String,
        accessToken: String,
        contentType: String,
        bytes: ByteArray,
        extraHeaders: Map<String, String> = emptyMap(),
    ): HttpResponse {
        return request { client ->
            client.post {
                this.url(url)
                header(HttpHeaders.Authorization, "Bearer $accessToken")
                header(HttpHeaders.Accept, MediaType.JSON)
                extraHeaders.forEach { (key, value) -> header(key, value) }
                setBody(ByteArrayContent(bytes, ContentType.parse(contentType)))
            }
        }
    }

    suspend fun postMultipart(
        url: String,
        accessToken: String,
        params: Map<String, String>,
        fileKey: String?,
        fileName: String?,
        fileContentType: String?,
        fileBytes: ByteArray?,
        extraHeaders: Map<String, String> = emptyMap(),
    ): HttpResponse {
        return request { client ->
            client.post {
                this.url(url)
                header(HttpHeaders.Authorization, "Bearer $accessToken")
                header(HttpHeaders.Accept, MediaType.JSON)
                extraHeaders.forEach { (key, value) -> header(key, value) }
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            params.forEach { (key, value) -> append(key, value) }
                            if (fileKey != null && fileBytes != null) {
                                append(
                                    fileKey,
                                    fileBytes,
                                    Headers.build {
                                        append(
                                            HttpHeaders.ContentType,
                                            fileContentType ?: ContentType.Application.OctetStream.toString(),
                                        )
                                        append(
                                            HttpHeaders.ContentDisposition,
                                            "filename=${fileName ?: "file"}",
                                        )
                                    },
                                )
                            }
                        },
                    ),
                )
            }
        }
    }

    private suspend fun request(
        block: suspend (HttpClient) -> KtorHttpResponse,
    ): HttpResponse {
        return HttpClient {
            followRedirects = true
        }.use { client ->
            val response = block(client)
            HttpResponse(
                status = response.status.value,
                headers = response.headers.toMap(),
                body = response.body(),
            )
        }
    }
}
