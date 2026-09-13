package work.socialhub.ksaypip.internal

import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.put
import work.socialhub.khttpclient.HttpRequest
import work.socialhub.ksaypip.api.PostsResource
import work.socialhub.ksaypip.api.request.posts.PostsConversationsRequest
import work.socialhub.ksaypip.api.request.posts.PostsCreateRequest
import work.socialhub.ksaypip.api.request.posts.PostsDeleteRequest
import work.socialhub.ksaypip.api.request.posts.PostsPostRequest
import work.socialhub.ksaypip.api.request.posts.PostsReactRequest
import work.socialhub.ksaypip.api.request.posts.PostsReactionsRequest
import work.socialhub.ksaypip.api.request.posts.PostsRemoveWantsTalkRequest
import work.socialhub.ksaypip.api.request.posts.PostsStartConversationRequest
import work.socialhub.ksaypip.api.request.posts.PostsUnreactRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.api.response.posts.PostsConversationsResponse
import work.socialhub.ksaypip.api.response.posts.PostsCreateResponse
import work.socialhub.ksaypip.api.response.posts.PostsPostResponse
import work.socialhub.ksaypip.api.response.posts.PostsReactResponse
import work.socialhub.ksaypip.api.response.posts.PostsReactionsResponse
import work.socialhub.ksaypip.api.response.posts.PostsStartConversationResponse
import work.socialhub.ksaypip.api.response.posts.PostsUnreactResponse
import work.socialhub.ksaypip.internal.InternalUtility.urlEncode
import work.socialhub.ksaypip.util.Headers.AUTHORIZATION
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class PostsResourceImpl(
    uri: String,
    accessToken: String,
) : AbstractAuthResourceImpl(uri, accessToken),
    PostsResource {

    override suspend fun post(request: PostsPostRequest): Response<PostsPostResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/posts/${urlEncode(request.postId.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .get()
        }
    }

    override fun postBlocking(request: PostsPostRequest): Response<PostsPostResponse> {
        return toBlocking { post(request) }
    }

    override suspend fun reactions(request: PostsReactionsRequest): Response<PostsReactionsResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/posts/${urlEncode(request.postId.orEmpty())}/reactions")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .get()
        }
    }

    override fun reactionsBlocking(request: PostsReactionsRequest): Response<PostsReactionsResponse> {
        return toBlocking { reactions(request) }
    }

    override suspend fun conversations(request: PostsConversationsRequest): Response<PostsConversationsResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/posts/${urlEncode(request.postId.orEmpty())}/conversations")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .get()
        }
    }

    override fun conversationsBlocking(request: PostsConversationsRequest): Response<PostsConversationsResponse> {
        return toBlocking { conversations(request) }
    }

    override suspend fun create(request: PostsCreateRequest): Response<PostsCreateResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/posts")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .idempotency(request.idempotencyKey)
                .jsonBody {
                    request.body?.let { put("body", it) }
                    request.mediaIds?.let { ids ->
                        put("mediaIds", buildJsonArray { ids.forEach { add(it) } })
                    }
                    request.wantsTalk?.let { put("wantsTalk", it) }
                    request.replyToPostId?.let { put("replyToPostId", it) }
                }
                .post()
        }
    }

    override fun createBlocking(request: PostsCreateRequest): Response<PostsCreateResponse> {
        return toBlocking { create(request) }
    }

    override suspend fun delete(request: PostsDeleteRequest): ResponseUnit {
        return proceedUnit {
            HttpRequest()
                .url("${uri}/api/posts/${urlEncode(request.postId.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .idempotency(request.idempotencyKey)
                .delete()
        }
    }

    override fun deleteBlocking(request: PostsDeleteRequest): ResponseUnit {
        return toBlocking { delete(request) }
    }

    override suspend fun removeWantsTalk(request: PostsRemoveWantsTalkRequest): ResponseUnit {
        return proceedUnit {
            HttpRequest()
                .url("${uri}/api/posts/${urlEncode(request.postId.orEmpty())}/wants-talk")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .delete()
        }
    }

    override fun removeWantsTalkBlocking(request: PostsRemoveWantsTalkRequest): ResponseUnit {
        return toBlocking { removeWantsTalk(request) }
    }

    override suspend fun react(request: PostsReactRequest): Response<PostsReactResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/posts/${urlEncode(request.postId.orEmpty())}/reactions/${urlEncode(request.emoji.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .put()
        }
    }

    override fun reactBlocking(request: PostsReactRequest): Response<PostsReactResponse> {
        return toBlocking { react(request) }
    }

    override suspend fun unreact(request: PostsUnreactRequest): Response<PostsUnreactResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/posts/${urlEncode(request.postId.orEmpty())}/reactions/${urlEncode(request.emoji.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .delete()
        }
    }

    override fun unreactBlocking(request: PostsUnreactRequest): Response<PostsUnreactResponse> {
        return toBlocking { unreact(request) }
    }

    override suspend fun startConversation(
        request: PostsStartConversationRequest,
    ): Response<PostsStartConversationResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/posts/${urlEncode(request.postId.orEmpty())}/conversations")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .idempotency(request.idempotencyKey)
                .jsonBody {
                    putOrNull("body", request.body)
                }
                .post()
        }
    }

    override fun startConversationBlocking(
        request: PostsStartConversationRequest,
    ): Response<PostsStartConversationResponse> {
        return toBlocking { startConversation(request) }
    }
}
