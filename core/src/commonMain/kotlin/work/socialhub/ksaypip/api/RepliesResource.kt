package work.socialhub.ksaypip.api

import work.socialhub.ksaypip.api.request.replies.RepliesReactRequest
import work.socialhub.ksaypip.api.request.replies.RepliesUnreactRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.replies.RepliesReactResponse
import work.socialhub.ksaypip.api.response.replies.RepliesUnreactResponse
import kotlin.js.JsExport

@JsExport
interface RepliesResource {

    /**
     * Put one picture on a reply. Idempotent. Participants only; your own reply is refused.
     */
    suspend fun react(request: RepliesReactRequest): Response<RepliesReactResponse>

    @JsExport.Ignore
    fun reactBlocking(request: RepliesReactRequest): Response<RepliesReactResponse>

    /**
     * Take your own picture back off a reply. Succeeds when there was nothing to remove.
     */
    suspend fun unreact(request: RepliesUnreactRequest): Response<RepliesUnreactResponse>

    @JsExport.Ignore
    fun unreactBlocking(request: RepliesUnreactRequest): Response<RepliesUnreactResponse>
}
