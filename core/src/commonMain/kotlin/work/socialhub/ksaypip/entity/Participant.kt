package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * One seat in a conversation.
 *
 * `side` is the conversation-local marker (`a` is the post's author, `b` started the
 * conversation), [person] is null where the viewer may not be shown who it is, and [isMe] says
 * whether this is the viewer's own seat.
 */
@JsExport
@Serializable
class Participant {

    var side: String = ""

    var person: Person? = null

    var isMe: Boolean = false
}
