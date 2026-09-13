package work.socialhub.ksaypip.api.request.friendrequests

import kotlin.js.JsExport

/**
 * Ask to become friends, addressing the counterpart by the identity the caller holds.
 *
 * Requires a conversation threshold: a `precondition_failed` /
 * `conversation_threshold_not_met` otherwise.
 */
@JsExport
class FriendRequestsRequestRequest {
    var identity: String? = null
    var idempotencyKey: String? = null
}
