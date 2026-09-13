package work.socialhub.ksaypip.api.request.posts

import kotlin.js.JsExport

/**
 * Take back the ask on one's own post. Idempotent; nothing raises it again.
 */
@JsExport
class PostsRemoveWantsTalkRequest {
    var postId: String? = null
}
