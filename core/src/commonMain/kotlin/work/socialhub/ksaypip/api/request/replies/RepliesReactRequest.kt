package work.socialhub.ksaypip.api.request.replies

import kotlin.js.JsExport

/**
 * Put one picture on a reply. Idempotent: the same URL twice is one reaction. Only the
 * conversation's two participants may write here, and a reply of the caller's own is refused.
 *
 * [identified] places the picture under the account's public persona. Absent means anonymous.
 */
@JsExport
class RepliesReactRequest {
    var replyId: String? = null
    var emoji: String? = null
    var identified: Boolean? = null
}
