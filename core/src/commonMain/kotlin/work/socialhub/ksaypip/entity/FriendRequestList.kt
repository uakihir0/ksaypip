package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * Incoming and outgoing pending friend requests.
 */
@JsExport
@Serializable
class FriendRequestList {

    var items: Array<FriendRequest> = arrayOf()
}
