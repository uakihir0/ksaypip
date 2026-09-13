package work.socialhub.ksaypip.api.request.feed

import kotlin.js.JsExport

/**
 * The feed filtered to one hashtag, without the `#`.
 */
@JsExport
class FeedTagRequest {
    var tag: String? = null
    var cursor: String? = null
    var limit: Int? = null
}
