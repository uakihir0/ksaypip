package work.socialhub.ksaypip.api.request.conversations

import kotlin.js.JsExport

/**
 * Reply in a conversation. Participants only; [idempotencyKey] makes a retry a repeat.
 * [identified] writes the reply under the account's public persona.
 */
@JsExport
class ConversationsReplyRequest {
    var conversationId: String? = null
    var body: String? = null
    var identified: Boolean? = null
    var idempotencyKey: String? = null
}
