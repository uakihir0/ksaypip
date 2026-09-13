package work.socialhub.ksaypip.api

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
import kotlin.js.JsExport

@JsExport
interface PostsResource {

    /**
     * A single post, under the same visibility rules as the feed.
     */
    suspend fun post(request: PostsPostRequest): Response<PostsPostResponse>

    @JsExport.Ignore
    fun postBlocking(request: PostsPostRequest): Response<PostsPostResponse>

    /**
     * Who put each picture on this post, as this viewer sees them. Needs a session.
     */
    suspend fun reactions(request: PostsReactionsRequest): Response<PostsReactionsResponse>

    @JsExport.Ignore
    fun reactionsBlocking(request: PostsReactionsRequest): Response<PostsReactionsResponse>

    /**
     * Conversations rooted at this post, readable by third parties.
     */
    suspend fun conversations(request: PostsConversationsRequest): Response<PostsConversationsResponse>

    @JsExport.Ignore
    fun conversationsBlocking(request: PostsConversationsRequest): Response<PostsConversationsResponse>

    /**
     * Create a post. `replyToPostId` makes it a self-reply.
     */
    suspend fun create(request: PostsCreateRequest): Response<PostsCreateResponse>

    @JsExport.Ignore
    fun createBlocking(request: PostsCreateRequest): Response<PostsCreateResponse>

    /**
     * Delete one's own post, softly.
     */
    suspend fun delete(request: PostsDeleteRequest): ResponseUnit

    @JsExport.Ignore
    fun deleteBlocking(request: PostsDeleteRequest): ResponseUnit

    /**
     * Take back the ask on one's own post. Idempotent.
     */
    suspend fun removeWantsTalk(request: PostsRemoveWantsTalkRequest): ResponseUnit

    @JsExport.Ignore
    fun removeWantsTalkBlocking(request: PostsRemoveWantsTalkRequest): ResponseUnit

    /**
     * Put one picture on a post. Idempotent; answers with the whole bar.
     */
    suspend fun react(request: PostsReactRequest): Response<PostsReactResponse>

    @JsExport.Ignore
    fun reactBlocking(request: PostsReactRequest): Response<PostsReactResponse>

    /**
     * Take your own picture back off a post. Idempotent; answers with the whole bar.
     */
    suspend fun unreact(request: PostsUnreactRequest): Response<PostsUnreactResponse>

    @JsExport.Ignore
    fun unreactBlocking(request: PostsUnreactRequest): Response<PostsUnreactResponse>

    /**
     * Start a 1:1 conversation on a post, created with its first reply.
     */
    suspend fun startConversation(request: PostsStartConversationRequest): Response<PostsStartConversationResponse>

    @JsExport.Ignore
    fun startConversationBlocking(request: PostsStartConversationRequest): Response<PostsStartConversationResponse>
}
