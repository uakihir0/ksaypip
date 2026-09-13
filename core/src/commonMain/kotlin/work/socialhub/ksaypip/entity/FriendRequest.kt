package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * A pending request in the viewer's own queue, which is where most of them are answered.
 */
@JsExport
@Serializable
class FriendRequest {

    var id: String = ""

    var direction: String = ""

    var createdAt: String = ""

    var relationshipId: String = ""

    var counterpart: Person = Person()

    var firstInteractionAt: String = ""
}
