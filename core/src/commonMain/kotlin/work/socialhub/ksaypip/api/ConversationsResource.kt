package work.socialhub.ksaypip.api

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
import kotlin.js.JsExport

@JsExport
interface ConversationsResource {

    /**
     * The viewer's own conversations, paged; newest word first.
     */
    suspend fun list(request: ConversationsListRequest): Response<ConversationsListResponse>

    @JsExport.Ignore
    fun listBlocking(request: ConversationsListRequest): Response<ConversationsListResponse>

    /**
     * One conversation, and a page of its replies from the newest end.
     */
    suspend fun conversation(request: ConversationsConversationRequest): Response<ConversationsConversationResponse>

    @JsExport.Ignore
    fun conversationBlocking(request: ConversationsConversationRequest): Response<ConversationsConversationResponse>

    /**
     * Reply in a conversation. Participants only.
     */
    suspend fun reply(request: ConversationsReplyRequest): Response<ConversationsReplyResponse>

    @JsExport.Ignore
    fun replyBlocking(request: ConversationsReplyRequest): Response<ConversationsReplyResponse>

    /**
     * Move your own reading mark to now.
     */
    suspend fun read(request: ConversationsReadRequest): ResponseUnit

    @JsExport.Ignore
    fun readBlocking(request: ConversationsReadRequest): ResponseUnit

    /**
     * Leave one conversation. Either seat may; its replies go with it.
     */
    suspend fun leave(request: ConversationsLeaveRequest): ResponseUnit

    @JsExport.Ignore
    fun leaveBlocking(request: ConversationsLeaveRequest): ResponseUnit
}
