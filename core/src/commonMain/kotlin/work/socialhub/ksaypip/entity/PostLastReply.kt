package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * The last thing said in a conversation, as a post row quotes it.
 *
 * `side` is the conversation-local seat, never a person: naming the speaker would put an author
 * identifier on a timeline row.
 */
@JsExport
@Serializable
class PostLastReply {

    var body: String = ""

    var side: String = ""
}
