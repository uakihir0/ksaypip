package work.socialhub.ksaypip.internal

import kotlinx.serialization.json.put
import work.socialhub.khttpclient.HttpRequest
import work.socialhub.ksaypip.api.WordMutesResource
import work.socialhub.ksaypip.api.request.wordmutes.WordMutesForgetRequest
import work.socialhub.ksaypip.api.request.wordmutes.WordMutesListRequest
import work.socialhub.ksaypip.api.request.wordmutes.WordMutesMuteRequest
import work.socialhub.ksaypip.api.request.wordmutes.WordMutesWindowRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.api.response.wordmutes.WordMutesListResponse
import work.socialhub.ksaypip.internal.InternalUtility.urlEncode
import work.socialhub.ksaypip.util.Headers.AUTHORIZATION
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class WordMutesResourceImpl(
    uri: String,
    accessToken: String,
) : AbstractAuthResourceImpl(uri, accessToken),
    WordMutesResource {

    override suspend fun list(request: WordMutesListRequest): Response<WordMutesListResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/word-mutes")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .pagination(request.cursor, request.limit)
                .get()
        }
    }

    override fun listBlocking(request: WordMutesListRequest): Response<WordMutesListResponse> {
        return toBlocking { list(request) }
    }

    override suspend fun mute(request: WordMutesMuteRequest): ResponseUnit {
        return proceedUnit {
            HttpRequest()
                .url("${uri}/api/word-mutes")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .idempotency(request.idempotencyKey)
                .jsonBody {
                    putOrNull("word", request.word)
                    putOrNull("duration", request.duration)
                }
                .post()
        }
    }

    override fun muteBlocking(request: WordMutesMuteRequest): ResponseUnit {
        return toBlocking { mute(request) }
    }

    override suspend fun setWindow(request: WordMutesWindowRequest): ResponseUnit {
        return proceedUnit {
            HttpRequest()
                .url("${uri}/api/word-mutes/${urlEncode(request.wordMuteId.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .jsonBody {
                    putOrNull("duration", request.duration)
                }
                .put()
        }
    }

    override fun setWindowBlocking(request: WordMutesWindowRequest): ResponseUnit {
        return toBlocking { setWindow(request) }
    }

    override suspend fun forget(request: WordMutesForgetRequest): ResponseUnit {
        return proceedUnit {
            HttpRequest()
                .url("${uri}/api/word-mutes/${urlEncode(request.wordMuteId.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .idempotency(request.idempotencyKey)
                .delete()
        }
    }

    override fun forgetBlocking(request: WordMutesForgetRequest): ResponseUnit {
        return toBlocking { forget(request) }
    }
}
