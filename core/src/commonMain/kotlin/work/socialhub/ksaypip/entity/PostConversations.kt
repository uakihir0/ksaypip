package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * What is being said under a post: how much of it there is, and the last thing said.
 *
 * `mine` is the conversation this reader started, and only that: the author of a post is a
 * participant in every conversation on it, so the flag would say nothing on their own post.
 */
@JsExport
@Serializable
class PostConversations {

    var count: Int = 0

    var mine: Boolean = false

    /** The newest surviving reply across the live conversations, or null where there are none. */
    var lastReply: PostLastReply? = null
}
