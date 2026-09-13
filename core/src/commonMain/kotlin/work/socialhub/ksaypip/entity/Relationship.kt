package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * A mutable relationship between the viewer and one counterpart, with its label, note and mark.
 */
@JsExport
@Serializable
class Relationship {

    var id: String = ""

    var counterpart: Person = Person()

    /** The viewer's private memo about the counterpart. Returned only on the relationship page. */
    var note: String? = null

    var firstInteractionAt: String = ""

    var friendSince: String? = null

    var conversations: Array<ConversationDigest> = arrayOf()

    var friendRequest: FriendRequestState? = null

    /** Whether `POST /friend-requests` would be accepted for this counterpart right now. */
    var canSendFriendRequest: Boolean = false
}
