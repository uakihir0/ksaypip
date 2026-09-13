package work.socialhub.ksaypip.api.request.conversations

import kotlin.js.JsExport

/**
 * One conversation, and a page of its replies from the newest end.
 *
 * `cursor` here asks for the replies written *before* this page; the first page is the newest
 * lines.
 */
@JsExport
class ConversationsConversationRequest {
    var conversationId: String? = null
    var cursor: String? = null
    var limit: Int? = null
}
