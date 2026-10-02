package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * One line in the notification list, any of the three kinds. Branch on [kind]:
 *
 * - `post.reaction` — somebody reacted to a post of yours. Fields: [postId], [postBody],
 *   [postImage], [reactions], [person], [peopleCount].
 * - `reply.reaction` — somebody reacted to a reply of yours. Fields: [conversationId],
 *   [replyId], [replyBody], [reactions], [person], [peopleCount].
 * - `conversation.reply` — somebody replied in a conversation you are part of. Fields:
 *   [conversationId], [body], [person].
 *
 * `arrivedAt` and [readAt] are common to all three.
 *
 * The halves model different shapes, and this class carries all of them rather than a sealed
 * union because a discriminated union cannot be exported to JavaScript. Unknown fields are
 * ignored on decode; fields not belonging to the line's kind are null.
 */
@JsExport
@Serializable
class Notification {

    /** `post.reaction`, `reply.reaction` or `conversation.reply`. */
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

    // reply.reaction
    var replyId: String? = null

    /** The reader's own reply, quoted so the line is about something. */
    var replyBody: String? = null

    // post.reaction and reply.reaction
    /** Newest kind first. */
    var reactions: Array<NotificationReaction>? = null

    /** How many people are behind this line, [person] included. */
    var peopleCount: Int? = null

    // reply.reaction and conversation.reply
    /** Where the line leads: the conversation the reply is in. */
    var conversationId: String? = null

    // conversation.reply
    /** The newest surviving reply's body, written by the other seat. */
    var body: String? = null
}
