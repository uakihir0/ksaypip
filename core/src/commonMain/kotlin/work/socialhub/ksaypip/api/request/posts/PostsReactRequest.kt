package work.socialhub.ksaypip.api.request.posts

import kotlin.js.JsExport

/**
 * Put one picture on a post. Idempotent: the same URL twice is one reaction.
 *
 * [identified] places the picture under the account's public persona. Absent means anonymous, and
 * a repeat tap does not change the mode of the row already there.
 */
@JsExport
class PostsReactRequest {
    var postId: String? = null
    var emoji: String? = null
    var identified: Boolean? = null
}
