package work.socialhub.ksaypip.api.request.posts

import kotlin.js.JsExport

/**
 * Start a 1:1 conversation on a post. [idempotencyKey] makes a retried request a repeat.
 */
@JsExport
class PostsStartConversationRequest {
    var postId: String? = null
    var body: String? = null
    var idempotencyKey: String? = null
}
