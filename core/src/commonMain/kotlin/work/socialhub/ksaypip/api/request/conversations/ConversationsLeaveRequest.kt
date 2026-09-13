package work.socialhub.ksaypip.api.request.conversations

import kotlin.js.JsExport

/**
 * Leave one conversation. Either seat may; its replies go with it.
 */
@JsExport
class ConversationsLeaveRequest {
    var conversationId: String? = null
    var idempotencyKey: String? = null
}
