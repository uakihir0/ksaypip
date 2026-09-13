package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * One picture on the post a notification is about, and how many of it there are now.
 */
@JsExport
@Serializable
class NotificationReaction {

    var emoji: String = ""

    var count: Int = 0
}
