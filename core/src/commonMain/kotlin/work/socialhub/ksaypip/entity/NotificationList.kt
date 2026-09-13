package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * What happened to you, newest arrival first.
 */
@JsExport
@Serializable
class NotificationList {

    var items: Array<Notification> = arrayOf()

    var nextCursor: String? = null
}
