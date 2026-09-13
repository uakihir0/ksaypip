package work.socialhub.ksaypip.api.request.me

import kotlin.js.JsExport

/**
 * The viewer's own posts, paged. The only post list without a window over it.
 */
@JsExport
class MePostsRequest {
    var cursor: String? = null
    var limit: Int? = null
}
