package work.socialhub.ksaypip.api.request.conversations

import kotlin.js.JsExport

/**
 * Move your own reading mark to now. Participants only.
 */
@JsExport
class ConversationsReadRequest {
    var conversationId: String? = null
}
