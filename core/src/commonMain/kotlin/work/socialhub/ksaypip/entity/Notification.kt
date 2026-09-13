package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * One line in the notification list, either kind. Branch on [kind]:
 *
 * - `post.reaction` — somebody reacted to a post of yours. Fields: [postId], [postBody],
 *   [postImage], [reactions], [person], [peopleCount].
 * - `conversation.reply` — somebody replied in a conversation you are part of. Fields:
 *   [conversationId], [body], [person].
 *
 * `arrivedAt` and [readAt] are common to both.
 *
 * The two halves model different shapes, and this class carries both rather than a sealed union
 * because a discriminated union cannot be exported to JavaScript. Unknown fields are ignored on
 * decode; fields not belonging to the line's kind are null.
 */
@JsExport
@Serializable
class Notification {

    /** `post.reaction` or `conversation.reply`. */
    var kind: String = ""

    /** When the most recent arrival arrived. */
    var arrivedAt: String = ""

    /** Null while still news. Otherwise when the reader last said they had seen it. */
    var readAt: String? = null

    /** The other seat, where you have a name for them — otherwise null. */
    var person: Person? = null

    // post.reaction
    var postId: String? = null

    var postBody: String? = null

    var postImage: Media? = null

    /** Newest kind first. */
    var reactions: Array<NotificationReaction>? = null

    /** How many people are behind this line, [person] included. */
    var peopleCount: Int? = null

    // conversation.reply
    var conversationId: String? = null

    /** The newest surviving reply's body, written by the other seat. */
    var body: String? = null
}
