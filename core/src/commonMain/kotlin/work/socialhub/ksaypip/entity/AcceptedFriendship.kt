package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * What accepting a friend request answers with.
 */
@JsExport
@Serializable
class AcceptedFriendship {

    var relationshipId: String = ""

    var friendSince: String = ""
}
