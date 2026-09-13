package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * A conversation and a page of what was said in it, oldest of the page first.
 *
 * The one paged list in this API whose cursor goes backwards in time: a conversation is read
 * oldest-first, so the first page is the newest lines and `olderRepliesCursor` asks for what was
 * said before them.
 */
@JsExport
@Serializable
class Conversation {

    var id: String = ""

    var postId: String = ""

    var createdAt: String = ""

    var lastReplyAt: String = ""

    var participants: Array<Participant> = arrayOf()

    var isMine: Boolean = false

    var replies: Array<Reply> = arrayOf()

    /** False for third parties, who are offered their own conversation on the post instead. */
    var canReply: Boolean = false

    /** Pass as `cursor` for the replies written before this page. Null at the beginning. */
    var olderRepliesCursor: String? = null

    /** The post the conversation hangs off. Null where the post was deleted. */
    var originPost: Post? = null
}
