package work.socialhub.ksaypip.api.request.posts

import kotlin.js.JsExport

/**
 * Create a post.
 *
 * [replyToPostId] names one of the caller's own posts and makes this a self-reply; combining it
 * with [wantsTalk] is a validation error. [everyone] false keeps the post out of the Everyone
 * timeline, and requires a hashtag in the body. [identified] writes under the account's public
 * persona. [idempotencyKey] is the client-supplied key that makes a retried write a repeat rather
 * than a second write.
 */
@JsExport
class PostsCreateRequest {
    var body: String? = null
    var mediaIds: Array<String>? = null
    var wantsTalk: Boolean? = null
    var replyToPostId: String? = null
    var everyone: Boolean? = null
    var identified: Boolean? = null
    var idempotencyKey: String? = null
}
