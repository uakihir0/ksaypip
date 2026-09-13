package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * A conversation as it appears in a list — all three lists return this shape.
 *
 * The post the conversation hangs off is referenced by ID only and never quoted: its body is
 * under the 7-day window for a non-friend, and a permanent list of excerpts would be that window
 * undone.
 */
@JsExport
@Serializable
class ConversationDigest {

    var id: String = ""

    var postId: String = ""

    var createdAt: String = ""

    var lastReplyAt: String = ""

    var participants: Array<Participant> = arrayOf()

    var isMine: Boolean = false

    var lastReply: PostLastReply? = null

    /** Whether something was said here that you have not read. */
    var unread: Boolean = false
}
