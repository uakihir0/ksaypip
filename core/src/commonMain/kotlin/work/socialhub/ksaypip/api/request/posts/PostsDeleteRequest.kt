package work.socialhub.ksaypip.api.request.posts

import kotlin.js.JsExport

/**
 * Delete one's own post, softly. There is no editing.
 */
@JsExport
class PostsDeleteRequest {
    var postId: String? = null
    var idempotencyKey: String? = null
}
