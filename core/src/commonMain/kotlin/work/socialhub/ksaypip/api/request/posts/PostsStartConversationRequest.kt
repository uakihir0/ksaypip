package work.socialhub.ksaypip.api.request.posts

import kotlin.js.JsExport

/**
 * Start a 1:1 conversation on a post. [identified] writes the opening line under the account's
 * public persona. [idempotencyKey] makes a retried request a repeat.
 */
@JsExport
class PostsStartConversationRequest {
    var postId: String? = null
    var body: String? = null
    var identified: Boolean? = null
    var idempotencyKey: String? = null
}
