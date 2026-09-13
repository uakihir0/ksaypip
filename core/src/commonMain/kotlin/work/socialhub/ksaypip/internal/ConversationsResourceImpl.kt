package work.socialhub.ksaypip.internal

import kotlinx.serialization.json.put
import work.socialhub.khttpclient.HttpRequest
import work.socialhub.ksaypip.api.ConversationsResource
import work.socialhub.ksaypip.api.request.conversations.ConversationsConversationRequest
import work.socialhub.ksaypip.api.request.conversations.ConversationsLeaveRequest
import work.socialhub.ksaypip.api.request.conversations.ConversationsListRequest
import work.socialhub.ksaypip.api.request.conversations.ConversationsReadRequest
import work.socialhub.ksaypip.api.request.conversations.ConversationsReplyRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.api.response.conversations.ConversationsConversationResponse
import work.socialhub.ksaypip.api.response.conversations.ConversationsListResponse
import work.socialhub.ksaypip.api.response.conversations.ConversationsReplyResponse
import work.socialhub.ksaypip.internal.InternalUtility.urlEncode
import work.socialhub.ksaypip.util.Headers.AUTHORIZATION
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class ConversationsResourceImpl(
    uri: String,
    accessToken: String,
) : AbstractAuthResourceImpl(uri, accessToken),
    ConversationsResource {

    override suspend fun list(request: ConversationsListRequest): Response<ConversationsListResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/conversations")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .pagination(request.cursor, request.limit)
                .get()
        }
    }

    override fun listBlocking(request: ConversationsListRequest): Response<ConversationsListResponse> {
        return toBlocking { list(request) }
    }

    override suspend fun conversation(
        request: ConversationsConversationRequest,
    ): Response<ConversationsConversationResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/conversations/${urlEncode(request.conversationId.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .pagination(request.cursor, request.limit)
                .get()
        }
    }

    override fun conversationBlocking(
        request: ConversationsConversationRequest,
    ): Response<ConversationsConversationResponse> {
        return toBlocking { conversation(request) }
    }

    override suspend fun reply(request: ConversationsReplyRequest): Response<ConversationsReplyResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/conversations/${urlEncode(request.conversationId.orEmpty())}/replies")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .idempotency(request.idempotencyKey)
                .jsonBody {
                    putOrNull("body", request.body)
                }
                .post()
        }
    }

    override fun replyBlocking(request: ConversationsReplyRequest): Response<ConversationsReplyResponse> {
        return toBlocking { reply(request) }
    }

    override suspend fun read(request: ConversationsReadRequest): ResponseUnit {
        return proceedUnit {
            HttpRequest()
                .url("${uri}/api/conversations/${urlEncode(request.conversationId.orEmpty())}/read")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .post()
        }
    }

    override fun readBlocking(request: ConversationsReadRequest): ResponseUnit {
        return toBlocking { read(request) }
    }

    override suspend fun leave(request: ConversationsLeaveRequest): ResponseUnit {
        return proceedUnit {
            HttpRequest()
                .url("${uri}/api/conversations/${urlEncode(request.conversationId.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .idempotency(request.idempotencyKey)
                .delete()
        }
    }

    override fun leaveBlocking(request: ConversationsLeaveRequest): ResponseUnit {
        return toBlocking { leave(request) }
    }
}
