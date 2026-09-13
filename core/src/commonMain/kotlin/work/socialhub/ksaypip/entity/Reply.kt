package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * One thing said in a conversation.
 */
@JsExport
@Serializable
class Reply {

    var id: String = ""

    var body: String = ""

    var createdAt: String = ""

    var side: String = ""

    var isMine: Boolean = false
}
