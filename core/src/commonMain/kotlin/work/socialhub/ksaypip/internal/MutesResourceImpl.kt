package work.socialhub.ksaypip.internal

import kotlinx.serialization.json.put
import work.socialhub.khttpclient.HttpRequest
import work.socialhub.ksaypip.api.MutesResource
import work.socialhub.ksaypip.api.request.mutes.MutesListRequest
import work.socialhub.ksaypip.api.request.mutes.MutesMuteRequest
import work.socialhub.ksaypip.api.request.mutes.MutesUnmuteRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.api.response.mutes.MutesListResponse
import work.socialhub.ksaypip.internal.InternalUtility.urlEncode
import work.socialhub.ksaypip.util.Headers.AUTHORIZATION
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class MutesResourceImpl(
    uri: String,
    accessToken: String,
) : AbstractAuthResourceImpl(uri, accessToken),
    MutesResource {

    override suspend fun list(request: MutesListRequest): Response<MutesListResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/mutes")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .pagination(request.cursor, request.limit)
                .get()
        }
    }

    override fun listBlocking(request: MutesListRequest): Response<MutesListResponse> {
        return toBlocking { list(request) }
    }

    override suspend fun mute(request: MutesMuteRequest): ResponseUnit {
        return proceedUnit {
            HttpRequest()
                .url("${uri}/api/mutes")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .idempotency(request.idempotencyKey)
                .jsonBody {
                    if (request.postId != null && request.identity == null) {
                        put("target", "post")
                        put("postId", request.postId)
                    } else {
                        put("target", "identity")
                        putOrNull("identity", request.identity)
                    }
                    putOrNull("duration", request.duration)
                }
                .post()
        }
    }

    override fun muteBlocking(request: MutesMuteRequest): ResponseUnit {
        return toBlocking { mute(request) }
    }

    override suspend fun unmute(request: MutesUnmuteRequest): ResponseUnit {
        return proceedUnit {
            HttpRequest()
                .url("${uri}/api/mutes/${urlEncode(request.identityToken.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .delete()
        }
    }

    override fun unmuteBlocking(request: MutesUnmuteRequest): ResponseUnit {
        return toBlocking { unmute(request) }
    }
}
