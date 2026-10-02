package work.socialhub.ksaypip.api.request.replies

import kotlin.js.JsExport

/**
 * Take your own picture back off a reply. Succeeds when there was nothing to remove.
 */
@JsExport
class RepliesUnreactRequest {
    var replyId: String? = null
    var emoji: String? = null
}
