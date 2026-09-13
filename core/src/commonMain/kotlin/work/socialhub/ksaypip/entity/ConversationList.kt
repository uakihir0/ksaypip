package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * A conversation under a post, read by anybody who can read the post.
 */
@JsExport
@Serializable
class ConversationList {

    var items: Array<ConversationDigest> = arrayOf()
}
