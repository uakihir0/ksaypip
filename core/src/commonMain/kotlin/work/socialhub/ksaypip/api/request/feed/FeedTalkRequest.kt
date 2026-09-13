package work.socialhub.ksaypip.api.request.feed

import kotlin.js.JsExport

/**
 * The feed narrowed to the posts whose author is asking to be talked to.
 */
@JsExport
class FeedTalkRequest {
    var cursor: String? = null
    var limit: Int? = null
}
