package work.socialhub.ksaypip.api.request.feed

import kotlin.js.JsExport

/**
 * Your friends' posts, paged; past the window, and only since each friendship.
 */
@JsExport
class FeedFriendsRequest {
    var cursor: String? = null
    var limit: Int? = null
}
