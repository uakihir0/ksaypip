package work.socialhub.ksaypip.api.request.posts

import kotlin.js.JsExport

/**
 * Put one picture on a post. Idempotent: the same URL twice is one reaction.
 */
@JsExport
class PostsReactRequest {
    var postId: String? = null
    var emoji: String? = null
}
