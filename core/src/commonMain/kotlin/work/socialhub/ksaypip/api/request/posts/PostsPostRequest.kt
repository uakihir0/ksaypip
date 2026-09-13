package work.socialhub.ksaypip.api.request.posts

import kotlin.js.JsExport

/**
 * A single post, under the same visibility rules as the feed.
 */
@JsExport
class PostsPostRequest {
    var postId: String? = null
}
