package work.socialhub.ksaypip.api.request.feed

import kotlin.js.JsExport

/**
 * The feed filtered by a phrase: the same window and filters, no author narrowing.
 *
 * The phrase is counted in code points by the server, at least two and at most sixty-four.
 */
@JsExport
class FeedSearchRequest {
    var q: String? = null
    var cursor: String? = null
    var limit: Int? = null
}
