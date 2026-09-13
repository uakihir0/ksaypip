package work.socialhub.ksaypip.api.request.friendrequests

import kotlin.js.JsExport

/**
 * Accept a friend request. Discloses both profiles, but never retroactively.
 */
@JsExport
class FriendRequestsAcceptRequest {
    var friendRequestId: String? = null
    var idempotencyKey: String? = null
}
