package work.socialhub.ksaypip.api.request.friendrequests

import kotlin.js.JsExport

/**
 * Decline a friend request. The counterpart is not told.
 */
@JsExport
class FriendRequestsDeclineRequest {
    var friendRequestId: String? = null
    var idempotencyKey: String? = null
}
