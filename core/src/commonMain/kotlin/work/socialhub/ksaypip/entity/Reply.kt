package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * One thing said in a conversation.
 *
 * [identified] says the line was written in the identified mode, and then the public name and
 * badge travel with the line itself ([identifiedAuthor]), because a reply's author is otherwise
 * said by [side] against the conversation's participants.
 */
@JsExport
@Serializable
class Reply {

    var id: String = ""

    var body: String = ""

    var createdAt: String = ""

    var side: String = ""

    var isMine: Boolean = false

    /** Whether the reply was written in the identified mode. */
    var identified: Boolean = false

    /** The public author of an identified reply; null on every other one. */
    var identifiedAuthor: IdentifiedPerson? = null

    /** The pictures on this reply, oldest first, counted. */
    var reactions: Array<PostReaction> = arrayOf()
}
