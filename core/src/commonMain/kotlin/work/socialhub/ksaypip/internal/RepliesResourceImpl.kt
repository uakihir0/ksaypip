package work.socialhub.ksaypip.internal

import kotlinx.serialization.json.put
import work.socialhub.khttpclient.HttpRequest
import work.socialhub.ksaypip.api.RepliesResource
import work.socialhub.ksaypip.api.request.replies.RepliesReactRequest
import work.socialhub.ksaypip.api.request.replies.RepliesUnreactRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.replies.RepliesReactResponse
import work.socialhub.ksaypip.api.response.replies.RepliesUnreactResponse
import work.socialhub.ksaypip.internal.InternalUtility.urlEncode
import work.socialhub.ksaypip.util.Headers.AUTHORIZATION
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class RepliesResourceImpl(
    uri: String,
    accessToken: String,
) : AbstractAuthResourceImpl(uri, accessToken),
    RepliesResource {

    override suspend fun react(request: RepliesReactRequest): Response<RepliesReactResponse> {
        return proceed {
            val http = HttpRequest()
                .url("${uri}/api/replies/${urlEncode(request.replyId.orEmpty())}/reactions/${urlEncode(request.emoji.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)

            // The mode is an optional body: absent means anonymous, which is the old write.
            request.identified?.let { identified ->
                http.jsonBody {
                    put("identified", identified)
                }
            }

            http.put()
        }
    }

    override fun reactBlocking(request: RepliesReactRequest): Response<RepliesReactResponse> {
        return toBlocking { react(request) }
    }

    override suspend fun unreact(request: RepliesUnreactRequest): Response<RepliesUnreactResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/replies/${urlEncode(request.replyId.orEmpty())}/reactions/${urlEncode(request.emoji.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .delete()
        }
    }

    override fun unreactBlocking(request: RepliesUnreactRequest): Response<RepliesUnreactResponse> {
        return toBlocking { unreact(request) }
    }
}
