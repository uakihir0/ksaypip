package work.socialhub.ksaypip.api

import work.socialhub.ksaypip.api.request.feedback.FeedbackSendRequest
import work.socialhub.ksaypip.api.response.ResponseUnit
import kotlin.js.JsExport

@JsExport
interface FeedbackResource {

    /**
     * Tell us about saypip. Delivered, and stored nowhere.
     */
    suspend fun send(request: FeedbackSendRequest): ResponseUnit

    @JsExport.Ignore
    fun sendBlocking(request: FeedbackSendRequest): ResponseUnit
}
