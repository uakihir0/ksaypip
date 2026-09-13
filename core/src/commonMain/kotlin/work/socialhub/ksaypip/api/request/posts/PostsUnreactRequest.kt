package work.socialhub.ksaypip.api.request.posts

import kotlin.js.JsExport

/**
 * Take your own picture back off a post. Idempotent.
 */
@JsExport
class PostsUnreactRequest {
    var postId: String? = null
    var emoji: String? = null
}
