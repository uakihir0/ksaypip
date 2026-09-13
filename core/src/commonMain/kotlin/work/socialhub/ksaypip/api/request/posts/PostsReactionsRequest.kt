package work.socialhub.ksaypip.api.request.posts

import kotlin.js.JsExport

/**
 * Who put each picture on a post, as this viewer sees them. Needs a session.
 */
@JsExport
class PostsReactionsRequest {
    var postId: String? = null
}
