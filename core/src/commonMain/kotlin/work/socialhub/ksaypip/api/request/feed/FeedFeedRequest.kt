package work.socialhub.ksaypip.api.request.feed

import kotlin.js.JsExport

/**
 * The Global Room, paged; 7-day window, block-filtered.
 */
@JsExport
class FeedFeedRequest {
    var cursor: String? = null
    var limit: Int? = null
}
