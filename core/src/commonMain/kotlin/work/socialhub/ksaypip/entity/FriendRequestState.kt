package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * A pending friend request, as the viewer's own queue draws it.
 */
@JsExport
@Serializable
class FriendRequestState {

    var id: String = ""

    var direction: String = ""

    var createdAt: String = ""
}
