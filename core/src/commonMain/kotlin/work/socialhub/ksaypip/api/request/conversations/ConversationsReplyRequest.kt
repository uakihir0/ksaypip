package work.socialhub.ksaypip.api.request.conversations

import kotlin.js.JsExport

/**
 * Reply in a conversation. Participants only; [idempotencyKey] makes a retry a repeat.
 */
@JsExport
class ConversationsReplyRequest {
    var conversationId: String? = null
    var body: String? = null
    var idempotencyKey: String? = null
}
