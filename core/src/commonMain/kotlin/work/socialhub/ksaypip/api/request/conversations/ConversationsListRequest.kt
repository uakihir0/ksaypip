package work.socialhub.ksaypip.api.request.conversations

import kotlin.js.JsExport

/**
 * The viewer's own conversations, paged; newest word first.
 */
@JsExport
class ConversationsListRequest {
    var cursor: String? = null
    var limit: Int? = null
}
