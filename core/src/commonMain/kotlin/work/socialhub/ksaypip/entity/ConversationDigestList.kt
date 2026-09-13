package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * A page of the viewer's own conversations, newest word first.
 */
@JsExport
@Serializable
class ConversationDigestList {

    var items: Array<ConversationDigest> = arrayOf()

    var nextCursor: String? = null
}
