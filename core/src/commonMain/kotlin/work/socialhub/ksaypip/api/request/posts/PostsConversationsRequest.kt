package work.socialhub.ksaypip.api.request.posts

import kotlin.js.JsExport

/**
 * Conversations rooted at a post, readable by third parties.
 */
@JsExport
class PostsConversationsRequest {
    var postId: String? = null
}
